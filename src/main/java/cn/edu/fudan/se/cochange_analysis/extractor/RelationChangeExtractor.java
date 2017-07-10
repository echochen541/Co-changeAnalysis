package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationUnique;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;

public class RelationChangeExtractor {
	public static void run(int repoId) {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByFilePairCountNum(20, repoId);

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
			ChangeRelationCountDAO.insertChangeRelationCountMapper(changeRelationCount);
			Iterator<String> i = commitIds.iterator();
			while (i.hasNext()) {
				String commitId = (String) i.next();
				ChangeRelationCommit changeRelationCommit = new ChangeRelationCommit(0, repoId, commitId, tmp[0],
						tmp[1], tmp[2], tmp[3], tmp[4]);
				ChangeRelationCommitDAO.insertChangeRelationCommitMapper(changeRelationCommit);
			}
		}
	}

	public static void main(String args[]) {
		int[] repos = { 1, 2, 3, 4, 5, 6 };
		for (int repoId : repos) {
			run(repoId);
		}
	}

	public static void generateDSM(int repoId) {
		List<ChangeRelationCommit> changeRelationCommit = ChangeRelationCommitDAO.selectAllChangeRelationCommit(repoId);
		List<ChangeRelationCount> changeRelationCount = ChangeRelationCountDAO.selectAllChangeRelationCount(repoId);
	}
}
