package cn.edu.fudan.se.cochange_analysis.main;

import cn.edu.fudan.se.cochange_analysis.extractor.ChangeRelationExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class Main {
	public static void main(String[] args) {
		// extract change relation
		System.out.println("Begin extract change relation:");
		
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		ChangeRelationExtractor extractor = new ChangeRelationExtractor(gitRepository);
		extractor.extractChangeRelation(20, 3);

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		extractor = new ChangeRelationExtractor(gitRepository);
		extractor.extractChangeRelation(20, 3);

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		extractor = new ChangeRelationExtractor(gitRepository);
		extractor.extractChangeRelation(20, 3);

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		extractor = new ChangeRelationExtractor(gitRepository);
		extractor.extractChangeRelation(20, 3);

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		extractor = new ChangeRelationExtractor(gitRepository);
		extractor.extractChangeRelation(20, 3);

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		extractor = new ChangeRelationExtractor(gitRepository);
		extractor.extractChangeRelation(20, 3);
	}
}
