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

import cn.edu.fudan.se.cochange_analysis.file.util.DSMGenerator;
import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;


public class RelationParser {
	private String project;
	private String directory;


	public RelationParser(String project, String directory) {
		this.project = project;
		this.directory = directory;
	}
	public static void main(String args[]){
		RelationParser a=new RelationParser("camel","D:\\Workspace\\co-change summer\\understand\\camel\\");
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
		DSMGenerator dsmGenerator;
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
			dsmGenerator=new DSMGenerator(typeList,fileList);
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
						dsmGenerator.setFileRelationType(hm.get(source), hm.get(target), mTypeList2);
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
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
