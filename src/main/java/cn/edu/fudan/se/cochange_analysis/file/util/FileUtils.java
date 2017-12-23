/**   
 * Copyright © 2017 Software Engineering Lab, Fudan University. All rights reserved.
 * @Project: change-extractor
 * @author: echo   
 * @date: May 23, 2017 5:04:48 PM 
 */
package cn.edu.fudan.se.cochange_analysis.file.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * @ClassName: FileUtils
 * @Description: TODO
 * @author: echo
 * @date: May 23, 2017 5:04:48 PM
 */
public class FileUtils {
	public static void main(String[] args) {
		String text = "src/main/java/org/apache/hadoop/hbase/regionserver/TestCompactSplitThread.java";
		System.out.println(FileUtils.isTestFile(text));
	}

	public static File writeBytesToFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(filePath + "/" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	public static String parseFilePath(String filePath, String repositoryName) {
		if (repositoryName.equals("wicket")) {
			filePath = filePath.replaceFirst("src/java/wicket", "org/apache/wicket");
			filePath = filePath.replaceFirst("src/main/java/wicket", "org/apache/wicket");
		}

		if (repositoryName.equals("hbase")) {
			if (!filePath.contains("org/apache/hadoop/hbase")) {
				filePath = filePath.replaceFirst("org/apache/hadoop", "org/apache/hadoop/hbase");
				filePath = filePath.replaceFirst("org/apache/hbase", "org/apache/hadoop/hbase");
			}
		}

		String fileName = null;
		String matchString1 = "org/apache/" + repositoryName;
		if (repositoryName.equals("hbase"))
			matchString1 = "org/apache/hadoop/" + repositoryName;

		String matchString2 = "src/main/java/";
		String matchString3 = "src/main/resources/";

		// search matchString1
		int index = filePath.indexOf(matchString1);
		// found matchString1
		if (index != -1) {
			fileName = filePath.substring(index);
		} else {
			// matchString1 not found, search matchString2
			index = filePath.indexOf(matchString2);
			// found matchString2
			if (index != -1) {
				fileName = filePath.substring(index + matchString2.length());
			} else {
				// matchString2 not found, search matchString3
				index = filePath.indexOf(matchString3);
				// matchString3 found
				if (index != -1) {
					fileName = filePath.substring(index + matchString3.length());
				} else {
					// matchString3 not found, keep original path
					fileName = filePath;
				}
			}
		}
		return fileName;
	}

	public static void analyzeChangeRelationOverlap(String outputdir) {
		try {
			Map<String, Integer> result = new HashMap<String, Integer>();
			String[] files = { "camel", "cassandra", "cxf", "hadoop", "hbase", "wicket" };
			for (String tmp : files) {
				FileInputStream fis = new FileInputStream(
						new File(outputdir + "/" + tmp + "_change-relation-count-rank.csv"));
				Scanner sc = new Scanner(fis);
				for (int i = 0; i < 32; i++) {
					String line = sc.nextLine();
					String[] content = line.split(",");
					if (result.containsKey(content[0])) {
						result.put(content[0], result.get(content[0]) + 1);
					} else {
						result.put(content[0], 1);
					}
				}
			}
			List<Entry<String, Integer>> mapList = new ArrayList<Entry<String, Integer>>(result.entrySet());
			Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
					return (o2.getValue() - o1.getValue());
				}
			});
			FileOutputStream fos = new FileOutputStream(new File(outputdir + "/ChangeRelationOverlap.csv"));
			for (Entry<String, Integer> tmp : mapList) {
				String t = tmp.getKey() + ", " + tmp.getValue() + ",\n";
				fos.write(t.getBytes());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isTestFile(String filePath) {
		// path contain test/, tester/, testing/, testng/, tests/, testutils/,
		// testutil/, testcase/
		if (filePath.contains("test/") || filePath.contains("tester/") || filePath.contains("testng/")
				|| filePath.contains("testing/") || filePath.contains("tests/") || filePath.contains("testutils/")
				|| filePath.contains("testutil/") || filePath.contains("testcase/"))
			return true;

		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		// System.out.println(fileName);
		if (fileName.contains("Test"))
			return true;
		return false;
	}

	public static String parseModifier(String name) {
		return name.replace("_", "").toLowerCase();
	}
}
