package cn.edu.fudan.se.cochange_analysis.git.extractor;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParent;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitChangeFileDAO;
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
				// System.out.println(commitId);
				// GitCommitDAO.insertCommit(commit);

				// current commit has more than zero parent
				if (revCommit.getParentCount() > 0) {
					RevCommit[] parentRevCommits = revCommit.getParents();
					// store parent commit
					for (RevCommit parentRevCommit : parentRevCommits) {
						String parentCommitId = parentRevCommit.getName();
						GitCommitParent commitParent = new GitCommitParent(gitRepositoryId, commitId, parentCommitId);
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return treeParser;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		GitExtractor gitExtractor = new GitExtractor(gitRepository);
		gitExtractor.extractCommitHistory();
	}
}
