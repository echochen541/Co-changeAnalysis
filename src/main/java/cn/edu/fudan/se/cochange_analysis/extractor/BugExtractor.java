package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
import cn.edu.fudan.se.cochange_analysis.git.bean.AccumutiveBug;
import cn.edu.fudan.se.cochange_analysis.git.bean.BugFixFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParentKey;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.IssueBug;
import cn.edu.fudan.se.cochange_analysis.git.dao.AccumutiveBugDAO;
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
					// BugFixFileDAO.insert(bugFixFile);
				}
				BugFixFileDAO.insertBatch(bugFixFileList);
			}
		}
	}

	public double[] computeAverageBFAndBC(List<String> fileList) {
		// System.out.println("fileList.size() " + fileList.size());
		double[] result = new double[2];
		double avgBugFrequency = 0.0;
		double avgBugChurn = 0.0;
		double sumBugFrequency = 0.0;
		double sumBugChurn = 0.0;

		List<AccumutiveBug> accumutiveBugList = AccumutiveBugDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		Map<String, Integer> bugFrequencyMap = new HashMap<String, Integer>();
		Map<String, Integer> bugChurnMap = new HashMap<String, Integer>();

		for (AccumutiveBug accumutiveBug : accumutiveBugList) {
			bugFrequencyMap.put(accumutiveBug.getFileName(), accumutiveBug.getBugFrequency());
			bugChurnMap.put(accumutiveBug.getFileName(), accumutiveBug.getBugChurn());
		}

		for (String fileName : fileList) {
			if (bugFrequencyMap.containsKey(fileName)) {
				sumBugFrequency += bugFrequencyMap.get(fileName);
				sumBugChurn += bugChurnMap.get(fileName);
			}
		}

		avgBugFrequency = sumBugFrequency / fileList.size();
		avgBugChurn = sumBugChurn / fileList.size();
		result[0] = avgBugFrequency;
		result[1] = avgBugChurn;
		// System.out.println("sumBugFrequency " + sumBugFrequency);
		// System.out.println("sumBugChurn " + sumBugChurn);
		System.out.println("avgBugFrequency " + avgBugFrequency);
		System.out.println("avgBugChurn " + avgBugChurn);
		System.out.println();
		return result;
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
			if (changeType.equals("MODIFY") && fileName.endsWith(".java") && !fileName.contains("/test/")
					&& !fileName.contains("/tester/")) {
				filteredFiles.add(changeFile);
			}
		}
		return filteredFiles;
	}
}
