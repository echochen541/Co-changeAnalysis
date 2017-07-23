package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotFile;
import cn.edu.fudan.se.cochange_analysis.git.dao.HotspotFileDAO;

public class LoadArchDebtFileToDb {
	private GitRepository repository;
	private String release;

	public LoadArchDebtFileToDb(GitRepository repository, String release) {
		this.repository = repository;
		this.release = release;
	}

	public static void main(String args[]) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		LoadArchDebtFileToDb parser = new LoadArchDebtFileToDb(gitRepository, "2.19.1");
		String inputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm/archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		parser = new LoadArchDebtFileToDb(gitRepository, "3.11.0");
		inputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm/archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);
		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		parser = new LoadArchDebtFileToDb(gitRepository, "3.1.11");
		inputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm/archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);
		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		parser = new LoadArchDebtFileToDb(gitRepository, "YARN-5355-branch-2-2017-04-25");
		inputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm/archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);
		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		parser = new LoadArchDebtFileToDb(gitRepository, "release-0.18.0");
		inputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm/archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);
		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		parser = new LoadArchDebtFileToDb(gitRepository, "wicket_1_2_b2_before_charsequence");
		inputDir = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm/archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);
	}

	public void parse(String fileName, String dir) {
		FileInputStream fis;
		List<HotspotFile> fileList = new ArrayList<HotspotFile>();
		int id = 0;

		try {
			fis = new FileInputStream(new File(dir + File.separator + fileName));
			Scanner sc = new Scanner(fis);
			int flag = 0;
			HotspotFile hf = null;
			String archIssueName = null;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line = line.trim();
				if (line.startsWith("</")) {
					if (line.startsWith("</" + archIssueName.trim())) {
						flag = 0;
					}
					continue;
				}
				if (line.startsWith("<")) {
					if (flag == 1) {
						continue;
					}
					// System.out.println("One");
					archIssueName = match(line);

					flag = 1;
					continue;
				}
				// System.out.println(line);
				String[] excelLine = line.split(" ");

				String tmpFileName = null;
				if (excelLine.length == 1) {
					// id = 0;
					tmpFileName = excelLine[0];
				} else {
					// id = Integer.parseInt(excelLine[0]);
					tmpFileName = excelLine[1];
				}
				hf = new HotspotFile(this.repository.getRepositoryId(), archIssueName, tmpFileName, this.release);
				fileList.add(hf);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("FileListSize:" + fileList.size());
		List<HotspotFile> batchList = new ArrayList<HotspotFile>();
		for (HotspotFile item : fileList) {
			batchList.add(item);
			if (batchList.size() == 100) {
				HotspotFileDAO.insertBatch(batchList);
				batchList.clear();
			}
		}
		if (!batchList.isEmpty())
			HotspotFileDAO.insertBatch(batchList);
	}

	public static String match(String source) {
		String result = null;
		String[] tmp = source.split(" ");
		result = tmp[0].replace('<', ' ');
		result = result.replace('>', ' ');
		return result.trim();
	}
}
