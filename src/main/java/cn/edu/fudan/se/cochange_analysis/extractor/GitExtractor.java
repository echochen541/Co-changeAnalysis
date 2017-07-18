package cn.edu.fudan.se.cochange_analysis.extractor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParentKey;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitChangeFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitParentDAO;

public class GitExtractor {
	private GitRepository gitRepository;
	private Git git;
	private Repository repository;
	private RevWalk revWalk;

	public GitExtractor(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
		try {
			git = Git.open(new File(gitRepository.getRepositoryPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		repository = git.getRepository();
		revWalk = new RevWalk(repository);
	}

	public void extractCommitHistory() {
		Integer gitRepositoryId = gitRepository.getRepositoryId();
		try {
			Git git = Git.open(new File(gitRepository.getRepositoryPath()));
			Iterable<RevCommit> gitlog = git.log().call();
			for (RevCommit revCommit : gitlog) {
				String commitId = revCommit.getName();
				String shortMessage = revCommit.getShortMessage();
				String fullMessage = revCommit.getFullMessage();
				String authorName = revCommit.getAuthorIdent().getName();
				String authorEmail = revCommit.getAuthorIdent().getEmailAddress();
				Date authoredDate = revCommit.getAuthorIdent().getWhen();
				String committerName = revCommit.getCommitterIdent().getName();
				String committerEmail = revCommit.getCommitterIdent().getEmailAddress();
				Date committedDate = revCommit.getCommitterIdent().getWhen();

				GitCommit commit = new GitCommit(gitRepositoryId, commitId, shortMessage, fullMessage, authorName,
						authorEmail, authoredDate, committerName, committerEmail, committedDate);
				System.out.println(gitRepositoryId + "," + commitId);
				GitCommitDAO.insertCommit(commit);

				// current commit has more than zero parent
				if (revCommit.getParentCount() > 0) {
					RevCommit[] parentRevCommits = revCommit.getParents();
					// store parent commit
					for (RevCommit parentRevCommit : parentRevCommits) {
						String parentCommitId = parentRevCommit.getName();
						GitCommitParentKey commitParent = new GitCommitParentKey(gitRepositoryId, commitId,
								parentCommitId);
						GitCommitParentDAO.insertCommitParent(commitParent);
						// System.out.println(commitId + " " + commitParent);
					}
				}

				// only one parent
				if (revCommit.getParentCount() == 1) {
					RevCommit preRevCommit = revCommit.getParent(0);
					extractChangedFiles(revCommit, preRevCommit);
				}
			}
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	}

	private void extractChangedFiles(RevCommit revCommit, RevCommit parentRevCommit) {
		AbstractTreeIterator currentTreeParser = prepareTreeParser(revCommit.getName());
		AbstractTreeIterator prevTreeParser = prepareTreeParser(parentRevCommit.getName());
		List<DiffEntry> diffs = null;
		try {
			diffs = git.diff().setNewTree(currentTreeParser).setOldTree(prevTreeParser).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		for (DiffEntry diff : diffs) {
			String newPath = diff.getNewPath();
			String oldPath = diff.getOldPath();
			String fileName = null;
			String changeType = diff.getChangeType().name();
			if (ChangeType.DELETE.name().equals(changeType))
				fileName = oldPath;
			else
				fileName = newPath;

			GitChangeFile changeFile = new GitChangeFile(gitRepository.getRepositoryId(), revCommit.getName(), fileName,
					changeType, oldPath, newPath);
			// System.out.println(changeFile);
			// System.out.println(changeFile.getFileName());
			GitChangeFileDAO.insertChangeFile(changeFile);
		}
	}

	// from the commit we can build the tree which allows us to construct the
	// TreeParser
	public CanonicalTreeParser prepareTreeParser(String objectId) {
		CanonicalTreeParser treeParser = new CanonicalTreeParser();

		try {
			RevCommit commit = revWalk.parseCommit(ObjectId.fromString(objectId));
			RevTree tree = revWalk.parseTree(commit.getTree().getId());
			ObjectReader oldReader = repository.newObjectReader();
			treeParser.reset(oldReader, tree.getId());
			revWalk.dispose();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return treeParser;
	}

	public byte[] getFileContentByCommitId(String commitId, String filePath) {
		if (commitId == null || filePath == null) {
			System.err.println("revisionId or fileName is null");
			return null;
		}
		if (repository == null || git == null || revWalk == null) {
			System.err.println("git repository is null..");
			return null;
		}

		try {
			ObjectId objId = repository.resolve(commitId);
			if (objId == null) {
				System.err.println("The commit: " + commitId + " does not exist.");
				return null;
			}
			RevCommit revCommit = revWalk.parseCommit(objId);
			if (revCommit != null) {
				RevTree revTree = revCommit.getTree();
				TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, revTree);
				ObjectId blobId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(blobId);
				byte[] bytes = loader.getBytes();
				return bytes;

				// InputStream input = FileUtils.open(blobId, repository);
				// byte[] bytes = FileUtils.toByteArray(input);
				// return bytes;
			} else {
				System.err.println("Cannot found file(" + filePath + ") in commit (" + commitId + "): " + revWalk);
			}
		} catch (RevisionSyntaxException e) {
			e.printStackTrace();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		GitExtractor gitExtractor = new GitExtractor(gitRepository);
		gitExtractor.getModifiedLineOfCode("471d69c3a99fdc3977b6b583d339af654f7a148b",
				"7a837ff7d7b994306ca248b6f89b89d5c9f6a134");
	}

	public Map<String, Integer> getModifiedLineOfCode(String commitId, String parentCommitId) {
		// System.out.println(parentCommitId + "," + commitId);

		Map<String, Integer> file2Loc = new HashMap<String, Integer>();
		AbstractTreeIterator newTree = prepareTreeParser(commitId), oldTree = prepareTreeParser(parentCommitId);
		try {
			List<DiffEntry> diff = git.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true)
					.call();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			@SuppressWarnings("resource")
			DiffFormatter df = new DiffFormatter(out);
			// 设置比较器为忽略空白字符对比（Ignores all whitespace）
			// df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
			df.setRepository(git.getRepository());
			System.out.println("------------------------------start-----------------------------");
			// 每一个diffEntry都是第个文件版本之间的变动差异
			for (DiffEntry diffEntry : diff) {
				String newPath = diffEntry.getNewPath();
				String oldPath = diffEntry.getOldPath();
				String fileName = null;
				String changeType = diffEntry.getChangeType().name();

				if (ChangeType.DELETE.name().equals(changeType))
					fileName = oldPath;
				else
					fileName = newPath;

				if (ChangeType.MODIFY.name().equals(changeType) && fileName.endsWith(".java")
						&& !fileName.contains("/test/") && !fileName.contains("/tester/")) {
					// 打印文件差异具体内容
					// df.format(diffEntry);
					// String diffText = out.toString("UTF-8");
					// System.out.println(diffText);

					// 获取文件差异位置，从而统计差异的行数，如增加行数，减少行数
					FileHeader fileHeader = df.toFileHeader(diffEntry);
					@SuppressWarnings("unchecked")
					List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();
					int addSize = 0, subSize = 0;
					for (HunkHeader hunkHeader : hunks) {
						EditList editList = hunkHeader.toEditList();
						for (Edit edit : editList) {
							subSize += edit.getEndA() - edit.getBeginA();
							addSize += edit.getEndB() - edit.getBeginB();
						}
					}
					System.out.println(fileName);
					System.out.println("addSize=" + addSize);
					System.out.println("subSize=" + subSize);
					System.out.println("------------------------------end-----------------------------");
					file2Loc.put(fileName, addSize + subSize);
					out.reset();
				}
			}
			out.close();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file2Loc;
	}
}
