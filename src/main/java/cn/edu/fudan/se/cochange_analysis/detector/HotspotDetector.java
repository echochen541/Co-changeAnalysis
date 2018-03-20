package cn.edu.fudan.se.cochange_analysis.detector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.parser.Parse2Tree;

public class HotspotDetector {
	private GitRepository gitRepository;

	public HotspotDetector() {
	}

	public HotspotDetector(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	public List<Hotspot> detectHotspot(String clusterDir, String crdsmDir, int threshold1, double threshold2) {
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
		GitRepository gitRepository = new GitRepository(2, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		HotspotDetector hotspotDetector = new HotspotDetector(gitRepository);
		String clusterDir = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-dsm/";
		String crdsmDir = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-dsm/";
		List<Hotspot> hotspotList = hotspotDetector.detectHotspot(clusterDir, crdsmDir, 4, 0.25);
		for (int i = 0; i < hotspotList.size(); i++) {
			System.out.println("Core File:" + hotspotList.get(i).getCoreFile());
			System.out.println("Other FileList:" + hotspotList.get(i).getFileList());
			System.out.println();
		}
	}

	public static List<String> getCoreFileList(List<Hotspot> hotspotList) {
		Set<String> set = new HashSet<String>();
		for (Hotspot hotspot : hotspotList) {
			set.add(hotspot.getCoreFile());
		}
		return new ArrayList<String>(set);
	}

	public static List<String> getOtherFileList(List<Hotspot> hotspotList, List<String> coreFileList) {
		Set<String> set = new HashSet<String>();
		for (Hotspot hotspot : hotspotList) {
			set.addAll(hotspot.getFileList());
		}
		List<String> rawOtherFileList = new ArrayList<String>(set);
		rawOtherFileList.removeAll(coreFileList);
		return rawOtherFileList;
	}

	public static List<String> getTotalFileList(List<Hotspot> hotspotList) {
		Set<String> set = new HashSet<String>();
		for (Hotspot hotspot : hotspotList) {
			set.add(hotspot.getCoreFile());
			set.addAll(hotspot.getFileList());
		}
		return new ArrayList<String>(set);
	}
}
