package cn.edu.fudan.se.cochange_analysis.detector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;

import cn.edu.fudan.se.cochange_analysis.extractor.BugExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationSum;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.Hotspot;
import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotRelationSum;
import cn.edu.fudan.se.cochange_analysis.git.bean.OriginCluster;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationSumDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.HotspotDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.HotspotRelationSumDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.OriginClusterDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.SnapshotFileDAO;
import cn.edu.fudan.se.cochange_analysis.parser.Parse2Tree;

public class HotspotDetector {
	private GitRepository gitRepository;

	public HotspotDetector() {
	}

	public HotspotDetector(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	public List<HotspotModel> detectHotspot(String clusterDir, String crdsmDir, int threshold1, double threshold2) {
		Parse2Tree parser = new Parse2Tree();
		String repositoryName = gitRepository.getRepositoryName();
		String clusterFile = clusterDir + repositoryName + "..clsx";
		String crdsmFile = crdsmDir + repositoryName + "_64_3.dsm";

		parser.rootNode = parser.parse(clusterFile);
		parser.getFileList(clusterFile);
		parser.parseDSM(crdsmFile);
		parser.dfs(parser.rootNode);
		parser.pickGroups();
		return parser.findHotspots(parser.pickedGroups, threshold1, threshold2);
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		for (int i = 1; i <= 6; i++) {
			gitRepository.setRepositoryId(i);
			HotspotDetector hDetector = new HotspotDetector(gitRepository);
			// List<HotspotModel> hotspots = hDetector.detectHotspot(3, 60, 5,
			// 0.4);
			// computeMetrics(hotspots);
			hDetector.sumRelationInHotspot(i, 3, 60, 5.0, 0.4);
		}
	}

	public void detectHotspotFromDB(int repositoryId, int clusterThresholdId, double minSize, double minRatio) {
		List<Integer> clusterIds = OriginClusterDAO.selectByClusterThresholdIdAndMinSizeAndMinRatio(repositoryId,
				clusterThresholdId, minSize, minRatio);
		for (int clusterId : clusterIds) {
			if (minSize == 5.0 && minRatio == 0.2) {
				Hotspot hotspot = new Hotspot(clusterThresholdId, 1, repositoryId, clusterId);
				HotspotDAO.insert(hotspot);
			}
		}
	}

	private static void computeMetrics(List<HotspotModel> hotspots) {
		if (hotspots == null)
			return;

		int minSize = 10000;
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

	public List<HotspotModel> detectHotspot(int threshold1, int threshold2, double minSize, double minRatio) {
		List<HotspotModel> hotspots = new ArrayList<>();

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
		for (Cluster cluster : clusters) {
			cluster.retrieveLeaves();
			detectHotspotHelper(cluster, fileIndexMap, distances, hotspots, minSize, minRatio, threshold2 + 1.0);
		}

		System.out.println(gitRepository.getRepositoryId());

		List<String> totalList = HotspotDetector.getTotalFileList(hotspots);
		List<String> coreFileList = HotspotDetector.getCoreFileList(hotspots);
		List<String> otherFileList = HotspotDetector.getOtherFileList(hotspots, coreFileList);
		System.out.println("echo hotspot");
		System.out.println("total hotspot number: " + hotspots.size());
		System.out.println("total file number: " + totalList.size());
		BugExtractor bugExtractor = new BugExtractor(gitRepository);
		System.out.println("total: ");
		bugExtractor.computeAverageBFAndBC(totalList);
		System.out.println("core: ");
		bugExtractor.computeAverageBFAndBC(coreFileList);
		System.out.println("other: ");
		bugExtractor.computeAverageBFAndBC(otherFileList);

		return hotspots;
	}

	private void detectHotspotHelper(Cluster cluster, Map<String, Integer> fileIndexMap, double[][] distances,
			List<HotspotModel> hotspots, double minSize, double minRatio, double maxDistance) {
		String coreFile = null;
		List<String> fileNames = new ArrayList<String>(cluster.getLeafNames());
		double clusterSize = fileNames.size();

		if (clusterSize < minSize) {
			return;
		}

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

		if (maxCnt >= minRatio * (clusterSize - 1.0)) {
			fileNames.remove(coreFile);
			HotspotModel instance = new HotspotModel(coreFile, fileNames);
			hotspots.add(instance);

			if (clusterSize == 6 && coreFile.equals("org/apache/cassandra/config/DatabaseDescriptor.java")) {
				DendrogramPanel.displaySingleCluster(cluster);
				// System.out.println(coreFile);
				// System.out.println(fileNames);
				computeCcFrequency(2, instance);
				computeDistance(fileIndexMap, distances, instance);
				// printCCR(distances, coreFile, fileNames, fileIndexMap,
				// maxDistance);
			}
			// System.out.println("hotspot size: " + clusterSize);
		}

		detectHotspotHelper(cluster.getChildren().get(0), fileIndexMap, distances, hotspots, minSize, minRatio,
				maxDistance);
		detectHotspotHelper(cluster.getChildren().get(1), fileIndexMap, distances, hotspots, minSize, minRatio,
				maxDistance);
	}

	private void computeDistance(Map<String, Integer> fileIndexMap, double[][] distances, HotspotModel hotspot) {
		Set<Double> distanceSet = new HashSet<>();

		List<String> totalList = hotspot.getTotalList();
		for (int i = 0; i < totalList.size(); i++) {
			System.out.println((char) ('A' + i) + ": " + totalList.get(i));
			for (int j = 0; j < totalList.size(); j++) {
				if (totalList.get(i).equals(totalList.get(j))) {
					continue;
				}

				double distance = distances[fileIndexMap.get(totalList.get(i))][fileIndexMap.get(totalList.get(j))];

				if (distance != 61.0) {
					System.out.print((char) ('A' + i) + " ");
					System.out.print((char) ('A' + j) + " ");
					System.out.println(distance);
					distanceSet.add(distance);
				}
			}
			System.out.println();
		}

		List<Double> distanceList = new ArrayList<>(distanceSet);

		Collections.sort(distanceList);
		for (int i = 0; i < distanceList.size(); i++) {
			System.out.println(i + " , " + distanceList.get(i));
		}
		System.out.println();
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
			System.out.println((char) ('A' + i) + "  " + totalList.get(i));
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
					System.out.print((char) ('A' + i) + "  ");
					System.out.print((char) ('A' + j) + "  ");
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

	private void sumRelationInHotspot(int repositoryId, int minCCF, int topN, double minSize, double minRatio) {
		List<HotspotRelationSum> hrsList = new ArrayList<>();

		int clusterThresholdId = -1, hotspotThresholdId = -1;
		if (minCCF == 3 && topN == 60) {
			clusterThresholdId = 1;
		}

		if (minSize == 5.0 && minRatio == 0.2) {
			hotspotThresholdId = 1;
		}

		if (minSize == 5.0 && minRatio == 0.4) {
			hotspotThresholdId = 2;
		}

		if (clusterThresholdId < 0 || hotspotThresholdId < 0) {
			return;
		}

		List<Hotspot> hotspotList = HotspotDAO.selectByThresholds(repositoryId, clusterThresholdId, hotspotThresholdId);
		System.out.println("repository id: " + repositoryId);
		System.out.println("hotspot size: " + hotspotList.size());

		// get top threshold2 co-change relations
		List<ChangeRelationSum> relationSumList = ChangeRelationSumDAO.selectTopNByRepositoryId(topN, repositoryId);

		Map<String, Integer> relationIndexMap = new HashMap<String, Integer>();
		for (int i = 0; i < relationSumList.size(); i++) {
			relationIndexMap.put(relationSumList.get(i).getRelationType(), i);
		}

		// cache
		Map<String, Map<String, Integer>> filePairRelationSumMap = new HashMap<>();

		for (Hotspot hotspot : hotspotList) {
			// System.out.println("hotspot: " + hotspot);

			int clusterId = hotspot.getClusterId();
			List<OriginCluster> oClusterList = OriginClusterDAO
					.selectByRepositoryIdAndClusterThresholdIdAndClusterId(repositoryId, clusterThresholdId, clusterId);

			Map<String, Integer> singleClusterRelationSumMap = new HashMap<>();

			for (int i = 0; i < oClusterList.size(); i++) {
				String fileName1 = oClusterList.get(i).getFileName();

				for (int j = i + 1; j < oClusterList.size(); j++) {
					String fileName2 = oClusterList.get(j).getFileName();
					String filePair = null;

					if (fileName1.compareTo(fileName2) < 0) {
						filePair = fileName1 + "||" + fileName2;
					} else {
						filePair = fileName2 + "||" + fileName1;
					}

					// System.out.println("filePair: " + filePair);
					// file pair in cache
					if (filePairRelationSumMap.containsKey(filePair)) {
						Map<String, Integer> singleFilePairRelationSumMap = filePairRelationSumMap.get(filePair);
						for (Entry<String, Integer> entry : singleFilePairRelationSumMap.entrySet()) {
							String relationType = entry.getKey();
							Integer sum = entry.getValue();

							if (singleClusterRelationSumMap.containsKey(relationType)) {
								singleClusterRelationSumMap.put(relationType,
										singleClusterRelationSumMap.get(relationType) + sum);
							} else {
								singleClusterRelationSumMap.put(relationType, sum);
							}
						}
					} else { // file pair not in cache
						Map<String, Integer> singleFilePairRelationSumMap = new HashMap<>();

						List<ChangeRelationCount> crcList = ChangeRelationCountDAO
								.selectByRepositoryIdAndFilePair(repositoryId, filePair);

						for (ChangeRelationCount crc : crcList) {
							String changeType1 = crc.getChangeType1();
							String changeType2 = crc.getChangeType2();
							int sum = crc.getCount();

							int compareResult = changeType1.compareTo(changeType2);
							String relationType = null;

							if (compareResult <= 0) {
								relationType = changeType1 + "--" + changeType2;
							} else {
								relationType = changeType2 + "--" + changeType1;
							}

							if (relationIndexMap.containsKey(relationType)) {
								if (singleClusterRelationSumMap.containsKey(relationType)) {
									singleClusterRelationSumMap.put(relationType,
											singleClusterRelationSumMap.get(relationType) + sum);
								} else {
									singleClusterRelationSumMap.put(relationType, sum);
								}
								singleFilePairRelationSumMap.put(relationType, sum);
							}
						}

						filePairRelationSumMap.put(filePair, singleFilePairRelationSumMap);
						// System.out.println(singleFilePairRelationSumMap);
					}
				}
			}

			// store singleClusterRelationSumMap to db
			for (Entry<String, Integer> entry : singleClusterRelationSumMap.entrySet()) {
				String relationType = entry.getKey();
				int sum = entry.getValue();
				HotspotRelationSum hrs = new HotspotRelationSum(clusterThresholdId, hotspotThresholdId, repositoryId,
						clusterId, relationType, sum);
				hrsList.add(hrs);
				if (hrsList.size() >= 100) {
					HotspotRelationSumDAO.insertBatch(hrsList);
					hrsList.clear();
				}
			}
		}

		if (hrsList.size() > 0) {
			HotspotRelationSumDAO.insertBatch(hrsList);
			hrsList.clear();
		}
	}
}
