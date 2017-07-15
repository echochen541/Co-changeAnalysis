package cn.edu.fudan.se.cochange_analysis.main;

import cn.edu.fudan.se.cochange_analysis.extractor.BugExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.ChangeExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.ChangeRelationExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.FilePairExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.preprocess.CommitFilter;

public class Main {
	public static void main(String[] args) {
		System.out.println(1);
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		// CommitFilter commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		// FilePairExtractor filePairExtractor = new
		// FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		// ChangeExtractor changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		ChangeRelationExtractor changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		changeRelationExtractor.extractChangeRelation(3, 3);
		// BugExtractor extractor = new BugExtractor(gitRepository);
		// extractor.extractBug();

		System.out.println(2);
		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		// commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		// filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		changeRelationExtractor.extractChangeRelation(3, 3);
		// extractor = new BugExtractor(gitRepository);
		// extractor.extractBug();

		System.out.println(3);
		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		// commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		// filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		changeRelationExtractor.extractChangeRelation(3, 3);
		// extractor = new BugExtractor(gitRepository);
		// extractor.extractBug();

		System.out.println(4);
		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		// commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		// filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		changeRelationExtractor.extractChangeRelation(3, 3);
		// extractor = new BugExtractor(gitRepository);
		// extractor.extractBug();

		System.out.println(5);
		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		// commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		// filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		changeRelationExtractor.extractChangeRelation(3, 3);
		// extractor = new BugExtractor(gitRepository);
		// extractor.extractBug();

		System.out.println(6);
		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		// commitFilter = new CommitFilter(gitRepository);
		// commitFilter.filterCommits();
		// filePairExtractor = new FilePairExtractor(gitRepository);
		// filePairExtractor.extractFilePairHistory();
		// changeExtractor = new ChangeExtractor(gitRepository);
		// changeExtractor.extracChange();
		changeRelationExtractor = new ChangeRelationExtractor(gitRepository);
		changeRelationExtractor.extractChangeRelation(3, 3);
		// extractor = new BugExtractor(gitRepository);
		// extractor.extractBug();
	}
}