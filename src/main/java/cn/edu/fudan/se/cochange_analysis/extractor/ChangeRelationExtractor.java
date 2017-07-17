package cn.edu.fudan.se.cochange_analysis.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationUnique;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.UniqueChangeOperation;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;

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

	// file pair co-change >= threshold1 times, change relation occur >=
	// threshold2 times
	public void extractChangeRelation(int threshold1, int threshold2) {
		int repositoryId = repository.getRepositoryId();
		// System.out.println("repository id : " + repositoryId);
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId,
				threshold1);
		// System.out.println(filePairCountList.size());

		for (FilePairCount filePairCountItem : filePairCountList) {
			String filePair = filePairCountItem.getFilePair();
			// System.out.println(repositoryId + " : " + filePair);
			List<FilePairCommit> filePairCommitList = FilePairCommitDAO.selectByRepositoryIdAndFilePair(repositoryId,
					filePair);
			// System.out.println(filePairCommitList.size());
			for (FilePairCommit filePairCommitItem : filePairCommitList) {
				String commitId = filePairCommitItem.getCommitId();
				String fileA = filePairCommitItem.getFileName1();
				String fileB = filePairCommitItem.getFileName2();

				List<UniqueChangeOperation> fileAChangeOperationList = ChangeOperationDAO
						.selectUniqueChangeOperationsByCommitIdAndFileName(commitId, fileA);
				List<UniqueChangeOperation> fileBChangeOperationList = ChangeOperationDAO
						.selectUniqueChangeOperationsByCommitIdAndFileName(commitId, fileB);

				// System.out.println("fileAChangeOperationList " +
				// fileAChangeOperationList.size());
				// System.out.println("fileBChangeOperationList " +
				// fileBChangeOperationList.size());

				for (UniqueChangeOperation fileAChangeOperationItem : fileAChangeOperationList) {
					String aChangeType = fileAChangeOperationItem.getChangeType();
					String aChangedEntityType = fileAChangeOperationItem.getChangedEntityType();

					if (aChangedEntityType.contains("JAVADOC") || aChangedEntityType.contains("COMMENT"))
						continue;

					for (UniqueChangeOperation fileBChangeOperationItem : fileBChangeOperationList) {
						String bChangeType = fileBChangeOperationItem.getChangeType();
						String bChangedEntityType = fileBChangeOperationItem.getChangedEntityType();

						if (bChangedEntityType.contains("JAVADOC") || bChangedEntityType.contains("COMMENT"))
							continue;

						String key = filePair + "--" + aChangeType + "--" + aChangedEntityType + "--" + bChangeType
								+ "--" + bChangedEntityType;
						if (result.containsKey(key)) {
							Set<String> keySet = result.get(key);
							keySet.add(commitId);
						} else {
							Set<String> newSet = new HashSet<String>();
							newSet.add(commitId);
							result.put(key, newSet);
						}
						// System.out.println(result);
					}
				}
			}
			// System.out.println(result.size());

			for (Map.Entry<String, Set<String>> entry : result.entrySet()) {
				String[] tmp = entry.getKey().split("--");
				Set<String> commitIds = entry.getValue();
				int size = commitIds.size();
				// if change relation occurs >= threshold2 times
				if (size >= threshold2) {
					ChangeRelationCount changeRelationCount = new ChangeRelationCount(0, repositoryId, tmp[0], tmp[1],
							tmp[2], tmp[3], tmp[4], size);
					ChangeRelationCountDAO.insertChangeRelationCount(changeRelationCount);

					List<ChangeRelationCommit> changeRelationCommits = new ArrayList<ChangeRelationCommit>();
					Iterator<String> i = commitIds.iterator();
					while (i.hasNext()) {
						String commitId = (String) i.next();
						ChangeRelationCommit changeRelationCommit = new ChangeRelationCommit(0, repositoryId, commitId,
								tmp[0], tmp[1], tmp[2], tmp[3], tmp[4]);
						changeRelationCommits.add(changeRelationCommit);
					}
					ChangeRelationCommitDAO.insertBatch(changeRelationCommits);
				}
			}
			result.clear();
		}
	}

	public void rankChangeRelationCount(String outputdir) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		int repositoryId = repository.getRepositoryId();
		List<ChangeRelationCount> changeRelationCountList = ChangeRelationCountDAO.selectByRepositoryId(repositoryId);

		// System.out.println(changeRelationCountList.size());
		for (ChangeRelationCount changeRelationCount : changeRelationCountList) {
			String change1 = changeRelationCount.getChangeType1() + "(" + changeRelationCount.getChangedEntityType1()
					+ ")";
			String change2 = changeRelationCount.getChangeType2() + "(" + changeRelationCount.getChangedEntityType2()
					+ ")";
			String changeRelation = null;
			if (change1.compareTo(change2) <= 0)
				changeRelation = change1 + "--" + change2;
			else
				changeRelation = change2 + "--" + change1;

			if (result.containsKey(changeRelation)) {
				result.put(changeRelation, result.get(changeRelation) + changeRelationCount.getCount());
			} else {
				result.put(changeRelation, changeRelationCount.getCount());
			}
		}

		List<Entry<String, Integer>> rankList = new ArrayList<Entry<String, Integer>>(result.entrySet());
		Collections.sort(rankList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});

		int line = rankList.size();
		try {
			String outputFileName = outputdir + "/" + this.repository.getRepositoryName()
					+ "_change-relation-count-rank.csv";
			System.out.println(outputFileName);
			File f = new File(outputFileName);
			if (!f.exists())
				f.createNewFile();

			FileOutputStream fos = new FileOutputStream(f);
			for (int i = 0; i < line; i++) {
				Entry<String, Integer> tmp = rankList.get(i);
				String byteS = tmp.getKey() + "," + tmp.getValue() + ",\n";
				fos.write(byteS.getBytes());
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateDSM(int threshold1, int threshold2) {
		int repositoryId = repository.getRepositoryId();
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId,
				threshold1);
		List<String> fileList = new ArrayList<String>();
		Set<String> fileSet = new HashSet<String>();
		for (FilePairCount filePairItem : filePairCountList) {
			String[] filePairs = filePairItem.getFilePair().split("\\|\\|");
			fileSet.add(filePairs[0]);
			fileSet.add(filePairs[1]);
		}
		Iterator<String> i = fileSet.iterator();
		while (i.hasNext()) {
			fileList.add(i.next());
		}
		Collections.sort(fileList);
		Map<String, Integer> fileIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < fileList.size(); index++) {
			fileIndexMap.put(fileList.get(index), index);
		}
		System.out.println("FileSize:" + fileList.size());

		StringBuilder[][] dsmMatrix = new StringBuilder[fileList.size()][fileList.size()];

		Map<String, Integer> typeIndexMap = new HashMap<String, Integer>();
		List<String> typeList = new ArrayList<String>();
		List<ChangeRelationUnique> changeRelationUniqueList = ChangeRelationCountDAO
				.selectDistinctChangeType(repositoryId, threshold2);
		StringBuilder typeBuilder = new StringBuilder();
		typeBuilder.append("[");
		for (ChangeRelationUnique changeRelationUniqueItem : changeRelationUniqueList) {
			if (changeRelationUniqueItem.getChangedEntityType1().contains("JAVADOC")
					|| changeRelationUniqueItem.getChangedEntityType2().contains("JAVADOC")
					|| changeRelationUniqueItem.getChangedEntityType1().contains("COMMENT")
					|| changeRelationUniqueItem.getChangedEntityType2().contains("COMMENT")) {
				continue;
			}
			String tmp = changeRelationUniqueItem.getChangeType1() + "||"
					+ changeRelationUniqueItem.getChangedEntityType1() + "||"
					+ changeRelationUniqueItem.getChangeType2() + "||"
					+ changeRelationUniqueItem.getChangedEntityType2();

			typeList.add(tmp);

		}
		Collections.sort(typeList);
		for (int m = 0; m < typeList.size(); m++) {
			typeIndexMap.put(typeList.get(m), m);
			typeBuilder.append(typeList.get(m) + ",");
		}
		System.out.println("TypeSize:" + typeList.size());
		typeBuilder.deleteCharAt(typeBuilder.length() - 1);
		typeBuilder.append("]\n");
		typeBuilder.append(fileList.size());
		typeBuilder.append("\n");
		StringBuilder matrixCell = new StringBuilder();
		for (int j = 0; j < typeList.size(); j++) {
			matrixCell.append("0");
		}
		for (FilePairCount myFileName : filePairCountList) {
			List<ChangeRelationCount> changeRelationCount = ChangeRelationCountDAO
					.selectAllChangeRelationCount(repositoryId, myFileName.getFilePair(), threshold2);
			for (ChangeRelationCount changeRelationCountItem : changeRelationCount) {
				String filePairName = changeRelationCountItem.getFilePair();
				String changeType1 = changeRelationCountItem.getChangeType1();
				String changeType2 = changeRelationCountItem.getChangeType2();
				String changedEntityType1 = changeRelationCountItem.getChangedEntityType1();
				String changedEntityType2 = changeRelationCountItem.getChangedEntityType2();
				if (changedEntityType1.contains("JAVADOC") || changedEntityType2.contains("JAVADOC")
						|| changedEntityType1.contains("COMMENT") || changedEntityType2.contains("COMMENT")) {
					continue;
				}
				String relationType = changeType1 + "||" + changedEntityType1 + "||" + changeType2 + "||"
						+ changedEntityType2;
				String[] filePairs = filePairName.split("\\|\\|");
				if (dsmMatrix[fileIndexMap.get(filePairs[0])][fileIndexMap.get(filePairs[1])] == null) {
					dsmMatrix[fileIndexMap.get(filePairs[0])][fileIndexMap.get(filePairs[1])] = new StringBuilder(
							matrixCell.toString());
					;
				}
				dsmMatrix[fileIndexMap.get(filePairs[0])][fileIndexMap.get(filePairs[1])]
						.setCharAt(typeIndexMap.get(relationType), '1');
			}
		}
		StringBuilder matrixBuilder = new StringBuilder();
		for (int m = 0; m < dsmMatrix.length; m++) {
			for (int n = 0; n < dsmMatrix[0].length; n++) {
				if (dsmMatrix[m][n] == null) {
					matrixBuilder.append("0 ");
				} else {
					matrixBuilder.append(dsmMatrix[m][n].toString() + " ");
				}
			}
			matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
			matrixBuilder.append("\n");
		}
		StringBuilder fileListBuilder = new StringBuilder();
		for (String fileName : fileList) {
			fileListBuilder.append(fileName + "\n");
		}

		try {
			FileOutputStream fos = new FileOutputStream(
					new File("D://" + repositoryId + "_" + threshold1 + "_" + threshold2 + ".dsm"));
			fos.write(typeBuilder.toString().getBytes());
			fos.write(matrixBuilder.toString().getBytes());
			fos.write(fileListBuilder.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateRelationCountSummary(int threshold1, int threshold2, int threshold3) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<FilePairCount> filePairCountList = FilePairCountDAO
				.selectByRepositoryIdAndCount(repository.getRepositoryId(), threshold1);
		for (FilePairCount filePairCountItem : filePairCountList) {
			List<ChangeRelationCount> changeRelationCount = ChangeRelationCountDAO.selectAllChangeRelationCount(
					repository.getRepositoryId(), filePairCountItem.getFilePair(), threshold2);
			for (ChangeRelationCount item : changeRelationCount) {
				if (item.getChangedEntityType1().contains("JAVADOC") || item.getChangedEntityType2().contains("JAVADOC")
						|| item.getChangedEntityType1().contains("COMMENT")
						|| item.getChangedEntityType2().contains("COMMENT")) {
					continue;
				}
				String item1Str = item.getChangeType1() + "||" + item.getChangedEntityType1();
				String item2Str = item.getChangeType2() + "||" + item.getChangedEntityType2();
				String tmp = null;
				if (item1Str.compareTo(item2Str) <= 0) {
					tmp = item1Str + "||" + item2Str;
				} else {
					tmp = item2Str + "||" + item2Str;
				}

				if (result.containsKey(tmp)) {
					result.put(tmp, result.get(tmp) + item.getCount());
				} else {
					result.put(tmp, item.getCount());
				}
			}
		}
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(result.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});

		int bound = list.size();
		try {
			FileOutputStream fos = new FileOutputStream(new File(
					"D://" + this.repository.getRepositoryName() + "_" + threshold1 + "_" + threshold2 + ".csv"));
			for (int i = 0; i < bound; i++) {
				Entry<String, Integer> tmp = list.get(i);
				String byteS = tmp.getKey() + "," + tmp.getValue() + ",\n";
				fos.write(byteS.getBytes());
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// next step
		// if (list.size() > 32) {
		// bound = 32;
		// }
		// Map<String, Integer> top32TypeMap = new HashMap<String, Integer>();

		// for (int i = 0; i < bound; i++) {
		// Entry<String, Integer> tmp = list.get(i);
		// top32TypeMap.put(tmp.getKey(), tmp.getValue());
		// }
		// this.generateTop32TypeDSM(threshold1, threshold2, top32TypeMap);

		// select top N
		if (list.size() > threshold3) {
			bound = threshold3;
		}

		Map<String, Integer> topNTypeMap = new HashMap<String, Integer>();

		for (int i = 0; i < bound; i++) {
			Entry<String, Integer> tmp = list.get(i);
			topNTypeMap.put(tmp.getKey(), tmp.getValue());
		}
		this.generateTopNTypeDSM(threshold1, threshold2, topNTypeMap);
	}

	public void generateTopNTypeDSM(int threshold1, int threshold2, Map<String, Integer> typeMap) {
		int repositoryId = repository.getRepositoryId();
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId,
				threshold1);
		List<String> fileList = new ArrayList<String>();
		Set<String> fileSet = new HashSet<String>();
		for (FilePairCount filePairItem : filePairCountList) {
			String[] filePairs = filePairItem.getFilePair().split("\\|\\|");
			fileSet.add(filePairs[0]);
			fileSet.add(filePairs[1]);
		}
		Iterator<String> i = fileSet.iterator();
		while (i.hasNext()) {
			fileList.add(i.next());
		}
		Collections.sort(fileList);
		Map<String, Integer> fileIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < fileList.size(); index++) {
			fileIndexMap.put(fileList.get(index), index);
		}
		System.out.println("FileSize:" + fileList.size());

		StringBuilder[][] dsmMatrix = new StringBuilder[fileList.size()][fileList.size()];
		Map<String, Integer> typeIndexMap = new HashMap<String, Integer>();
		List<String> typeList = new ArrayList<String>();
		for (String tmp : typeMap.keySet()) {
			typeList.add(tmp);
		}
		Collections.sort(typeList);
		StringBuilder typeBuilder = new StringBuilder();
		typeBuilder.append("[");
		for (int m = 0; m < typeList.size(); m++) {
			typeIndexMap.put(typeList.get(m), m);
			typeBuilder.append(typeList.get(m) + ", ");
		}
		System.out.println("TypeSize:" + typeList.size());
		typeBuilder.deleteCharAt(typeBuilder.length() - 1);
		typeBuilder.append("]\n");
		typeBuilder.append(fileList.size());
		typeBuilder.append("\n");
		StringBuilder matrixCell = new StringBuilder();
		for (int j = 0; j < typeList.size(); j++) {
			matrixCell.append("0");

		}
		for (FilePairCount myFileName : filePairCountList) {
			List<ChangeRelationCount> changeRelationCount = ChangeRelationCountDAO
					.selectAllChangeRelationCount(repositoryId, myFileName.getFilePair(), threshold2);
			for (ChangeRelationCount changeRelationCountItem : changeRelationCount) {
				String filePairName = changeRelationCountItem.getFilePair();
				String changeType1 = changeRelationCountItem.getChangeType1();
				String changeType2 = changeRelationCountItem.getChangeType2();
				String changedEntityType1 = changeRelationCountItem.getChangedEntityType1();
				String changedEntityType2 = changeRelationCountItem.getChangedEntityType2();

				String change1 = changeType1 + "||" + changedEntityType1;
				String change2 = changeType2 + "||" + changedEntityType2;

				int compareResult = change1.compareTo(change2);

				String relationType = null;
				if (compareResult <= 0)
					relationType = change1 + change2;
				else
					relationType = change2 + change1;

				String[] filePairs = filePairName.split("\\|\\|");

				if (!typeMap.containsKey(relationType))
					continue;

				// (i, j) or (j, i)
				if (compareResult <= 0) {
					if (dsmMatrix[fileIndexMap.get(filePairs[0])][fileIndexMap.get(filePairs[1])] == null) {
						StringBuilder sb = new StringBuilder(matrixCell.toString());
						dsmMatrix[fileIndexMap.get(filePairs[0])][fileIndexMap.get(filePairs[1])] = sb;
					}
					dsmMatrix[fileIndexMap.get(filePairs[0])][fileIndexMap.get(filePairs[1])]
							.setCharAt(typeIndexMap.get(relationType), '1');
				} else {
					if (dsmMatrix[fileIndexMap.get(filePairs[1])][fileIndexMap.get(filePairs[0])] == null) {
						StringBuilder sb = new StringBuilder(matrixCell.toString());
						dsmMatrix[fileIndexMap.get(filePairs[1])][fileIndexMap.get(filePairs[0])] = sb;
					}
					dsmMatrix[fileIndexMap.get(filePairs[1])][fileIndexMap.get(filePairs[0])]
							.setCharAt(typeIndexMap.get(relationType), '1');
				}
			}
		}
		StringBuilder matrixBuilder = new StringBuilder();
		for (int m = 0; m < dsmMatrix.length; m++) {
			for (int n = 0; n < dsmMatrix[0].length; n++) {
				if (dsmMatrix[m][n] == null) {
					matrixBuilder.append("0 ");
				} else {
					matrixBuilder.append(dsmMatrix[m][n].toString() + " ");
				}
			}
			matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
			matrixBuilder.append("\n");
		}
		StringBuilder fileListBuilder = new StringBuilder();
		for (String fileName : fileList) {
			fileListBuilder.append(fileName + "\n");
		}

		try {
			FileOutputStream fos = new FileOutputStream(
					new File("D://" + repository.getRepositoryName() + "_" + threshold1 + "_" + threshold2 + ".dsm"));
			fos.write(typeBuilder.toString().getBytes());
			fos.write(matrixBuilder.toString().getBytes());
			fos.write(fileListBuilder.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {

	}
}
