package cn.edu.fudan.se.cochange_analysis.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.csvreader.CsvWriter;

import cn.edu.fudan.se.cochange_analysis.detector.HotspotDetector;
import cn.edu.fudan.se.cochange_analysis.detector.HotspotModel;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationSum;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.GlobalRelationSum;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationSumDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.GlobalRelationSumDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.SnapshotFileDAO;

/**
 * @author echo
 *
 */
public class ChangeRelationExtractor {
	private GitRepository repository;

	public ChangeRelationExtractor() {
	}

	public ChangeRelationExtractor(GitRepository repository) {
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

		// for (int i = 1; i <= 6; i++) {
		// gitRepository.setRepositoryId(i);
		// ChangeRelationExtractor crDetector = new
		// ChangeRelationExtractor(gitRepository);
		// crDetector.computeAllRelationCoverage(gitRepository.getRepositoryId());
		// crDetector.computeTopRelationCoverage(gitRepository.getRepositoryId(),
		// 60);
		// }

		ChangeRelationExtractor crDetector = new ChangeRelationExtractor(gitRepository);
		// crDetector.detectGlobalTopN(20);
		crDetector.sumRelationsInAllTopN(60);

		// List<String> fileList = new ArrayList<>();
		// fileList.add("org/apache/cassandra/config/Config.java");
		// fileList.add("org/apache/cassandra/config/DatabaseDescriptor.java");
		// fileList.add("org/apache/cassandra/service/CassandraDaemon.java");
		// fileList.add("org/apache/cassandra/service/ReadCallback.java");
		// fileList.add("org/apache/cassandra/service/StorageProxyMBean.java");
		// fileList.add("org/apache/cassandra/utils/FBUtilities.java");

		// crDetector.displayRelations(2, 60, fileList);
	}

	private void sumRelationsInAllTopN(int topN) {
		Map<String, Integer> relationSumMap = new HashMap<String, Integer>();

		List<ChangeRelationSum> crsList1 = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, 1);

		for (ChangeRelationSum crs : crsList1) {
			String relationType = crs.getRelationType();
			int sum = crs.getSum();
			if (relationSumMap.containsKey(relationType)) {
				relationSumMap.put(relationType, relationSumMap.get(relationType) + sum);
			} else {
				relationSumMap.put(relationType, sum);
			}
		}

		List<ChangeRelationSum> crsList2 = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, 2);
		for (ChangeRelationSum crs : crsList2) {
			String relationType = crs.getRelationType();
			int sum = crs.getSum();
			if (relationSumMap.containsKey(relationType)) {
				relationSumMap.put(relationType, relationSumMap.get(relationType) + sum);
			} else {
				relationSumMap.put(relationType, sum);
			}
		}

		List<ChangeRelationSum> crsList3 = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, 3);
		for (ChangeRelationSum crs : crsList3) {
			String relationType = crs.getRelationType();
			int sum = crs.getSum();
			if (relationSumMap.containsKey(relationType)) {
				relationSumMap.put(relationType, relationSumMap.get(relationType) + sum);
			} else {
				relationSumMap.put(relationType, sum);
			}
		}

		List<ChangeRelationSum> crsList4 = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, 4);
		for (ChangeRelationSum crs : crsList4) {
			String relationType = crs.getRelationType();
			int sum = crs.getSum();
			if (relationSumMap.containsKey(relationType)) {
				relationSumMap.put(relationType, relationSumMap.get(relationType) + sum);
			} else {
				relationSumMap.put(relationType, sum);
			}
		}

		List<ChangeRelationSum> crsList5 = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, 5);
		for (ChangeRelationSum crs : crsList5) {
			String relationType = crs.getRelationType();
			int sum = crs.getSum();
			if (relationSumMap.containsKey(relationType)) {
				relationSumMap.put(relationType, relationSumMap.get(relationType) + sum);
			} else {
				relationSumMap.put(relationType, sum);
			}
		}

		List<ChangeRelationSum> crsList6 = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, 6);
		for (ChangeRelationSum crs : crsList6) {
			String relationType = crs.getRelationType();
			int sum = crs.getSum();
			if (relationSumMap.containsKey(relationType)) {
				relationSumMap.put(relationType, relationSumMap.get(relationType) + sum);
			} else {
				relationSumMap.put(relationType, sum);
			}
		}

		System.out.println(relationSumMap.size());
		System.out.println(relationSumMap);

		String csvFilePath = "C:/Users/echo/Desktop/relationSum.csv";
		try {
			// 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);
			CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("UTF-8"));
			// 写表头
			String[] csvHeaders = { "Relation", "Sum" };
			csvWriter.writeRecord(csvHeaders);
			// 写内容
			for (Entry<String, Integer> entry : relationSumMap.entrySet()) {
				String[] csvContent = { entry.getKey(), entry.getValue() + "" };
				csvWriter.writeRecord(csvContent);
			}
			csvWriter.close();
			System.out.println("--------CSV文件已经写入--------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void displayRelations(int repositoryId, int topN, List<String> fileList) {
		List<ChangeRelationSum> crsList = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, repositoryId);
		Set<String> relationSet = new HashSet<>();
		for (ChangeRelationSum crs : crsList) {
			relationSet.add(crs.getRelationType());
		}
		System.out.println(crsList);

		for (int i = 0; i < fileList.size() - 1; i++) {
			for (int j = i + 1; j < fileList.size(); j++) {
				System.out.println((char) (i - 0 + 'A') + "--" + (char) (j - 0 + 'A'));

				String fileName1 = fileList.get(i);
				String fileName2 = fileList.get(j);
				String filePair = "";
				if (fileName1.compareTo(fileName2) < 0) {
					filePair = fileName1 + "||" + fileName2;
				} else {
					filePair = fileName2 + "||" + fileName1;
				}
				List<ChangeRelationCount> crcList = ChangeRelationCountDAO.selectByRepositoryIdAndFilePair(repositoryId,
						filePair);
				for (ChangeRelationCount crc : crcList) {
					String changeType1 = crc.getChangeType1();
					String changeType2 = crc.getChangeType2();
					String changeType = "";
					if (changeType1.compareTo(changeType2) < 0) {
						changeType = changeType1 + "--" + changeType2;
					} else {
						changeType = changeType2 + "--" + changeType1;
					}
					// System.out.print(changeType);
					boolean exist = relationSet.contains(changeType);
					// if (exist) {
					// System.out.println(exist);
					System.out.print("[" + changeType1.toLowerCase() + ", " + changeType2.toLowerCase() + "]" + " : ");
					System.out.println(crc.getCount());
					// }
				}
				System.out.println();
			}
		}

	}

	private void detectGlobalTopN(int topN) {
		List<GlobalRelationSum> grsList = GlobalRelationSumDAO.selectByTopN(topN);
		// System.out.println(grsList);

		List<ChangeRelationSum> crsList1 = ChangeRelationSumDAO.selectTopNByRepositoryId(60, 1);
		Set<String> relationSet1 = new HashSet<>();
		for (ChangeRelationSum crs : crsList1) {
			relationSet1.add(crs.getRelationType());
		}

		List<ChangeRelationSum> crsList2 = ChangeRelationSumDAO.selectTopNByRepositoryId(60, 2);
		Set<String> relationSet2 = new HashSet<>();
		for (ChangeRelationSum crs : crsList2) {
			relationSet2.add(crs.getRelationType());
		}

		List<ChangeRelationSum> crsList3 = ChangeRelationSumDAO.selectTopNByRepositoryId(60, 3);
		Set<String> relationSet3 = new HashSet<>();
		for (ChangeRelationSum crs : crsList3) {
			relationSet3.add(crs.getRelationType());
		}

		List<ChangeRelationSum> crsList4 = ChangeRelationSumDAO.selectTopNByRepositoryId(60, 4);
		Set<String> relationSet4 = new HashSet<>();
		for (ChangeRelationSum crs : crsList4) {
			relationSet4.add(crs.getRelationType());
		}

		List<ChangeRelationSum> crsList5 = ChangeRelationSumDAO.selectTopNByRepositoryId(60, 5);
		Set<String> relationSet5 = new HashSet<>();
		for (ChangeRelationSum crs : crsList5) {
			relationSet5.add(crs.getRelationType());
		}

		List<ChangeRelationSum> crsList6 = ChangeRelationSumDAO.selectTopNByRepositoryId(60, 6);
		Set<String> relationSet6 = new HashSet<>();
		for (ChangeRelationSum crs : crsList6) {
			relationSet6.add(crs.getRelationType());
		}

		for (GlobalRelationSum grs : grsList) {
			String relationType = grs.getRelationType();
			System.out.print("[" + relationType.replace("--", ", ").toLowerCase() + "] ");
			int occurrence = 0;
			if (relationSet1.contains(relationType)) {
				occurrence++;
			}

			if (relationSet2.contains(relationType)) {
				occurrence++;
			}

			if (relationSet3.contains(relationType)) {
				occurrence++;
			}

			if (relationSet4.contains(relationType)) {
				occurrence++;
			}

			if (relationSet5.contains(relationType)) {
				occurrence++;
			}
			if (relationSet6.contains(relationType)) {
				occurrence++;
			}
			System.out.println(occurrence);
		}
	}

	private void computeTopRelationCoverage(Integer repositoryId, int topN) {
		List<String> snapshotFileList = SnapshotFileDAO.selectFileByRepositoryId(repositoryId);
		Set<String> snapshotFileSet = new HashSet<>(snapshotFileList);

		List<ChangeRelationSum> topRelationList = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, repositoryId);
		Set<String> topRelationSet = new HashSet<>();
		for (ChangeRelationSum crs : topRelationList) {
			topRelationSet.add(crs.getRelationType());
		}

		Set<String> allRelationFileSet = new HashSet<>();
		Set<String> topRelationFileSet = new HashSet<>();
		List<ChangeRelationCount> crcList = ChangeRelationCountDAO.selectByRepositoryId(repositoryId);

		for (ChangeRelationCount crc : crcList) {
			String filePair = crc.getFilePair();
			String fileName1 = filePair.split("\\|\\|")[0];
			String fileName2 = filePair.split("\\|\\|")[1];
			if (snapshotFileSet.contains(fileName1)) {
				allRelationFileSet.add(fileName1);
			}
			if (snapshotFileSet.contains(fileName2)) {
				allRelationFileSet.add(fileName2);
			}

			String changeType1 = crc.getChangeType1();
			String changeType2 = crc.getChangeType2();
			String relationType = "";

			if (changeType1.compareTo(changeType2) < 0) {
				relationType = changeType1 + "--" + changeType2;
			} else {
				relationType = changeType2 + "--" + changeType1;
			}

			if (topRelationSet.contains(relationType)) {
				if (snapshotFileSet.contains(fileName1)) {
					topRelationFileSet.add(fileName1);
				}
				if (snapshotFileSet.contains(fileName2)) {
					topRelationFileSet.add(fileName2);
				}
			}
		}

		System.out.println(repositoryId);
		System.out.println(snapshotFileSet.size());
		System.out.println(allRelationFileSet.size());
		System.out.println(topRelationFileSet.size());

		System.out.println(
				(int) ((double) topRelationFileSet.size() / (double) allRelationFileSet.size() * 10000) / 100.0 + "%");
		System.out.println();

	}

	private void computeAllRelationCoverage(int repositoryId) {
		List<String> snapshotFileList = SnapshotFileDAO.selectFileByRepositoryId(repositoryId);
		Set<String> snapshotFileSet = new HashSet<>(snapshotFileList);
		Set<String> allRelationFileSet = new HashSet<>();
		List<ChangeRelationCount> crcList = ChangeRelationCountDAO.selectByRepositoryId(repositoryId);

		for (ChangeRelationCount crc : crcList) {
			String filePair = crc.getFilePair();
			String fileName1 = filePair.split("\\|\\|")[0];
			String fileName2 = filePair.split("\\|\\|")[1];
			if (snapshotFileSet.contains(fileName1)) {
				allRelationFileSet.add(fileName1);
			}
			if (snapshotFileSet.contains(fileName2)) {
				allRelationFileSet.add(fileName2);
			}
		}
		System.out.println(repositoryId);
		System.out.println(snapshotFileSet.size());
		System.out.println(allRelationFileSet.size());
		System.out.println(
				(int) ((double) allRelationFileSet.size() / (double) snapshotFileSet.size() * 10000) / 100.0 + "%");
		System.out.println();
	}

	private void computeCoverage(int topN) {
		Set<String> totalRelationSet = new HashSet<String>();

		Set<String> relationSet1 = new HashSet<String>();
		Set<String> relationSet2 = new HashSet<String>();
		Set<String> relationSet3 = new HashSet<String>();
		Set<String> relationSet4 = new HashSet<String>();
		Set<String> relationSet5 = new HashSet<String>();
		Set<String> relationSet6 = new HashSet<String>();

		for (int i = 1; i <= 6; i++) {
			List<ChangeRelationSum> crsList = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, i);
			for (ChangeRelationSum crs : crsList) {
				String relationType = crs.getRelationType();
				totalRelationSet.add(relationType);

				if (i == 1) {
					relationSet1.add(relationType);
				} else if (i == 2) {
					relationSet2.add(relationType);
				} else if (i == 3) {
					relationSet3.add(relationType);
				} else if (i == 4) {
					relationSet4.add(relationType);
				} else if (i == 5) {
					relationSet5.add(relationType);
				} else {
					relationSet6.add(relationType);
				}
			}
		}

		List<Integer> occurenceList = new ArrayList<Integer>();

		Map<Integer, Integer> occurenceMap = new HashMap<>();
		occurenceMap.put(1, 0);
		occurenceMap.put(2, 0);
		occurenceMap.put(3, 0);
		occurenceMap.put(4, 0);
		occurenceMap.put(5, 0);
		occurenceMap.put(6, 0);

		for (String relationType : totalRelationSet) {
			int occurence = 0;
			if (relationSet1.contains(relationType)) {
				occurence++;
			}
			if (relationSet2.contains(relationType)) {
				occurence++;
			}
			if (relationSet3.contains(relationType)) {
				occurence++;
			}
			if (relationSet4.contains(relationType)) {
				occurence++;
			}
			if (relationSet5.contains(relationType)) {
				occurence++;
			}
			if (relationSet6.contains(relationType)) {
				occurence++;
			}
			occurenceMap.put(occurence, occurenceMap.get(occurence) + 1);

			if (occurence == 6) {
				System.out.println(relationType);
			}
		}

		System.out.println(occurenceMap);
	}

	// file pair co-change >= threshold1 times, change relation occur >=
	// threshold2 times
	public void extractChangeRelation(int threshold1, int threshold2) {
		int repositoryId = repository.getRepositoryId();
		System.out.println("repository id : " + repositoryId);
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold1);
		System.out.println("filePairCountList.size() : " + filePairCountList.size());

		List<ChangeRelationCommit> changeRelationCommits = new ArrayList<ChangeRelationCommit>();
		List<ChangeRelationCount> changeRelationCounts = new ArrayList<ChangeRelationCount>();

		for (FilePairCount filePairCountItem : filePairCountList) {
			String filePair = filePairCountItem.getFilePair();
			// System.out.println("filePair : " + filePair);
			List<FilePairCommit> filePairCommitList = FilePairCommitDAO.selectByRepositoryIdAndFilePair(repositoryId,
					filePair);
			// System.out.println("filePairCommitList.size() : " +
			// filePairCommitList.size());

			for (FilePairCommit filePairCommitItem : filePairCommitList) {
				String commitId = filePairCommitItem.getCommitId();
				String fileA = filePairCommitItem.getFileName1();
				String fileB = filePairCommitItem.getFileName2();

				List<String> fileAChangeTypeList = ChangeOperationDAO
						.selectUniqueChangeTypesByCommitIdAndFileName(commitId, fileA);
				List<String> fileBChangeTypeList = ChangeOperationDAO
						.selectUniqueChangeTypesByCommitIdAndFileName(commitId, fileB);

				for (String aChangeType : fileAChangeTypeList) {
					for (String bChangeType : fileBChangeTypeList) {
						String key = filePair + "--" + aChangeType + "--" + bChangeType;
						if (result.containsKey(key)) {
							List<String> keySet = result.get(key);
							keySet.add(commitId);
						} else {
							List<String> newList = new ArrayList<String>();
							newList.add(commitId);
							result.put(key, newList);
						}
					}
				}
			}

			for (Entry<String, List<String>> entry : result.entrySet()) {
				List<String> commitIds = entry.getValue();
				int size = commitIds.size();
				// if change relation occurs >= threshold2 times
				if (size >= threshold2) {
					String[] tmp = entry.getKey().split("--");
					ChangeRelationCount changeRelationCount = new ChangeRelationCount(0, repositoryId, tmp[0], tmp[1],
							tmp[2], size);
					changeRelationCounts.add(changeRelationCount);

					if (changeRelationCounts.size() >= 100) {
						ChangeRelationCountDAO.insertBatch(changeRelationCounts);
						changeRelationCounts.clear();
					}

					Iterator<String> i = commitIds.iterator();
					while (i.hasNext()) {
						String commitId = (String) i.next();
						ChangeRelationCommit changeRelationCommit = new ChangeRelationCommit(0, repositoryId, commitId,
								tmp[0], tmp[1], tmp[2]);
						changeRelationCommits.add(changeRelationCommit);

						if (changeRelationCommits.size() >= 100) {
							ChangeRelationCommitDAO.insertBatch(changeRelationCommits);
							changeRelationCommits.clear();
						}
					}
				}
			}
			result.clear();
		}
		if (!changeRelationCounts.isEmpty()) {
			ChangeRelationCountDAO.insertBatch(changeRelationCounts);
		}
		if (!changeRelationCommits.isEmpty()) {
			ChangeRelationCommitDAO.insertBatch(changeRelationCommits);
		}
	}

	public void sumChangeRelation(String outputdir) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		int repositoryId = repository.getRepositoryId();
		List<ChangeRelationCount> changeRelationCountList = ChangeRelationCountDAO.selectByRepositoryId(repositoryId);

		// System.out.println(changeRelationCountList.size());
		for (ChangeRelationCount changeRelationCount : changeRelationCountList) {
			String changeType1 = changeRelationCount.getChangeType1();
			String changeType2 = changeRelationCount.getChangeType2();
			String changeRelation = null;
			if (changeType1.compareTo(changeType2) <= 0)
				changeRelation = changeType1 + "--" + changeType2;
			else
				changeRelation = changeType2 + "--" + changeType1;

			if (result.containsKey(changeRelation)) {
				result.put(changeRelation, result.get(changeRelation) + changeRelationCount.getCount());
			} else {
				result.put(changeRelation, changeRelationCount.getCount());
			}
		}

		List<Entry<String, Integer>> sumList = new ArrayList<Entry<String, Integer>>(result.entrySet());

		int line = sumList.size();
		try {
			String outputFileName = outputdir + "/" + this.repository.getRepositoryName() + "_change-relation-sum.csv";
			System.out.println(outputFileName);
			File f = new File(outputFileName);
			if (!f.exists())
				f.createNewFile();

			FileOutputStream fos = new FileOutputStream(f);

			String header = "repository_id,relation_type,sum\n";
			fos.write(header.getBytes());
			for (int i = 0; i < line; i++) {
				Entry<String, Integer> tmp = sumList.get(i);
				String byteS = repositoryId + "," + tmp.getKey() + "," + tmp.getValue() + ",\n";
				fos.write(byteS.getBytes());
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
