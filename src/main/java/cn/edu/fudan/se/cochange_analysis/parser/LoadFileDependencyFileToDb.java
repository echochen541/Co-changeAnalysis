package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.edu.fudan.se.cochange_analysis.dsm.GenerateDSM;
import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.SnapshotFile;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.SnapshotFileDAO;

public class LoadFileDependencyFileToDb {
	private GitRepository repository;
	private String release;
	public LoadFileDependencyFileToDb(GitRepository repository,String release) {
		this.release=release;
		this.repository = repository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		LoadFileDependencyFileToDb a = new LoadFileDependencyFileToDb(gitRepository,"2.19.1");
		String inputDir = "D:\\Workspace\\co-change summer\\understand\\"+a.repository.getRepositoryName();
//		a.parse(inputDir);
//		System.out.println(a.repository.getRepositoryName()+" finished");
		
		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		 a = new LoadFileDependencyFileToDb(gitRepository,"3.11.0");
		 inputDir = "D:\\Workspace\\co-change summer\\understand\\"+a.repository.getRepositoryName();
		a.parse(inputDir);
		System.out.println(a.repository.getRepositoryName()+" finished");
		
		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		a = new LoadFileDependencyFileToDb(gitRepository,"3.1.11");
		inputDir = "D:\\Workspace\\co-change summer\\understand\\"+a.repository.getRepositoryName();
		a.parse(inputDir);
		System.out.println(a.repository.getRepositoryName()+" finished");
		
		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		 a = new LoadFileDependencyFileToDb(gitRepository,"YARN-5355-branch-2-2017-04-25");
		 inputDir = "D:\\Workspace\\co-change summer\\understand\\"+a.repository.getRepositoryName();
		a.parse(inputDir);
		System.out.println(a.repository.getRepositoryName()+" finished");
		
		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		 a = new LoadFileDependencyFileToDb(gitRepository,"release-0.18.0");
		 inputDir = "D:\\Workspace\\co-change summer\\understand\\"+a.repository.getRepositoryName();
		a.parse(inputDir);
		System.out.println(a.repository.getRepositoryName()+" finished");
		
		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		a = new LoadFileDependencyFileToDb(gitRepository,"wicket_1_2_b2_before_charsequence");
		inputDir = "D:\\Workspace\\co-change summer\\understand\\"+a.repository.getRepositoryName();
		a.parse(inputDir);
		System.out.println(a.repository.getRepositoryName()+" finished");
	}


	private void parse(String inputDir) {
		String gitRepositoryName = this.repository.getRepositoryName();
		String xmlPath = inputDir + "/" + this.repository.getRepositoryName() + "_FileDependencyCytoscape.xml";
		File f = new File(xmlPath);
		List<String> fileList = new ArrayList<String>();
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(f);
			Element root = document.getRootElement();
			List<Element> nodes = root.elements("node");
			for (Iterator<Element> it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				int id = Integer.parseInt(elm.attribute("id").getValue());
				List<Element> children = elm.elements("att");
				for (Element child : children) {
					Attribute name = child.attribute("name");
					if (name.getValue().equals("longName")) {
						String path = child.attribute("value").getValue();

						if (FileUtils.isTestFile(path)) {
							break;
						}

						path = path.replace("\\", "/");
						String parsedName = FileUtils.parseFilePath(path, gitRepositoryName);
						String startsWithStr = "org/apache/";
						if (gitRepositoryName.equals("hbase")) {
							startsWithStr += "hadoop/hbase";
						} else {
							startsWithStr += gitRepositoryName;
						}
						if (!parsedName.startsWith(startsWithStr)) {
							break;
						}
						fileList.add(parsedName);
					}
				}
			}
			Collections.sort(fileList);
			System.out.println("FileListSize:" + fileList.size());
			List<SnapshotFile> batchList=new ArrayList<SnapshotFile>();
			for(String item:fileList){
				SnapshotFile sf=new SnapshotFile(this.repository.getRepositoryId(),item,this.release);
				batchList.add(sf);
				if(batchList.size()==100){
					SnapshotFileDAO.insertBatch(batchList);
					batchList.clear();
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
