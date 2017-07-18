package cn.edu.fudan.se.cochange_analysis.file.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateDSM {
	private StringBuilder[][] structureDsmMatrix;
	private int[][] historyDsmMatrix;
	private List<String> typeList;
	private List<String> fileList;
	
	private StringBuilder matrixBuilder;
	private StringBuilder typeBuilder;
	private StringBuilder fileListBuilder;
	
	private StringBuilder matrixCell;
	private Map<String,Integer> typeIndexMap;
	
	public GenerateDSM(List<String> mTypeList,List<String> mFileList){
		
		Collections.sort(mTypeList);
		Collections.sort(mFileList);
		structureDsmMatrix = new StringBuilder[mFileList.size()][mFileList.size()];
		typeList=mTypeList;
		fileList=mFileList;
		matrixBuilder=new StringBuilder();
		typeBuilder=new StringBuilder();
		fileListBuilder=new StringBuilder();
		matrixCell=new StringBuilder();
		typeIndexMap=new HashMap<String,Integer>();
		for(int i=0;i<typeList.size();i++){
			matrixCell.append("0");
			typeIndexMap.put(typeList.get(i),i);
		}
		
		buildString();
	}
	public GenerateDSM(List<String> mFileList){
		
		Collections.sort(mFileList);
		historyDsmMatrix=new int[mFileList.size()][mFileList.size()];
		fileList=mFileList;
		fileListBuilder=new StringBuilder();
		
		for (String fileName : fileList) {
			fileListBuilder.append(fileName + "\n");
		}
	}
	public void setFileStructureRelationType(String fromFileA,String toFileB,List<String> typeList){
		int x=fileList.indexOf(fromFileA);
		int y=fileList.indexOf(toFileB);
		if (structureDsmMatrix[x][y] == null) {
			structureDsmMatrix[x][y] = new StringBuilder(
					matrixCell.toString());
			;
		}
		for(String tmp:typeList){
			structureDsmMatrix[x][y].setCharAt(typeIndexMap.get(tmp), '1');
		}
	}
	public void setFileStructureRelationType(String fromFileA,String toFileB, String type){
		int x=fileList.indexOf(fromFileA);
		int y=fileList.indexOf(toFileB);
		if (structureDsmMatrix[x][y] == null) {
			structureDsmMatrix[x][y] = new StringBuilder(matrixCell.toString());
		}
		structureDsmMatrix[x][y].setCharAt(typeIndexMap.get(type), '1');
	}
	public void setFileHistoryRelationCount(String fromFileA,String toFileB,int count){
		int x=fileList.indexOf(fromFileA);
		int y=fileList.indexOf(toFileB);
		historyDsmMatrix[x][y]=count;
			
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
	
	public void write2StructureDSM(String outputPath){
		for (int m = 0; m < structureDsmMatrix.length; m++) {
			for (int n = 0; n < structureDsmMatrix[0].length; n++) {
				if (structureDsmMatrix[m][n] == null) {
					matrixBuilder.append("0 ");
				} else {
					matrixBuilder.append(structureDsmMatrix[m][n].toString() + " ");
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
	public void write2HistoryDSM(String outputPath){
		for (int m = 0; m < historyDsmMatrix.length; m++) {
			for (int n = 0; n < historyDsmMatrix[0].length; n++) {
					matrixBuilder.append(String.valueOf(historyDsmMatrix[m][n]) + " ");
			}
			matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
			matrixBuilder.append("\n");
		}
		try {
			FileOutputStream fos = new FileOutputStream(
					new File(outputPath));
			String tmp=this.fileList.size()+"\n";
			fos.write(tmp.getBytes());
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
