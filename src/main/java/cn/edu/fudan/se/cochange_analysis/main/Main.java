package cn.edu.fudan.se.cochange_analysis.main;

import cn.edu.fudan.se.cochange_analysis.extractor.ChangeExtractor;
import cn.edu.fudan.se.cochange_analysis.extractor.FilePairExtractor;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.preprocess.CommitFilter;

public class Main {
	public static void main(String[] args) {
		// filter commits
		System.out.println("Begin filter commits:");
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		CommitFilter commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		commitFilter = new CommitFilter(gitRepository);
		System.out.println(gitRepository.getRepositoryId());
		commitFilter.filterCommits();

		// extract change operation
		System.out.println("Begin extract change operation:");
		gitRepository = new GitRepository(1, "camel", "D:/echo/lab/research/co-change/projects/camel/.git");
		ChangeExtractor changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		changeExtractor = new ChangeExtractor(gitRepository);
		changeExtractor.extracChange();

		// extract file pair
		System.out.println("Begin extract file pair:");
		gitRepository = new GitRepository(1, "camel", "D:/echo/lab/research/co-change/projects/camel/.git");
		FilePairExtractor extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		extractor = new FilePairExtractor(gitRepository);
		extractor.extractFilePairHistory();
	}
}
