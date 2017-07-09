package cn.edu.fudan.se.cochange_analysis.git.bean;

public class ChangeOperationUnique {

	private String changeType;

	private String changedEntityType;

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

	public ChangeOperationUnique(String changeType, String changedEntityType) {
		super();
		this.changeType = changeType;
		this.changedEntityType = changedEntityType;
	}

}
