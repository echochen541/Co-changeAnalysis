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
		String inputDir = "D:\\echo\\lab\\research\\co-change\\ICSE-2018\\data\\hotspot-dsm\\archIssues-camel_sdsm";
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
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.startsWith("</")) {
					continue;
				}
				if (line.startsWith("<")) {
					String archIssueName=match(line)+"\n";
					fos.write(archIssueName.getBytes());
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
		String reg = "<(.*) name=";
		Matcher m = Pattern.compile(reg).matcher(source);
		while (m.find()) {
			String r = m.group(1);
			result = r;
			return result;
		}
		return result;
	}

}
