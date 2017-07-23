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
		//TODO 每个项目release 表格
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		LoadFileDependencyFileToDb a = new LoadFileDependencyFileToDb(gitRepository,"release");
		String inputDir = "D:\\Workspace\\co-change summer\\understand\\camel";
		a.parse(inputDir,inputDir);
		System.out.println("Finished");
	}


	private void parse(String inputDir, String outputDir) {
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
