package cn.edu.fudan.se.cochange_analysis.git.bean;

public class UniqueChangeOperation {

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

	public UniqueChangeOperation(String changeType, String changedEntityType) {
		super();
		this.changeType = changeType;
		this.changedEntityType = changedEntityType;
	}
}
