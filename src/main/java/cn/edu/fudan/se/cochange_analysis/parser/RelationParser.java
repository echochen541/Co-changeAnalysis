package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

//import cn.edu.fudan.se.constant.MySqlConstants;

public class RelationParser {
	private String project;
	private String directory;

	Connection conn = null;
	Statement stmt;

	public RelationParser(String project, String directory) {
		this.project = project;
		this.directory = directory;

		try {
			Class.forName(MySqlConstants.DRIVER);
			conn = DriverManager.getConnection(MySqlConstants.URL, MySqlConstants.USER, MySqlConstants.PASSWORD);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		String fileName = f.getName();
		String[] tokens = fileName.split("-");
		String version = tokens[1];
		// if (version.equals("2.0.8")||version.equals("2.1.2")) {
		// System.out.println(version+" Begin");
		// System.out.println(version+" End");
		// return;
		// }
		// System.out.println(this.project + "\t" + version);
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
						hm.put(id, parseName(path));
					}
				}
			}

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
						// System.out.println(this.project + "\t" + version +
						// "\t" + hm.get(source) + "\t" + kind + "\t"
						// + hm.get(target));
						String sql = "insert into structure_relation values('" + this.project + "','" + hm.get(source)
								+ "','" + hm.get(target) + "','" + version + "','" + kind + "')";
						// System.out.println(sql);
						try {
							stmt.execute(sql);
						} catch (Exception e) {
							System.out.println(sql);
							e.printStackTrace();
						}
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// System.out.println(version + " End");
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

	public String parseName(String fileName) {
		int index = fileName.indexOf("org\\apache\\cassandra\\");
		return (fileName.substring(index, fileName.length())).replaceAll(".java", "_java").replaceAll("\\\\", ".");
	}
}
