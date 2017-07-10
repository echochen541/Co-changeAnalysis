package cn.edu.fudan.se.cochange_analysis.git.bean;

public class ChangeRelationUnique {
	String changeType1;
	String changeType2;
	String changedEntityType1;
	String changedEntityType2;
	
	public ChangeRelationUnique(String changeType1, String changedEntityType1, String changeType2,
			String changedEntityType2) {
		super();
		this.changeType1 = changeType1;
		this.changedEntityType1 = changedEntityType1;
		this.changeType2 = changeType2;
		this.changedEntityType2 = changedEntityType2;
	}
	public String getChangeType1() {
		return changeType1;
	}
	public void setChangeType1(String changeType1) {
		this.changeType1 = changeType1;
	}
	public String getChangeType2() {
		return changeType2;
	}
	public void setChangeType2(String changeType2) {
		this.changeType2 = changeType2;
	}
	public String getChangedEntityType1() {
		return changedEntityType1;
	}
	public void setChangedEntityType1(String changedEntityType1) {
		this.changedEntityType1 = changedEntityType1;
	}
	public String getChangedEntityType2() {
		return changedEntityType2;
	}
	public void setChangedEntityType2(String changedEntityType2) {
		this.changedEntityType2 = changedEntityType2;
	}
	

}
