package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class ChangeRelationCommit implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.change_relation_commit_id
     *
     * @mbg.generated
     */
    private Integer changeRelationCommitId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.repository_id
     *
     * @mbg.generated
     */
    private Integer repositoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.commit_id
     *
     * @mbg.generated
     */
    private Integer commitId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.file_pair
     *
     * @mbg.generated
     */
    private String filePair;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.change_type1
     *
     * @mbg.generated
     */
    private String changeType1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.changed_entity_type1
     *
     * @mbg.generated
     */
    private String changedEntityType1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.change_type2
     *
     * @mbg.generated
     */
    private String changeType2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_commit.changed_entity_type2
     *
     * @mbg.generated
     */
    private String changedEntityType2;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table change_relation_commit
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_commit
     *
     * @mbg.generated
     */
    public ChangeRelationCommit(Integer changeRelationCommitId, Integer repositoryId, Integer commitId, String filePair, String changeType1, String changedEntityType1, String changeType2, String changedEntityType2) {
        this.changeRelationCommitId = changeRelationCommitId;
        this.repositoryId = repositoryId;
        this.commitId = commitId;
        this.filePair = filePair;
        this.changeType1 = changeType1;
        this.changedEntityType1 = changedEntityType1;
        this.changeType2 = changeType2;
        this.changedEntityType2 = changedEntityType2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_commit
     *
     * @mbg.generated
     */
    public ChangeRelationCommit() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.change_relation_commit_id
     *
     * @return the value of change_relation_commit.change_relation_commit_id
     *
     * @mbg.generated
     */
    public Integer getChangeRelationCommitId() {
        return changeRelationCommitId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.change_relation_commit_id
     *
     * @param changeRelationCommitId the value for change_relation_commit.change_relation_commit_id
     *
     * @mbg.generated
     */
    public void setChangeRelationCommitId(Integer changeRelationCommitId) {
        this.changeRelationCommitId = changeRelationCommitId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.repository_id
     *
     * @return the value of change_relation_commit.repository_id
     *
     * @mbg.generated
     */
    public Integer getRepositoryId() {
        return repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.repository_id
     *
     * @param repositoryId the value for change_relation_commit.repository_id
     *
     * @mbg.generated
     */
    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.commit_id
     *
     * @return the value of change_relation_commit.commit_id
     *
     * @mbg.generated
     */
    public Integer getCommitId() {
        return commitId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.commit_id
     *
     * @param commitId the value for change_relation_commit.commit_id
     *
     * @mbg.generated
     */
    public void setCommitId(Integer commitId) {
        this.commitId = commitId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.file_pair
     *
     * @return the value of change_relation_commit.file_pair
     *
     * @mbg.generated
     */
    public String getFilePair() {
        return filePair;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.file_pair
     *
     * @param filePair the value for change_relation_commit.file_pair
     *
     * @mbg.generated
     */
    public void setFilePair(String filePair) {
        this.filePair = filePair == null ? null : filePair.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.change_type1
     *
     * @return the value of change_relation_commit.change_type1
     *
     * @mbg.generated
     */
    public String getChangeType1() {
        return changeType1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.change_type1
     *
     * @param changeType1 the value for change_relation_commit.change_type1
     *
     * @mbg.generated
     */
    public void setChangeType1(String changeType1) {
        this.changeType1 = changeType1 == null ? null : changeType1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.changed_entity_type1
     *
     * @return the value of change_relation_commit.changed_entity_type1
     *
     * @mbg.generated
     */
    public String getChangedEntityType1() {
        return changedEntityType1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.changed_entity_type1
     *
     * @param changedEntityType1 the value for change_relation_commit.changed_entity_type1
     *
     * @mbg.generated
     */
    public void setChangedEntityType1(String changedEntityType1) {
        this.changedEntityType1 = changedEntityType1 == null ? null : changedEntityType1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.change_type2
     *
     * @return the value of change_relation_commit.change_type2
     *
     * @mbg.generated
     */
    public String getChangeType2() {
        return changeType2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.change_type2
     *
     * @param changeType2 the value for change_relation_commit.change_type2
     *
     * @mbg.generated
     */
    public void setChangeType2(String changeType2) {
        this.changeType2 = changeType2 == null ? null : changeType2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_commit.changed_entity_type2
     *
     * @return the value of change_relation_commit.changed_entity_type2
     *
     * @mbg.generated
     */
    public String getChangedEntityType2() {
        return changedEntityType2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_commit.changed_entity_type2
     *
     * @param changedEntityType2 the value for change_relation_commit.changed_entity_type2
     *
     * @mbg.generated
     */
    public void setChangedEntityType2(String changedEntityType2) {
        this.changedEntityType2 = changedEntityType2 == null ? null : changedEntityType2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_commit
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChangeRelationCommit other = (ChangeRelationCommit) that;
        return (this.getChangeRelationCommitId() == null ? other.getChangeRelationCommitId() == null : this.getChangeRelationCommitId().equals(other.getChangeRelationCommitId()))
            && (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getCommitId() == null ? other.getCommitId() == null : this.getCommitId().equals(other.getCommitId()))
            && (this.getFilePair() == null ? other.getFilePair() == null : this.getFilePair().equals(other.getFilePair()))
            && (this.getChangeType1() == null ? other.getChangeType1() == null : this.getChangeType1().equals(other.getChangeType1()))
            && (this.getChangedEntityType1() == null ? other.getChangedEntityType1() == null : this.getChangedEntityType1().equals(other.getChangedEntityType1()))
            && (this.getChangeType2() == null ? other.getChangeType2() == null : this.getChangeType2().equals(other.getChangeType2()))
            && (this.getChangedEntityType2() == null ? other.getChangedEntityType2() == null : this.getChangedEntityType2().equals(other.getChangedEntityType2()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_commit
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getChangeRelationCommitId() == null) ? 0 : getChangeRelationCommitId().hashCode());
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getCommitId() == null) ? 0 : getCommitId().hashCode());
        result = prime * result + ((getFilePair() == null) ? 0 : getFilePair().hashCode());
        result = prime * result + ((getChangeType1() == null) ? 0 : getChangeType1().hashCode());
        result = prime * result + ((getChangedEntityType1() == null) ? 0 : getChangedEntityType1().hashCode());
        result = prime * result + ((getChangeType2() == null) ? 0 : getChangeType2().hashCode());
        result = prime * result + ((getChangedEntityType2() == null) ? 0 : getChangedEntityType2().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_commit
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", changeRelationCommitId=").append(changeRelationCommitId);
        sb.append(", repositoryId=").append(repositoryId);
        sb.append(", commitId=").append(commitId);
        sb.append(", filePair=").append(filePair);
        sb.append(", changeType1=").append(changeType1);
        sb.append(", changedEntityType1=").append(changedEntityType1);
        sb.append(", changeType2=").append(changeType2);
        sb.append(", changedEntityType2=").append(changedEntityType2);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}