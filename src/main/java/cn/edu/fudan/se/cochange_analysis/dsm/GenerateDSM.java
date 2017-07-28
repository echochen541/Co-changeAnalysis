package cn.edu.fudan.se.cochange_analysis.dsm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateDSM <T> {
	private Class<T> type;
	private StringBuilder[][] stringDataMatrix;
	private int[][] intDataMatrix;
	private List<String> typeList;
	private List<String> fileList;

	private StringBuilder matrixBuilder;
	private StringBuilder typeBuilder;
	private StringBuilder fileListBuilder;

	private StringBuilder matrixCell;
	
	public GenerateDSM(Class<T> type,List<String> mTypeList, List<String> mFileList) {
		this.type=type;
		if(type.equals(Integer.class)){
			stringDataMatrix = new StringBuilder[mFileList.size()][mFileList.size()];
			matrixCell = new StringBuilder();
			for (int i = 0; i < typeList.size(); i++) {
				matrixCell.append("0");
			}
		}else if(type.equals(String.class)){
			intDataMatrix=new int[mFileList.size()][mFileList.size()];
		}
		typeList = mTypeList;
		fileList = mFileList;
		matrixBuilder = new StringBuilder();
		typeBuilder = new StringBuilder();
		fileListBuilder = new StringBuilder();
		
	}

//	public GenerateDSM(List<String> mTypeList, List<String> mFileList) {
//		// Collections.sort(mTypeList);
//		// Collections.sort(mFileList);
//		stringDataMatrix = new StringBuilder[mFileList.size()][mFileList.size()];
//		
//		typeList = mTypeList;
//		fileList = mFileList;
//		matrixBuilder = new StringBuilder();
//		typeBuilder = new StringBuilder();
//		fileListBuilder = new StringBuilder();
//		matrixCell = new StringBuilder();
//		for (int i = 0; i < typeList.size(); i++) {
//			matrixCell.append("0");
//		}
//		buildString();
//	}

//	public GenerateDSM(List<String> mFileList) {
//		// Collections.sort(mFileList);
//		intDataMatrix = new int[mFileList.size()][mFileList.size()];
//		fileList = mFileList;
//		fileListBuilder = new StringBuilder();
//
//		for (String fileName : fileList) {
//			fileListBuilder.append(fileName + "\n");
//		}
//	}
	private void setMatrixRelation(int x,int y,Object content1){
		if(content1 instanceof String){
			String content= (String) content1;
			if (stringDataMatrix[x][y] == null) {
				stringDataMatrix[x][y] = new StringBuilder(matrixCell.toString());
			}
			stringDataMatrix[x][y].setCharAt(this.typeList.indexOf(content), '1');
		}else {
			Integer tmp=(Integer) content1;
			intDataMatrix[x][y] = tmp.intValue();
		}
	}

	public void setFileStructureRelationType(String fromFileA, String toFileB, List<String> typeList2) {
		// System.out.println(fromFileA + "," + toFileB + "," + typeList2);
		int x = fileList.indexOf(fromFileA);
		int y = fileList.indexOf(toFileB);
		for (String tmp : typeList2) {
			this.setMatrixRelation(x, y, tmp);
		}
//		if (stringDataMatrix[x][y] == null) {
//			stringDataMatrix[x][y] = new StringBuilder(matrixCell.toString());
//		}
//		for (String tmp : typeList2) {
//			stringDataMatrix[x][y].setCharAt(this.typeList.indexOf(tmp), '1');
//		}
		// System.out.println(structureDsmMatrix[x][y]);
	}

	public void setFileStructureRelationType(String fromFileA, String toFileB, String type) {
		int x = fileList.indexOf(fromFileA);
		int y = fileList.indexOf(toFileB);
		this.setMatrixRelation(x, y, type);
//		if (stringDataMatrix[x][y] == null) {
//			stringDataMatrix[x][y] = new StringBuilder(matrixCell.toString());
//		}
//		stringDataMatrix[x][y].setCharAt(this.typeList.indexOf(type), '1');
	}

	public void setFileHistoryRelationCount(String fromFileA, String toFileB, int count) {
		int x = fileList.indexOf(fromFileA);
		int y = fileList.indexOf(toFileB);
		if (x == -1 || y == -1) {
			return;
		}
		// System.out.println(fileList.get(x) + " , " + fileList.get(y));
		intDataMatrix[x][y] = count;
		// System.out.println(historyDsmMatrix[x][y]);
	}

	private void buildString() {
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

	public void write2StructureDSM(String outputPath) {
		buildString();
		try {
			FileOutputStream fos = new FileOutputStream(new File(outputPath));
			fos.write(typeBuilder.toString().getBytes());
			for (int m = 0; m < stringDataMatrix.length; m++) {
				matrixBuilder = new StringBuilder();
				for (int n = 0; n < stringDataMatrix[0].length; n++) {
					if (stringDataMatrix[m][n] == null) {
						matrixBuilder.append("0 ");
					} else {
						matrixBuilder.append(stringDataMatrix[m][n].toString() + " ");
					}
				}
				matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
				matrixBuilder.append("\n");
				fos.write(matrixBuilder.toString().getBytes());
			}
			fos.write(fileListBuilder.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write2HistoryDSM(String outputPath) {
		for (String fileName : fileList) {
			fileListBuilder.append(fileName + "\n");
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(outputPath));
			String tmp = this.fileList.size() + "\n";
			fos.write(tmp.getBytes());
			for (int m = 0; m < intDataMatrix.length; m++) {
				matrixBuilder = new StringBuilder();
				for (int n = 0; n < intDataMatrix[0].length; n++) {
					matrixBuilder.append(String.valueOf(intDataMatrix[m][n]) + " ");
				}
				matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
				matrixBuilder.append("\n");
				fos.write(matrixBuilder.toString().getBytes());
			}
			fos.write(fileListBuilder.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
