package cn.edu.fudan.se.cochange_analysis.extractor;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
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
		GitRepository gitRepository = new GitRepository(2, "cassandra",
				"D:/echo/lab/research/co-change/projects/cassandra/.git");
		GitExtractor gitExtractor = new GitExtractor(gitRepository);

		byte[] content1 = gitExtractor.getFileContentByCommitId("55b617c6a94705158282d5889d635510d137d5e4",
				"interface/gen-java/org/apache/cassandra/service/NotFoundException.java");
		byte[] content2 = gitExtractor.getFileContentByCommitId("04d5ec33b2011f6d275b8d2f9ee29dcafde203cb",
				"interface/gen-java/org/apache/cassandra/service/NotFoundException.java");

		System.out.println(new String(content1));
		System.out.println(new String(content2));

		// create temp files before and after the commit
		File left = FileUtils.writeBytesToFile(content1, "4f7b2027-7638-4750-9359-b7ccd91fb920", "A.v1");
		File right = FileUtils.writeBytesToFile(content2, "4f7b2027-7638-4750-9359-b7ccd91fb920", "A.v2");

		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		try {
			distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch (Exception e) {
			System.err.println("Warning: error while change distilling. " + e.getMessage());
		}

		// delete temp files
		// left.delete();
		// right.delete();

		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		System.out.println(changes);

		// gitExtractor.extractCommitHistory();

		// gitRepository = new GitRepository(2, "cassandra",
		// "D:/echo/lab/research/co-change/projects/cassandra/.git");
		// gitExtractor = new GitExtractor(gitRepository);
		// gitExtractor.extractCommitHistory();

		// GitRepository gitRepository = new GitRepository(3, "cxf",
		// "D:/echo/lab/research/co-change/projects/cxf/.git");
		// GitExtractor gitExtractor = new GitExtractor(gitRepository);
		// gitExtractor.extractCommitHistory();

		// gitRepository = new GitRepository(4, "hadoop",
		// "D:/echo/lab/research/co-change/projects/hadoop/.git");
		// gitExtractor = new GitExtractor(gitRepository);
		// gitExtractor.extractCommitHistory();

		// gitRepository = new GitRepository(5, "hbase",
		// "D:/echo/lab/research/co-change/projects/hbase/.git");
		// gitExtractor = new GitExtractor(gitRepository);
		// gitExtractor.extractCommitHistory();

		// gitRepository = new GitRepository(6, "wicket",
		// "D:/echo/lab/research/co-change/projects/wicket/.git");
		// gitExtractor = new GitExtractor(gitRepository);
		// gitExtractor.extractCommitHistory();
	}
}
