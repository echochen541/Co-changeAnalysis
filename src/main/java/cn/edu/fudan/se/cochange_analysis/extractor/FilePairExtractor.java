package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
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

	}

	public void extractFilePairHistory() {
		int repositoryId = repository.getRepositoryId();
		// System.out.println(repositoryId);
		String repositoryName = repository.getRepositoryName();
		List<GitCommit> filteredCommits = GitCommitDAO.selectFilteredByRepositoryId(repositoryId);
		String splitString = "||";

		for (GitCommit commit : filteredCommits) {
			String commitId = commit.getCommitId();
			List<GitChangeFile> changeFiles = GitChangeFileDAO.selectFilteredByRepositoryIdAndCommitId(repositoryId,
					commitId);
			// System.out.println(repositoryId + " : " + commitId);
			List<FilePairCommit> filePairCommits = new ArrayList<FilePairCommit>();

			for (int i = 0; i < changeFiles.size() - 1; i++) {
				GitChangeFile changeFile1 = changeFiles.get(i);
				String filePath1 = changeFile1.getFileName();
				String fileName1 = FileUtils.parseFilePath(filePath1, repositoryName);

				for (int j = i + 1; j < changeFiles.size(); j++) {
					GitChangeFile changeFile2 = changeFiles.get(j);
					String filePath2 = changeFile2.getFileName();
					String fileName2 = FileUtils.parseFilePath(filePath2, repositoryName);

					String filePair = null;
					FilePairCommit filePairCommit = null;

					int compare = fileName1.compareTo(fileName2);
					if (compare > 0) {
						filePair = fileName2 + splitString + fileName1;
						filePairCommit = new FilePairCommit(repositoryId, commitId, filePath2, filePath1, filePair);
					} else {
						filePair = fileName1 + splitString + fileName2;
						filePairCommit = new FilePairCommit(repositoryId, commitId, filePath1, filePath2, filePair);
					}
					// FilePairCommitDAO.insertFilePairCommit(filePairCommit);
					filePairCommits.add(filePairCommit);
				}
			}
			FilePairCommitDAO.insertBatch(filePairCommits);
		}
	}
}
