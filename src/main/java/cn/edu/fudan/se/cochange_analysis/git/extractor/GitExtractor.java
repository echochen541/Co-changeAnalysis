package cn.edu.fudan.se.cochange_analysis.git.extractor;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;

public class GitExtractor {
	private GitRepository repository;

	public GitExtractor(GitRepository repository) {
		this.repository = repository;
	}

	public void extractCommitHistory() {
		Integer repositoryId = repository.getRepositoryId();
		try {
			Git git = Git.open(new File(repository.getRepositoryPath()));
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

				GitCommit commit = new GitCommit(repositoryId, commitId, shortMessage, fullMessage, authorName, authorEmail,
						authoredDate, committerName, committerEmail, committedDate);
				System.out.println(commitId);
				GitCommitDAO.insertCommit(commit);

				// current commit has more than zero parent
				if (revCommit.getParentCount() > 0) {
					// store parent commit

					// only one parent
					if (revCommit.getParentCount() == 1) {
						RevCommit preRevCommit = revCommit.getParent(0);
						extractChangedFiles(revCommit, preRevCommit);
					}
				}
			}
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	}

	private void extractChangedFiles(RevCommit revCommit, RevCommit preRevCommit) {
		
	}

	public static void main(String[] args) {
		GitRepository repository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		GitExtractor gitExtractor = new GitExtractor(repository);
		gitExtractor.extractCommitHistory();
	}
}
