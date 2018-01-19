package cn.edu.fudan.se.cochange_analysis.main;

import java.util.List;

import cn.edu.fudan.se.cochange_analysis.dsm.DSMGenerator;
import cn.edu.fudan.se.cochange_analysis.extractor.BugExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.ChangeExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.ChangeRelationExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.FilePairExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.HotspotFileDAO;
import cn.edu.fudan.se.cochange_analysis.preprocess.CommitFilter;

public class Main {
	public static void main(String[] args) {
		System.out.println(1);
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		CommitFilter commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		FilePairExtractor filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		ChangeExtractor changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		ChangeRelationExtractor changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		// changeRelationExtractor.extractChangeRelation(3, 3);
		String changeRelationSumOutputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-sum";
		// changeRelationExtractor.sumChangeRelation(changeRelationSumOutputDir);
		BugExtractor bugExtractor = new BugExtractor(gitRepository);
		// bugExtractor.extractBug();
		DSMGenerator dsmGenerator = new DSMGenerator(gitRepository);
		List<String> snapshotFileList = dsmGenerator.getSnapshotFile("camel-2.19.1");
		// System.out.println("snapshotFileList.size(): " +
		// snapshotFileList.size());
		String crdsmOutputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-dsm";
		// dsmGenerator.generateCRDSM(snapshotFileList, crdsmOutputDir, 32, 3);
		List<String> hotspotFileList = HotspotFileDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		bugExtractor.computeAverageBFAndBC(snapshotFileList);
		bugExtractor.computeAverageBFAndBC(hotspotFileList);

		System.out.println(2);
		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		// changeRelationExtractor.extractChangeRelation(3, 3);
		// changeRelationExtractor.sumChangeRelation(changeRelationSumOutputDir);
		bugExtractor = new BugExtractor(gitRepository);
		// bugExtractor.extractBug();
		dsmGenerator = new DSMGenerator(gitRepository);
		snapshotFileList = dsmGenerator.getSnapshotFile("cassandra-3.11.0");
		// System.out.println("snapshotFileList.size(): " +
		// snapshotFileList.size());
		// dsmGenerator.generateCRDSM(snapshotFileList, crdsmOutputDir, 32, 3);
		hotspotFileList = HotspotFileDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		bugExtractor.computeAverageBFAndBC(snapshotFileList);
		bugExtractor.computeAverageBFAndBC(hotspotFileList);

		System.out.println(3);
		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		// changeRelationExtractor.extractChangeRelation(3, 3);
		// changeRelationExtractor.sumChangeRelation(changeRelationSumOutputDir);
		bugExtractor = new BugExtractor(gitRepository);
		// bugExtractor.extractBug();
		dsmGenerator = new DSMGenerator(gitRepository);
		snapshotFileList = dsmGenerator.getSnapshotFile("cxf-3.1.11");
		// System.out.println("snapshotFileList.size(): " +
		// snapshotFileList.size());
		// dsmGenerator.generateCRDSM(snapshotFileList, crdsmOutputDir, 32, 3);
		hotspotFileList = HotspotFileDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		bugExtractor.computeAverageBFAndBC(snapshotFileList);
		bugExtractor.computeAverageBFAndBC(hotspotFileList);

		System.out.println(4);
		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		// changeRelationExtractor.extractChangeRelation(3, 3);
		// changeRelationExtractor.sumChangeRelation(changeRelationSumOutputDir);
		bugExtractor = new BugExtractor(gitRepository);
		// bugExtractor.extractBug();
		dsmGenerator = new DSMGenerator(gitRepository);
		snapshotFileList = dsmGenerator.getSnapshotFile("YARN-5355-branch-2-2017-04-25");
		// System.out.println("snapshotFileList.size(): " +
		// snapshotFileList.size());
		// dsmGenerator.generateCRDSM(snapshotFileList, crdsmOutputDir, 32, 3);
		hotspotFileList = HotspotFileDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		bugExtractor.computeAverageBFAndBC(snapshotFileList);
		bugExtractor.computeAverageBFAndBC(hotspotFileList);

		System.out.println(5);
		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		// changeRelationExtractor.extractChangeRelation(3, 3);
		// changeRelationExtractor.sumChangeRelation(changeRelationSumOutputDir);
		bugExtractor = new BugExtractor(gitRepository);
		// bugExtractor.extractBug();
		dsmGenerator = new DSMGenerator(gitRepository);
		snapshotFileList = dsmGenerator.getSnapshotFile("release-0.18.0");
		// System.out.println("snapshotFileList.size(): " +
		// snapshotFileList.size());
		// dsmGenerator.generateCRDSM(snapshotFileList, crdsmOutputDir, 32, 3);
		hotspotFileList = HotspotFileDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		bugExtractor.computeAverageBFAndBC(snapshotFileList);
		bugExtractor.computeAverageBFAndBC(hotspotFileList);

		System.out.println(6);
		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		// changeRelationExtractor.extractChangeRelation(3, 3);
		// changeRelationExtractor.sumChangeRelation(changeRelationSumOutputDir);
		bugExtractor = new BugExtractor(gitRepository);
		// bugExtractor.extractBug();
		dsmGenerator = new DSMGenerator(gitRepository);
		snapshotFileList = dsmGenerator.getSnapshotFile("wicket_1_2_b2_before_charsequence");
		// System.out.println("snapshotFileList.size(): " +
		// snapshotFileList.size());
		// dsmGenerator.generateCRDSM(snapshotFileList, crdsmOutputDir, 32, 3);
		hotspotFileList = HotspotFileDAO.selectByRepositoryId(gitRepository.getRepositoryId());
		bugExtractor.computeAverageBFAndBC(snapshotFileList);
		bugExtractor.computeAverageBFAndBC(hotspotFileList);
	}
}