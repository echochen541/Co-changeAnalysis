package cn.edu.fudan.se.cochange_analysis.main;

import cn.edu.fudan.se.cochange_analysis.extractor.ChangeExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.FilePairExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.RelationChangeExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class Main {
	public static void main(String[] args) {
		// extract change operation
		// GitRepository gitRepository = new GitRepository(3, "cxf",
		// "D:/echo/lab/research/co-change/projects/cxf/.git");
		// ChangeExtractor changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();

		// gitRepository = new GitRepository(4, "hadoop",
		// "D:/echo/lab/research/co-change/projects/hadoop/.git");
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();

		// GitRepository gitRepository = new GitRepository(5, "hbase",
		// "D:/echo/lab/research/co-change/projects/hbase/.git");
		// ChangeExtractor changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();

		// gitRepository = new GitRepository(6, "wicket",
		// "D:/echo/lab/research/co-change/projects/wicket/.git");
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();

		int[] repos = { 1, 2, 3, 4 };
		for (int repoId : repos) {
			RelationChangeExtractor.generateDSM(repoId, 20, 5);
			RelationChangeExtractor.generateDSM(repoId, 20, 10);
			RelationChangeExtractor.generateDSM(repoId, 20, 15);
			
			RelationChangeExtractor.generateDSM(repoId, 30, 5);
			RelationChangeExtractor.generateDSM(repoId, 30, 10);
			RelationChangeExtractor.generateDSM(repoId, 30, 15);
		}

		// extract file pair
		// System.out.println("Begin extract file pair:");
		// GitRepository gitRepository = new GitRepository(1, "camel",
		// "D:/echo/lab/research/co-change/projects/camel/.git");
		// FilePairExtractor extractor = new FilePairExtractor(gitRepository);
		// extractor.extractFilePairHistory();

		// gitRepository = new GitRepository(2, "cassandra",
		// "D:/echo/lab/research/co-change/projects/cassandra/.git");
		// extractor = new FilePairExtractor(gitRepository);
		// extractor.extractFilePairHistory();

		// GitRepository gitRepository = new GitRepository(3, "cxf",
		// "D:/echo/lab/research/co-change/projects/cxf/.git");
		// FilePairExtractor extractor = new FilePairExtractor(gitRepository);
		// extractor.extractFilePairHistory();

		// gitRepository = new GitRepository(4, "hadoop",
		// "D:/echo/lab/research/co-change/projects/hadoop/.git");
		// extractor = new FilePairExtractor(gitRepository);
		// extractor.extractFilePairHistory();

		// gitRepository = new GitRepository(5, "hbase",
		// "D:/echo/lab/research/co-change/projects/hbase/.git");
		// extractor = new FilePairExtractor(gitRepository);
		// extractor.extractFilePairHistory();

		// gitRepository = new GitRepository(6, "wicket",
		// "D:/echo/lab/research/co-change/projects/wicket/.git");
		// extractor = new FilePairExtractor(gitRepository);
		// extractor.extractFilePairHistory();
	}
}
