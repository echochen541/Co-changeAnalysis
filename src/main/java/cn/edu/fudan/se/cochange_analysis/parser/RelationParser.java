package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.edu.fudan.se.cochange_analysis.file.util.GenerateDSM;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;
import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;


public class RelationParser {
	private GitRepository repository;
	private String project;
	private String directory;


	public RelationParser(GitRepository gitRepo,String project, String directory) {
		this.project = project;
		this.directory = directory;
		this.repository = repository;
		this.repository=gitRepo;
	}
	public static void main(String args[]){
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		RelationParser a=new RelationParser(gitRepository,"camel","D:\\Workspace\\co-change summer\\understand\\camel\\");
		a.extractRelation();
	}

	public void extractRelation() {
		ArrayList<File> files = getListFiles(new File(directory));
		for (File f : files) {
			if (f.getAbsolutePath().endsWith("xml")) {
				System.out.println("Begin parse file " + f.getName());
				parseFile(f);
				// System.exit(0);
			}
		}
		// System.out.println(files.size());
	}

	private void parseFile(File f) {
		
		String[] types={"Extend","Typed","Import","Call","Create","Cast","Implement","Set","Modify","Use","Throw"};
		List<String> typeList = new ArrayList<String>();
		List<String> fileList = new ArrayList<String>(); 
		for(String tmp:types){
			typeList.add(tmp);
		}
		GenerateDSM structureDSM;
		GenerateDSM historyDSM;
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		String fileName = f.getName();
		String[] tokens = fileName.split("-");
		String version = tokens[1];
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(f);
			Element root = document.getRootElement();
			List<Element> nodes = root.elements("node");
			// parse each node tag
			for (Iterator<Element> it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				int id = Integer.parseInt(elm.attribute("id").getValue());
				List<Element> children = elm.elements("att");
				for (Element child : children) {
					Attribute name = child.attribute("name");
					if (name.getValue().equals("longName")) {
						String path = child.attribute("value").getValue();
						// System.out.println(id + "\t" + parseName(path));
						String packageName=FileUtils.parseFilePath(path, this.project);
						hm.put(id, packageName);
						fileList.add(packageName);
					}
					
				}
			}
			Collections.sort(fileList);
			structureDSM=new GenerateDSM(typeList,fileList);
			
			nodes = root.elements("edge");
			for (Iterator<Element> it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				int source = Integer.parseInt(elm.attribute("source").getValue());
				int target = Integer.parseInt(elm.attribute("target").getValue());
				List<Element> children = elm.elements("att");
				for (Element child : children) {
					Attribute name = child.attribute("name");
					if (name.getValue().equals("dependency kind")) {
						String kind = child.attribute("value").getValue();
						String [] mTypeList=kind.split(",");
						List<String> mTypeList2=new ArrayList<String>();
						for(String tmp:mTypeList){
							mTypeList2.add(tmp.trim());
						}
						structureDSM.setFileStructureRelationType(hm.get(source), hm.get(target), mTypeList2);
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		historyDSM=new GenerateDSM(fileList);
		for(int i=0;i<fileList.size();i++)
			for(int j=i+1;j<fileList.size();j++){
				FilePairCount dataList=FilePairCountDAO.selectByRepositoryIdAndFilePairName(this.repository.getRepositoryId(),fileList.get(i)+"||"+fileList.get(j));
				if(dataList==null){
					System.err.println("ERR");
				}
				historyDSM.setFileHistoryRelationCount(fileList.get(i),fileList.get(j), dataList.getCount());
				historyDSM.setFileHistoryRelationCount(fileList.get(j),fileList.get(i), dataList.getCount());
			}
	}

	private ArrayList<File> getListFiles(File directory) {
		ArrayList<File> files = new ArrayList<File>();
		if (directory.isFile()) {
			files.add(directory);
			return files;
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getListFiles(fileOne));
			}
		}
		return files;
	}

	
}
