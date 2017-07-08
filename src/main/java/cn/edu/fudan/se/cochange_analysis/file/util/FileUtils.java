/**   
 * Copyright © 2017 Software Engineering Lab, Fudan University. All rights reserved.
 * @Project: change-extractor
 * @author: echo   
 * @date: May 23, 2017 5:04:48 PM 
 */
package cn.edu.fudan.se.cochange_analysis.file.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName: FileUtils
 * @Description: TODO
 * @author: echo
 * @date: May 23, 2017 5:04:48 PM
 */
public class FileUtils {
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
		String fileName = null;
		String matchString1 = "org/apache/" + repositoryName;
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
				fileName = filePath.substring(index);
			} else {
				// matchString2 not found, search matchString3
				index = filePath.indexOf(matchString3);
				// matchString3 found
				if (index != -1) {
					fileName = filePath.substring(index);
				} else {
					// matchString1,2,3 not found, keep original path
					fileName = filePath;
				}
			}
		}
		return fileName;
	}
}
