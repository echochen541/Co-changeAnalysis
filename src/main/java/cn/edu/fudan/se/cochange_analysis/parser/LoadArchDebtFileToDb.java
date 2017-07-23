package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.SnapshotFile;
import cn.edu.fudan.se.cochange_analysis.git.dao.HotspotFileDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.SnapshotFileDAO;

public class LoadArchDebtFileToDb {
	private GitRepository repository;
	private String release;

	public LoadArchDebtFileToDb(GitRepository repository,String release) {
		this.repository = repository;
		this.release=release;
	}

	public static void main(String args[]) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		ArchDebtParser parser = new ArchDebtParser(gitRepository);
		String inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		parser = new ArchDebtParser(gitRepository);
		inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		parser = new ArchDebtParser(gitRepository);
		inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		parser = new ArchDebtParser(gitRepository);
		inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		parser = new ArchDebtParser(gitRepository);
		inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		parser = new ArchDebtParser(gitRepository);
		inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-"
				+ gitRepository.getRepositoryName() + "_sdsm";
		parser.parse("ArchIssues.txt", inputDir);
	}

	public void parse(String fileName, String dir) {
		FileInputStream fis;
		List<HotspotFile> fileList=new ArrayList<HotspotFile>();
		
		
		try {
			fis = new FileInputStream(new File(dir + File.separator + fileName));
			Scanner sc = new Scanner(fis);
			int flag = 0;
			HotspotFile hf=null;
			String archIssueName = null;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line=line.trim();
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
					archIssueName = match(line);
					
					flag = 1;
					continue;
				}
				String[] excelLine = line.split(" ");
				hf=new HotspotFile(Integer.parseInt(excelLine[0]),this.repository.getRepositoryId(),archIssueName,excelLine[1],this.release);
				fileList.add(hf);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FileListSize:" + fileList.size());
		List<HotspotFile> batchList=new ArrayList<HotspotFile>();
		for(HotspotFile item:fileList){
			batchList.add(item);
			if(batchList.size()==100){
				HotspotFileDAO.insertBatch(batchList);
				batchList.clear();
			}
		}

	}

	public static String match(String source) {
		String result = null;
		String[] tmp = source.split(" ");
		result = tmp[0].replace('<', ' ');
		result = result.replace('>', ' ');
		return result.trim();
	}

}
