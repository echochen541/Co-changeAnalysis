package cn.edu.fudan.se.cochange_analysis.preprocess;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParentKey;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitChangeFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitParentDAO;

public class CommitFilter {
	private GitRepository gitRepository;

	public CommitFilter() {
		// TODO Auto-generated constructor stub
	}

	public CommitFilter(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		CommitFilter commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		commitFilter = new CommitFilter(gitRepository);
		// System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();
	}

	public void filterCommits() {
		int repositoryId = gitRepository.getRepositoryId();
		List<GitCommit> commits = GitCommitDAO.selectByRepositoryId(repositoryId);
		for (GitCommit commit : commits) {
			String commitId = commit.getCommitId();
			System.out.println(repositoryId + " : " + commitId);
			List<GitCommitParentKey> parentCommits = GitCommitParentDAO.selectByRepositoryIdAndCommitId(repositoryId,
					commitId);
			// System.out.println(parentCommits);
			// filter merge commit
			if (parentCommits.size() == 1) {
				List<GitChangeFile> changeFiles = GitChangeFileDAO.selectByRepositoryIdAndCommitId(repositoryId,
						commitId);
				// System.out.println(changeFiles.size());
				List<GitChangeFile> filteredFiles = filterChangeFiles(changeFiles);
				// System.out.println(filteredFiles.size());
				if (filteredFiles.size() >= 2 && filteredFiles.size() <= 30) {
					// store filtered commit
					GitCommitDAO.insertFilteredCommit(commit);
					// store filtered commit change file
					for (GitChangeFile filteredFile : filteredFiles) {
						GitChangeFileDAO.insertChangeFileFilter(filteredFile);
					}
				}
			}
			// System.out.println();
		}
	}

	private List<GitChangeFile> filterChangeFiles(List<GitChangeFile> changeFiles) {
		List<GitChangeFile> filteredFiles = new ArrayList<GitChangeFile>();
		for (GitChangeFile changeFile : changeFiles) {
			String fileName = changeFile.getFileName();
			String changeType = changeFile.getChangeType();
			// java file and non test java file
			// System.out.println(fileName);
			if (changeType.equals("MODIFY") && fileName.endsWith(".java") && !fileName.endsWith("Test.java")) {
				filteredFiles.add(changeFile);
			}
		}
		return filteredFiles;
	}
}
