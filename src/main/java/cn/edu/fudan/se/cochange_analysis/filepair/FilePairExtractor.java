package cn.edu.fudan.se.cochange_analysis.filepair;

import java.util.List;

import cn.edu.fudan.se.cochange_analysis.change.extractor.ChangeExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitChangeFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;

public class FilePairExtractor {
	private GitRepository repository;

	public FilePairExtractor() {
		// TODO Auto-generated constructor stub
	}

	public FilePairExtractor(GitRepository repository) {
		this.repository = repository;
	}

	public GitRepository getRepository() {
		return repository;
	}

	public void setRepository(GitRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		FilePairExtractor extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();
	}

	public void extractFilePairHistory() {
		int repositoryId = repository.getRepositoryId();
		String repositoryName = repository.getRepositoryName();
		List<GitCommit> filteredCommits = GitCommitDAO.selectFilteredByRepositoryId(repositoryId);
		String matchString = "org/apache/" + repositoryName;
		String splitString = "||";

		for (GitCommit commit : filteredCommits) {
			String commitId = commit.getCommitId();
			List<GitChangeFile> changeFiles = GitChangeFileDAO.selectFilteredByRepositoryIdAndCommitId(repositoryId,
					commitId);
			System.out.println(repositoryId + " : " + commitId);
			
			for (int i = 0; i < changeFiles.size() - 1; i++) {
				GitChangeFile changeFile1 = changeFiles.get(i);
				String filePath1 = changeFile1.getFileName();
				// System.out.println(filePath1);
				String fileName1 = filePath1.substring(filePath1.indexOf(matchString));
				for (int j = i + 1; j < changeFiles.size(); j++) {
					GitChangeFile changeFile2 = changeFiles.get(j);
					String filePath2 = changeFile2.getFileName();
					String fileName2 = filePath2.substring(filePath2.indexOf(matchString));
					String filePair = null;
					int compare = fileName1.compareTo(fileName2);
					if (compare > 0)
						filePair = fileName2 + splitString + fileName1;
					else if (compare < 0)
						filePair = fileName1 + splitString + fileName2;
					else {
						System.out.println("duplicated short names");
						System.exit(0);
					}

					FilePairCommit filePairCommit = new FilePairCommit(repositoryId, commitId, filePath1, filePath2,
							filePair);
					FilePairCommitDAO.insertFilePairCommit(filePairCommit);
				}
			}
		}
	}

}
