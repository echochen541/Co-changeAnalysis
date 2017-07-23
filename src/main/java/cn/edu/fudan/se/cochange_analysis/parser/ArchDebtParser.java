package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class ArchDebtParser {
	private GitRepository repository;

	public ArchDebtParser(GitRepository repository) {
		this.repository = repository;
	}

	public static void main(String args[]){
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		ArchDebtParser a = new ArchDebtParser(gitRepository);
		String inputDir = "E:\\2017-07-20\\data\\hotspot\\archIssues-camel_sdsm";
		a.parse("ArchIssues.txt", inputDir);
		 inputDir = "E:\\2017-07-20\\data\\hotspot\\archIssues-cassandra_sdsm";
		a.parse("ArchIssues.txt", inputDir);
		 inputDir = "E:\\2017-07-20\\data\\hotspot\\archIssues-cxf_sdsm";
		a.parse("ArchIssues.txt", inputDir);
		 inputDir = "E:\\2017-07-20\\data\\hotspot\\archIssues-hadoop_sdsm";
		a.parse("ArchIssues.txt", inputDir);
		 inputDir = "E:\\2017-07-20\\data\\hotspot\\archIssues-hbase_sdsm";
		a.parse("ArchIssues.txt", inputDir);
		 inputDir = "E:\\2017-07-20\\data\\hotspot\\archIssues-wicket_sdsm";
		a.parse("ArchIssues.txt", inputDir);
		System.out.println("Finished");
		
	}

	public void parse(String fileName, String dir) {
		FileInputStream fis;
		FileOutputStream fos;
		try {
			fis = new FileInputStream(new File(dir + File.separator + fileName));
			fos = new FileOutputStream(new File(dir + File.separator + fileName.split("\\.")[0] + ".csv"));
			Scanner sc = new Scanner(fis);
			int flag=0;
			String archIssueName=null;
			while (sc.hasNextLine()) {
				
				String line = sc.nextLine();
				if (line.startsWith("</")) {
					if (line.startsWith("</"+archIssueName.trim())){
						flag=0;
					}
					continue;
				}
				if (line.startsWith("<")) {
					if(flag==1){
						continue;
					}
					archIssueName=match(line)+"\n";
					fos.write(archIssueName.getBytes());
					flag=1;
					continue;
				}
				
				String excelLine=line.replace(' ', ',')+",\n";
				fos.write(excelLine.getBytes());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String match(String source) {
		String result = null;
		String[] tmp=source.split(" ");
		result=tmp[0].replace('<', ' ');
		result=result.replace('>', ' ');
		return result.trim();
	}

}
