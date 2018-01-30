package cn.edu.fudan.se.cochange_analysis.detector;

import java.util.List;

public class Hotspot {
	private String coreFile;
	private List<String> fileList;

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
}
