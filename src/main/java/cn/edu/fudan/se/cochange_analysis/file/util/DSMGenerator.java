package cn.edu.fudan.se.cochange_analysis.file.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DSMGenerator {
	private StringBuilder[][] dsmMatrix;
	private List<String> typeList;
	private List<String> fileList;
	
	private StringBuilder matrixBuilder;
	private StringBuilder typeBuilder;
	private StringBuilder fileListBuilder;
	
	public DSMGenerator(int size,ArrayList<String> mTypeList,List<String> mFileList){
		dsmMatrix = new StringBuilder[size][size];
		typeList=mTypeList;
		fileList=mFileList;
		matrixBuilder=new StringBuilder();
		typeBuilder=new StringBuilder();
		fileListBuilder=new StringBuilder();
		buildString();
	}
	public StringBuilder[][] getDsmMatrix() {
		return dsmMatrix;
	}
	
	private void buildString(){
		typeBuilder.append("[");
		for (int m = 0; m < typeList.size(); m++) {
			typeBuilder.append(typeList.get(m) + ",");
		}
		typeBuilder.deleteCharAt(typeBuilder.length() - 1);
		typeBuilder.append("]\n");
		typeBuilder.append(fileList.size());
		typeBuilder.append("\n");
		
		
		for (String fileName : fileList) {
			fileListBuilder.append(fileName + "\n");
		}
	}
	
	public void write2DSM(String outputPath){
		for (int m = 0; m < dsmMatrix.length; m++) {
			for (int n = 0; n < dsmMatrix[0].length; n++) {
				if (dsmMatrix[m][n] == null) {
					matrixBuilder.append("0 ");
				} else {
					matrixBuilder.append(dsmMatrix[m][n].toString() + " ");
				}
			}
			matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
			matrixBuilder.append("\n");
		}
		try {
			FileOutputStream fos = new FileOutputStream(
					new File(outputPath));
			fos.write(typeBuilder.toString().getBytes());
			fos.write(matrixBuilder.toString().getBytes());
			fos.write(fileListBuilder.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
