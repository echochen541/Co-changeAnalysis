package cn.edu.fudan.se.cochange_analysis.shared.dependency;

public enum ChangeType {
	ADD_METHOD("ADDITIONAL_FUNCTIONALITY","METHOD");
	
	
	private String changeType;
	private String changedEntityType;
	
	private ChangeType(String changeType, String changedEntityType) {
		this.changeType = changeType;
		this.changedEntityType = changedEntityType;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangedEntityType() {
		return changedEntityType;
	}

	public void setChangedEntityType(String changedEntityType) {
		this.changedEntityType = changedEntityType;
	}
}
