package cn.edu.fudan.se.cochange_analysis.detector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HotspotModel {
	private String coreFile;
	private List<String> fileList;

	public HotspotModel(String coreFile, List<String> fileList) {
		this.coreFile = coreFile;
		this.fileList = fileList;
	}

	public HotspotModel() {
		// TODO Auto-generated constructor stub
	}

	public String getCoreFile() {
		return coreFile;
	}

	public void setCoreFile(String coreFile) {
		this.coreFile = coreFile;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public List<String> getTotalList() {
		List<String> totalList = new ArrayList<String>();
		totalList.add(coreFile);
		totalList.addAll(fileList);
		Collections.sort(totalList);
		return totalList;
	}

	public int getSize() {
		return 1 + fileList.size();
	}

	@Override
	public String toString() {
		return "Hotspot [coreFile=" + coreFile + ", fileList=" + fileList + "]";
	}
}
