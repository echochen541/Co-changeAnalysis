package cn.edu.fudan.se.cochange_analysis.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationUnique;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationUnique;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;

public class RelationChangeExtractor {
	private GitRepository repository;
	
	public GitRepository getRepository() {
		return repository;
	}

	public void setRepository(GitRepository repository) {
		this.repository = repository;
	}
	

	public RelationChangeExtractor(GitRepository repository) {
		super();
		this.repository = repository;
	}

	public void run(int repoId, int threshold) {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByFilePairCountNum(threshold, repoId);

		for (FilePairCount filePairCountItem : filePairCountList) {
			List<FilePairCommit> filePairCommitList = FilePairCommitDAO
					.selectByFilePairName(filePairCountItem.getFilePair(), repoId);
			for (FilePairCommit filePairCommitItem : filePairCommitList) {
				String commitId = filePairCommitItem.getCommitId();
				String filePair = filePairCommitItem.getFilePair();
				String fileA = filePairCommitItem.getFileName1();
				String fileB = filePairCommitItem.getFileName2();
				List<ChangeOperationUnique> fileAChangeOperationList = ChangeOperationDAO
						.selectChangeOperationsByFileNameAndCommitId(fileA, commitId);
				List<ChangeOperationUnique> fileBChangeOperationList = ChangeOperationDAO
						.selectChangeOperationsByFileNameAndCommitId(fileB, commitId);
				for (ChangeOperationUnique fileAChangeOperationItem : fileAChangeOperationList) {
					String aChangeType = fileAChangeOperationItem.getChangeType();
					String aChangedEntityType = fileAChangeOperationItem.getChangedEntityType();
					for (ChangeOperationUnique fileBChangeOperationItem : fileBChangeOperationList) {
						String bChangeType = fileBChangeOperationItem.getChangeType();
						String bChangedEntityType = fileBChangeOperationItem.getChangedEntityType();
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
					}
				}
			}
		}
		for (Map.Entry<String, Set<String>> entry : result.entrySet()) {
			String[] tmp = entry.getKey().split("--");
			Set<String> commitIds = entry.getValue();
			int size = commitIds.size();
			ChangeRelationCount changeRelationCount = new ChangeRelationCount(0, repoId, tmp[0], tmp[1], tmp[2], tmp[3],
					tmp[4], size);
			ChangeRelationCountDAO.insertChangeRelationCount(changeRelationCount);

			List<ChangeRelationCommit> changeRelationCommits = new ArrayList<ChangeRelationCommit>();
			Iterator<String> i = commitIds.iterator();
			while (i.hasNext()) {
				String commitId = (String) i.next();
				ChangeRelationCommit changeRelationCommit = new ChangeRelationCommit(0, repoId, commitId, tmp[0],
						tmp[1], tmp[2], tmp[3], tmp[4]);
				// ChangeRelationCommitDAO.insertChangeRelationCommit(changeRelationCommit);
				changeRelationCommits.add(changeRelationCommit);
			}
			ChangeRelationCommitDAO.insertBatch(changeRelationCommits);
		}
	}

	public void generateRelationCountSummary(int repoId,int threshold){
		List<ChangeRelationUnique> changeRelationUniqueList = ChangeRelationCountDAO.selectDistinctChangeType(repoId,
				threshold);
		Map<String,Integer> result=new HashMap<String,Integer>();
		for(ChangeRelationUnique changeRelationUniqueItem:changeRelationUniqueList){
			result.put(changeRelationUniqueItem.getChangeType1()+"||"
					+changeRelationUniqueItem.getChangedEntityType1()+"||"
					+changeRelationUniqueItem.getChangeType2()+"||"
					+changeRelationUniqueItem.getChangedEntityType2(), 0);
		}
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByFilePairCountNum(threshold, repoId);
		for(FilePairCount filePairCountItem:filePairCountList){
			List<ChangeRelationCount> changeRelationCount = ChangeRelationCountDAO.selectAllChangeRelationCount(repoId,
					filePairCountItem.getFilePair(), threshold);
			
		}
		
	}

	public void generateDSM(int repoId, int threshold, int threshold2) {
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByFilePairCountNum(threshold, repoId);
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
		List<ChangeRelationUnique> changeRelationUniqueList = ChangeRelationCountDAO.selectDistinctChangeType(repoId,
				threshold2);
		StringBuilder typeBuilder = new StringBuilder();
		typeBuilder.append("[");
		for (ChangeRelationUnique changeRelationUniqueItem : changeRelationUniqueList) {
			if (changeRelationUniqueItem.getChangeType1().contains("JAVADOC")
					|| changeRelationUniqueItem.getChangeType2().contains("JAVADOC")
					|| changeRelationUniqueItem.getChangeType1().contains("COMMENT")
					|| changeRelationUniqueItem.getChangeType2().contains("COMMENT")) {
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
			List<ChangeRelationCount> changeRelationCount = ChangeRelationCountDAO.selectAllChangeRelationCount(repoId,
					myFileName.getFilePair(), threshold2);
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
			FileOutputStream fos = new FileOutputStream(new File("D://" + repoId + ".dsm"));
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
}
