package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
import cn.edu.fudan.se.cochange_analysis.git.bean.BugFixFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParentKey;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.IssueBug;
import cn.edu.fudan.se.cochange_analysis.git.dao.BugFixFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitChangeFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitParentDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.IssueBugDAO;

public class BugExtractor {
	private GitRepository gitRepository;

	public BugExtractor() {
	}

	public BugExtractor(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		BugExtractor extractor = new BugExtractor(gitRepository);
		extractor.extractBug();
	}

	public void extractBug() {
		GitExtractor gitExtractor = new GitExtractor(gitRepository);
		int gitRepositoryId = gitRepository.getRepositoryId();
		String gitRepositoryName = gitRepository.getRepositoryName();
		String regex = null;
		if (gitRepositoryName.equals("hadoop"))
			regex = "(HADOOP|HDFS|HDT|MAPREDUCE|YARN)-\\d+";
		else
			regex = gitRepositoryName.toUpperCase() + "-\\d+";
		Pattern pattern = Pattern.compile(regex);

		List<GitCommit> commitList = GitCommitDAO.selectByRepositoryId(gitRepositoryId);
		for (GitCommit commit : commitList) {
			String message = commit.getShortMessage();
			Matcher matcher = pattern.matcher(message);

			// exclude duplicate issue ids
			Set<String> issueIdList = new HashSet<String>();
			while (matcher.find()) {
				issueIdList.add(matcher.group());
			}

			for (String issueId : issueIdList) {
				IssueBug issueBug = IssueBugDAO.selectByRepositoryIdAndIssueId(gitRepositoryId, issueId);

				// issue's type is not bug
				if (issueBug == null)
					continue;

				String commitId = commit.getCommitId();
				List<GitCommitParentKey> commitParentList = GitCommitParentDAO
						.selectByRepositoryIdAndCommitId(gitRepositoryId, commitId);
				String parentCommitId = commitParentList.get(0).getParentCommitId();
				Map<String, Integer> file2Loc = gitExtractor.getModifiedLineOfCode(commitId, parentCommitId);

				List<GitChangeFile> changeFileList = GitChangeFileDAO.selectByRepositoryIdAndCommitId(gitRepositoryId,
						commitId);
				List<GitChangeFile> filteredFileList = filterChangeFiles(changeFileList);

				List<BugFixFile> bugFixFileList = new ArrayList<BugFixFile>();
				for (GitChangeFile changeFile : filteredFileList) {
					String fileName = changeFile.getFileName();
					String shortName = FileUtils.parseFilePath(fileName, gitRepositoryName);
					int lineOfCode = file2Loc.get(fileName);
					Date fixDate = commit.getAuthoredDate();
					BugFixFile bugFixFile = new BugFixFile(gitRepositoryId, commitId, fileName, shortName, issueId,
							lineOfCode, fixDate);
					bugFixFileList.add(bugFixFile);
					BugFixFileDAO.insert(bugFixFile);
				}
				// BugFixFileDAO.insertBatch(bugFixFileList);
			}
		}
	}

	public GitRepository getRepository() {
		return gitRepository;
	}

	public void setRepository(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	private List<GitChangeFile> filterChangeFiles(List<GitChangeFile> changeFiles) {
		List<GitChangeFile> filteredFiles = new ArrayList<GitChangeFile>();
		for (GitChangeFile changeFile : changeFiles) {
			String fileName = changeFile.getFileName();
			String changeType = changeFile.getChangeType();
			// java file and non test java file
			if (changeType.equals("MODIFY") && fileName.endsWith(".java") && !fileName.contains("/test/")) {
				filteredFiles.add(changeFile);
			}
		}
		return filteredFiles;
	}
}