package cn.edu.fudan.se.cochange_analysis.detector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationSum;
import cn.edu.fudan.se.cochange_analysis.git.bean.ClusterAncestor;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.OriginCluster;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationSumDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ClusterAncestorDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.OriginClusterDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.SnapshotFileDAO;

public class OriginClusterToDB {
	private GitRepository gitRepository;
	static int globalClusterId = 0;
	static List<OriginCluster> clusterDBList = new ArrayList<OriginCluster>();
	static List<ClusterAncestor> clusterAncestorDBList = new ArrayList<ClusterAncestor>();

	public OriginClusterToDB() {
	}

	public OriginClusterToDB(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		for (int i = 1; i <= 6; i++) {
			gitRepository.setRepositoryId(i);
			OriginClusterToDB oc2DB = new OriginClusterToDB(gitRepository);
			oc2DB.storeCluster(3, 60);
		}
	}

	private static void computeMetrics(List<HotspotModel> hotspots) {
		if (hotspots == null)
			return;

		int minSize = 1000;
		int maxSize = 0;
		double sum = 0;
		double mean = 0;
		int median = 0;
		List<Integer> sizeList = new ArrayList<Integer>();

		for (HotspotModel hotspot : hotspots) {
			int size = hotspot.getSize();
			minSize = Math.min(size, minSize);
			maxSize = Math.max(size, maxSize);
			sum += size;
			sizeList.add(size);
		}

		int numOfHotspots = hotspots.size();

		mean = sum / (double) numOfHotspots;

		Collections.sort(sizeList);

		if (numOfHotspots % 2 == 0) {
			median = (sizeList.get(numOfHotspots / 2) + sizeList.get(numOfHotspots / 2 - 1)) / 2;
		} else {
			median = sizeList.get(numOfHotspots / 2);
		}
		System.out.println("max: " + maxSize);
		System.out.println("min: " + minSize);
		System.out.println("mean: " + mean);
		System.out.println("median: " + median);
		System.out.println();
	}

	public void storeCluster(int threshold1, int threshold2) {
		globalClusterId = 1;
		clusterDBList.clear();
		clusterAncestorDBList.clear();

		String[] clusterNames = null;
		double[][] distances = null;
		Map<String, Integer> fileIndexMap = new HashMap<String, Integer>();
		Map<String, Integer> relationIndexMap = new HashMap<String, Integer>();
		int repositoryId = gitRepository.getRepositoryId();

		// get all co-change pairs co-change time >= threshold1 and file in
		// snapshot
		String release = "";
		if (repositoryId == 1) {
			release = "camel-2.19.1";
		}
		if (repositoryId == 2) {
			release = "cassandra-3.11.0";
		}
		if (repositoryId == 3) {
			release = "cxf-3.1.11";
		}
		if (repositoryId == 4) {
			release = "YARN-5355-branch-2-2017-04-25";
		}
		if (repositoryId == 5) {
			release = "release-0.18.0";
		}
		if (repositoryId == 6) {
			release = "wicket_1_2_b2_before_charsequence";
		}

		List<String> snapshotFileList = SnapshotFileDAO
				.selectFileByRepositoryIdAndRelease(gitRepository.getRepositoryId(), release);
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold1);
		List<String> fileList = getFileInSnapshot(snapshotFileList, filePairCountList);
		clusterNames = new String[fileList.size()];

		for (int index = 0; index < fileList.size(); index++) {
			fileIndexMap.put(fileList.get(index), index);
			clusterNames[index] = fileList.get(index);
		}

		// get top threshold2 co-change relations
		List<ChangeRelationSum> relationSumList = ChangeRelationSumDAO.selectTopNByRepositoryId(threshold2,
				repositoryId);
		for (int i = 0; i < relationSumList.size(); i++) {
			relationIndexMap.put(relationSumList.get(i).getRelationType(), i);
		}

		// input distances matrix, clusterNames
		distances = new double[fileList.size()][fileList.size()];

		for (FilePairCount filePairCount : filePairCountList) {
			String filePair = filePairCount.getFilePair();
			String[] tokens = filePair.split("\\|\\|");
			String fileName1 = tokens[0];
			String fileName2 = tokens[1];

			if (fileList.contains(fileName1) && fileList.contains(fileName2)) {
				double totalCnt = (double) filePairCount.getCount();
				int index1 = fileIndexMap.get(fileName1);
				int index2 = fileIndexMap.get(fileName2);

				double similarity = 0.0;

				List<ChangeRelationCount> changeRelationCountList = ChangeRelationCountDAO
						.selectByRepositoryIdAndFilePair(repositoryId, filePair);
				// System.out.println(++k + " : " + filePair);
				for (ChangeRelationCount changeRelationCount : changeRelationCountList) {
					String changeType1 = changeRelationCount.getChangeType1();
					String changeType2 = changeRelationCount.getChangeType2();
					double relationCnt = (double) changeRelationCount.getCount();

					int compareResult = changeType1.compareTo(changeType2);
					String relationType = null;

					if (compareResult <= 0) {
						relationType = changeType1 + "--" + changeType2;
						if (relationIndexMap.containsKey(relationType)) {
							similarity += relationCnt / totalCnt;
						}
					} else {
						relationType = changeType2 + "--" + changeType1;
						if (relationIndexMap.containsKey(relationType)) {
							similarity += relationCnt / totalCnt;
						}
					}
				}
				distances[index1][index2] = threshold2 + 1.0 - similarity;
				distances[index2][index1] = threshold2 + 1.0 - similarity;
			}
		}

		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances[0].length; j++) {
				if (distances[i][j] == 0.0) {
					distances[i][j] = threshold2 + 1.0;
				}
			}
		}

		// hierarchical cluster
		ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
		List<Cluster> clusters = alg.performFlatClustering(distances, clusterNames, new AverageLinkageStrategy(),
				threshold2 + 1.0);
		// DendrogramPanel.displayMultipleClusters(clusters);

		// select hotsopt
		int clusterThresholdId = -1;

		if (threshold1 == 3 && threshold2 == 60) {
			clusterThresholdId = 1;
		}

		for (Cluster cluster : clusters) {
			cluster.retrieveLeaves();
			storeClusterHelper(clusterThresholdId, gitRepository.getRepositoryId(), new ArrayList<Integer>(),
					globalClusterId, cluster, fileIndexMap, distances, threshold2 + 1.0);
		}

		if (clusterDBList.size() > 0) {
			// store, clear
			OriginClusterDAO.insertBatch(clusterDBList);
			clusterDBList.clear();
		}

		if (clusterAncestorDBList.size() > 0) {
			// store, clear
			ClusterAncestorDAO.insertBatch(clusterAncestorDBList);
			clusterAncestorDBList.clear();
		}
	}

	private void storeClusterHelper(int clusterThresholdId, int repositoryId, List<Integer> ancestorIds, int clusterId,
			Cluster cluster, Map<String, Integer> fileIndexMap, double[][] distances, double maxDistance) {
		// System.out.println("cluster Id: " + clusterId);
		globalClusterId++;

		String coreFile = null;
		List<String> fileNames = new ArrayList<String>(cluster.getLeafNames());
		double clusterSize = fileNames.size();

		if (cluster.isLeaf()) {
			return;
		}

		double maxCnt = 0;

		for (int i = 0; i < clusterSize; i++) {
			String fileName1 = fileNames.get(i);
			int index1 = fileIndexMap.get(fileName1);

			int cnt = 0;
			for (int j = 0; j < clusterSize; j++) {
				String fileName2 = fileNames.get(j);
				int index2 = fileIndexMap.get(fileName2);

				if (distances[index1][index2] != maxDistance) {
					cnt++;
				}
			}
			if (cnt > maxCnt) {
				maxCnt = cnt;
				coreFile = fileName1;
			}
		}

		// store cluster
		for (String fileName : fileNames) {
			OriginCluster originCluster = null;

			if (fileName.equals(coreFile)) {
				originCluster = new OriginCluster(clusterThresholdId, repositoryId, clusterId, clusterSize, maxCnt,
						coreFile, 1);
				// clusterDBList.add(originCluster);
			} else {
				originCluster = new OriginCluster(clusterThresholdId, repositoryId, clusterId, clusterSize, maxCnt,
						fileName, 0);
				// clusterDBList.add(originCluster);
			}

			// if (clusterDBList.size() >= 10) {
			// store, clear
			// OriginClusterDAO.insertBatch(clusterDBList);
			// clusterDBList.clear();
			// }

			OriginClusterDAO.insert(originCluster);
		}

		// store cluster ancestor
		if (ancestorIds.size() > 0) {
			for (int i = ancestorIds.size() - 1; i >= 0; i--) {
				ClusterAncestor clusterAncestor = null;

				int ancestorId = ancestorIds.get(i);

				if (i == ancestorIds.size() - 1) {
					clusterAncestor = new ClusterAncestor(clusterThresholdId, repositoryId, clusterId, ancestorId, 1);
					// clusterAncestorDBList.add(clusterAncestor);
				} else {
					clusterAncestor = new ClusterAncestor(clusterThresholdId, repositoryId, clusterId, ancestorId, 0);
					// clusterAncestorDBList.add(clusterAncestor);
				}

				ClusterAncestorDAO.insert(clusterAncestor);
			}
		}

		List<Integer> leftChildAncestorIds = new ArrayList<Integer>(ancestorIds);
		leftChildAncestorIds.add(clusterId);

		storeClusterHelper(clusterThresholdId, repositoryId, leftChildAncestorIds, globalClusterId,
				cluster.getChildren().get(0), fileIndexMap, distances, maxDistance);

		List<Integer> rightChildAncestorIds = new ArrayList<Integer>(ancestorIds);
		rightChildAncestorIds.add(clusterId);

		storeClusterHelper(clusterThresholdId, repositoryId, rightChildAncestorIds, globalClusterId,
				cluster.getChildren().get(1), fileIndexMap, distances, maxDistance);
	}

	private void printCCR(double[][] distances, String coreFile, List<String> fileNames,
			Map<String, Integer> fileIndexMap, double maxDistance) {
		int i = fileIndexMap.get(coreFile);

		for (int k = 0; k < fileNames.size(); k++) {
			int j = fileIndexMap.get(fileNames.get(k));
			if (distances[i][j] != maxDistance) {
				System.out.println(coreFile + " , " + fileNames.get(k));
				System.out.println(i + " , " + j);
			}
		}

	}

	private void computeCcFrequency(int repositoryId, HotspotModel hotspot) {
		List<String> totalList = hotspot.getTotalList();
		for (int i = 0; i < totalList.size(); i++) {
			System.out.println(i + 1 + "  " + totalList.get(i));
			for (int j = 0; j < totalList.size(); j++) {
				if (totalList.get(i).equals(totalList.get(j))) {
					continue;
				}

				String filePair = "";
				if (i < j) {
					filePair = totalList.get(i) + "||" + totalList.get(j);
				} else {
					filePair = totalList.get(j) + "||" + totalList.get(i);
				}

				FilePairCount fpc = FilePairCountDAO.selectByRepositoryIdAndFilePair(repositoryId, filePair);
				if (fpc != null) {
					int cnt = fpc.getCount();
					System.out.print(i + 1 + "  ");
					System.out.print(j + 1 + "  ");
					System.out.println(cnt);
				}
			}
			System.out.println();
		}
	}

	private void displayWeightMatrix(String coreFile, List<String> fileList, Map<String, Integer> fileIndexMap,
			double[][] distances) {
		int index1 = fileIndexMap.get(coreFile);

		for (int i = 0; i < fileList.size(); i++) {
			int index2 = fileIndexMap.get(fileList.get(i));

			if (distances[index1][index2] != 65) {
				System.out.println(fileList.get(i));
			}
		}

	}

	public static List<String> getCoreFileList(List<HotspotModel> hotspotList) {
		Set<String> set = new HashSet<String>();
		for (HotspotModel hotspot : hotspotList) {
			set.add(hotspot.getCoreFile());
		}
		return new ArrayList<String>(set);
	}

	public static List<String> getOtherFileList(List<HotspotModel> hotspotList, List<String> coreFileList) {
		Set<String> set = new HashSet<String>();
		for (HotspotModel hotspot : hotspotList) {
			set.addAll(hotspot.getFileList());
		}
		List<String> rawOtherFileList = new ArrayList<String>(set);
		rawOtherFileList.removeAll(coreFileList);
		return rawOtherFileList;
	}

	public static List<String> getTotalFileList(List<HotspotModel> hotspotList) {
		Set<String> set = new HashSet<String>();
		for (HotspotModel hotspot : hotspotList) {
			set.add(hotspot.getCoreFile());
			set.addAll(hotspot.getFileList());
		}
		return new ArrayList<String>(set);
	}

	private List<String> getFileInSnapshot(List<String> snapshotFileList, List<FilePairCount> filePairCountList) {
		Set<String> fileSet = new HashSet<String>();
		for (FilePairCount filePairItem : filePairCountList) {
			String[] filePairs = filePairItem.getFilePair().split("\\|\\|");
			if (snapshotFileList.contains(filePairs[0])) {
				fileSet.add(filePairs[0]);
			}
			if (snapshotFileList.contains(filePairs[1])) {
				fileSet.add(filePairs[1]);
			}
		}

		List<String> fileList = new ArrayList<String>(fileSet);
		Collections.sort(fileList);
		return fileList;
	}
}
