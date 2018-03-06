package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.ParallelChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ParallelChangeRelationCommitDAO;

public class DataExtractor {
	private GitRepository repository;

	public DataExtractor() {
	}

	public DataExtractor(GitRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		DataExtractor dataExtractor = new DataExtractor(gitRepository);
		// dataExtractor.computeSpanTime(6);
		dataExtractor.computePCRDistribution(6);
		// dataExtractor.computePairPCR(6);
		gitRepository.setRepositoryId(2);
		// dataExtractor.computeSpanTime(6);
		dataExtractor.computePCRDistribution(6);
		// dataExtractor.computePairPCR(6);
		gitRepository.setRepositoryId(3);
		// dataExtractor.computeSpanTime(6);
		dataExtractor.computePCRDistribution(6);
		// dataExtractor.computePairPCR(6);
		gitRepository.setRepositoryId(4);
		// dataExtractor.computeSpanTime(6);
		dataExtractor.computePCRDistribution(6);
		// dataExtractor.computePairPCR(6);
		gitRepository.setRepositoryId(5);
		// dataExtractor.computeSpanTime(6);
		dataExtractor.computePCRDistribution(6);
		// dataExtractor.computePairPCR(6);
		gitRepository.setRepositoryId(6);
		// dataExtractor.computeSpanTime(6);
		dataExtractor.computePCRDistribution(6);
		// dataExtractor.computePairPCR(6);
	}

	public void computeSpanTime(int threshold) {
		int repositoryId = repository.getRepositoryId();
		// System.out.println(repositoryId);

		Map<String, Date> commitTimeMap = new HashMap<String, Date>();
		List<GitCommit> commitList = GitCommitDAO.selectFilteredByRepositoryId(repositoryId);
		for (GitCommit commit : commitList) {
			commitTimeMap.put(commit.getCommitId(), commit.getCommittedDate());
		}

		List<FilePairCount> fpcntList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold);
		// System.out.println("fpcntList size " + fpcntList.size());

		List<Double> numOfMonthsList = new ArrayList<Double>();

		for (FilePairCount fpcnt : fpcntList) {
			String filePair = fpcnt.getFilePair();

			// System.out.println(filePair);
			List<FilePairCommit> fpcList = FilePairCommitDAO.selectByRepositoryIdAndFilePair(repositoryId, filePair);
			// System.out.println("fpcList size " + fpcList.size());

			Date minDate = commitTimeMap.get(fpcList.get(0).getCommitId()), maxDate = minDate;
			for (FilePairCommit fpc : fpcList) {
				Date time = commitTimeMap.get(fpc.getCommitId());
				if (time.compareTo(minDate) < 0) {
					minDate = time;
				}
				if (time.compareTo(maxDate) > 0) {
					maxDate = time;
				}
			}

			double numOfMonths = getMonthNum(minDate, maxDate);

			numOfMonthsList.add(numOfMonths);
		}

		double sum = 0;
		for (double numOfMonths : numOfMonthsList) {
			sum += numOfMonths;
		}

		double size = numOfMonthsList.size();
		double mean = sum / size;
		double median = 0.0;
		if (size % 2 == 0) {
			median = (numOfMonthsList.get((int) (mean / 2)) + numOfMonthsList.get((int) (mean / 2) - 1)) / 2;
		} else {
			median = numOfMonthsList.get((int) (mean / 2));
		}

		// Collections.sort(numOfMonthsList);

		// System.out.println(numOfMonthsList);
		System.out.println("File Pair Size : " + numOfMonthsList.size());
		System.out.println("Mean : " + mean);
		System.out.println("Median : " + median);
		System.out.println();
	}

	public static double getMonthNum(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return (cal2.get(1) - cal1.get(1)) * 12 + (cal2.get(2) - cal1.get(2));
	}

	public void computePairCR(int threshold) {
		int repositoryId = repository.getRepositoryId();
		List<FilePairCount> fpcntList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold);
		Set<String> fpSet = new HashSet<String>();
		for (FilePairCount fpcnt : fpcntList) {
			fpSet.add(fpcnt.getFilePair());
		}

		List<ChangeRelationCount> crcntList = ChangeRelationCountDAO.selectDistinctByRepositoryId(repositoryId);
		int cnt = 0;
		for (ChangeRelationCount crcnt : crcntList) {
			String filePair = crcnt.getFilePair();
			if (fpSet.contains(filePair)) {
				cnt++;
			}
		}
		System.out.println(cnt);
		System.out.println();
	}

	public void computePairPCR(int threshold) {
		int repositoryId = repository.getRepositoryId();
		List<FilePairCount> fpcntList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold);
		Set<String> fpSet = new HashSet<String>();
		for (FilePairCount fpcnt : fpcntList) {
			fpSet.add(fpcnt.getFilePair());
		}

		List<ParallelChangeRelationCommit> pcrcList = ParallelChangeRelationCommitDAO
				.selectDistinctByRepositoryId(repositoryId);

		int cnt = 0;
		for (ParallelChangeRelationCommit pcrc : pcrcList) {
			String filePair = pcrc.getFilePair();
			if (fpSet.contains(filePair)) {
				cnt++;
			}
		}
		System.out.println(cnt);
		System.out.println();
	}

	public void computePCRDistribution(int threshold) {
		int repositoryId = repository.getRepositoryId();
		List<ParallelChangeRelationCommit> pcrcList = ParallelChangeRelationCommitDAO
				.selectByRepositoryId(repositoryId);
		System.out.println(pcrcList.size());

		List<FilePairCount> fpcntList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold);
		Set<String> fpSet = new HashSet<String>();
		for (FilePairCount fpcnt : fpcntList) {
			fpSet.add(fpcnt.getFilePair());
		}

		int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt = 0;
		for (ParallelChangeRelationCommit pcrc : pcrcList) {
			String filePair = pcrc.getFilePair();
			String changeType = pcrc.getChangeType1();

			if (!fpSet.contains(filePair)) {
				continue;
			}

			cnt++;

			if (changeType.equals("ADDITIONAL_OBJECT_STATE") || changeType.equals("REMOVED_OBJECT_STATE")
					|| changeType.equals("ATTRIBUTE_TYPE_CHANGE") || changeType.equals("ATTRIBUTE_RENAMING")) {
				cnt1++;
			} else if (changeType.equals("PARAMETER_INSERT") || changeType.equals("PARAMETER_DELETE")
					|| changeType.equals("PARAMETER_TYPE_CHANGE") || changeType.equals("PARAMETER_RENAMING")) {
				cnt2++;
			} else if (changeType.equals("RETURN_TYPE_INSERT") || changeType.equals("RETURN_TYPE_DELETE")
					|| changeType.equals("RETURN_TYPE_CHANGE")) {
				cnt3++;
			} else {
				cnt4++;
			}

		}
		System.out.println(cnt);
		System.out.println(cnt1 + " , " + cnt2 + " , " + cnt3 + " , " + cnt4);
		double sum = cnt1 + cnt2 + cnt3 + cnt4;
		System.out.println(cnt1/sum);
		System.out.println();
	}
}
