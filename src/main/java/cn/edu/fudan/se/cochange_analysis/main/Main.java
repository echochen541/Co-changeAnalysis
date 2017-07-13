package cn.edu.fudan.se.cochange_analysis.main;

import cn.edu.fudan.se.cochange_analysis.crawler.JIRACrawler;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class Main {
	public static void main(String[] args) {
		GitRepository repository = new GitRepository(1, "camel", "D:/echo/lab/research/co-change/projects/camel/.git");
		String beginUrl = "https://issues.apache.org/jira/rest/api/latest/search?jql=";
		int pageSize = 100;
		int totalSize = 3874;
		JIRACrawler crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		// crawler.crawl();

		// repository = new GitRepository(2, "cassandra",
		// "D:/echo/lab/research/co-change/projects/cassandra/.git");
		// totalSize = 7418;
		// crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		// crawler.crawl();

		// repository = new GitRepository(3, "cxf",
		// "D:/echo/lab/research/co-change/projects/cxf/.git");
		// totalSize = 4644;
		// crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		// crawler.crawl();

		// there five jira projects
		repository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		totalSize = 18772;
		crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		crawler.crawl();

		// repository = new GitRepository(5, "hbase",
		// "D:/echo/lab/research/co-change/projects/hbase/.git");
		// totalSize = 9028;
		// crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		// crawler.crawl();

		// repository = new GitRepository(6, "wicket",
		// "D:/echo/lab/research/co-change/projects/wicket/.git");
		// totalSize = 3913;
		// crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		// crawler.crawl();
	}
}