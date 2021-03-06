package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class ChangeRelationCount implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_count.change_relation_count_id
     *
     * @mbg.generated
     */
    private Integer changeRelationCountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_count.repository_id
     *
     * @mbg.generated
     */
    private Integer repositoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_count.file_pair
     *
     * @mbg.generated
     */
    private String filePair;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_count.change_type1
     *
     * @mbg.generated
     */
    private String changeType1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_count.change_type2
     *
     * @mbg.generated
     */
    private String changeType2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_relation_count.count
     *
     * @mbg.generated
     */
    private Integer count;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    public ChangeRelationCount(Integer changeRelationCountId, Integer repositoryId, String filePair, String changeType1, String changeType2, Integer count) {
        this.changeRelationCountId = changeRelationCountId;
        this.repositoryId = repositoryId;
        this.filePair = filePair;
        this.changeType1 = changeType1;
        this.changeType2 = changeType2;
        this.count = count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    public ChangeRelationCount() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_count.change_relation_count_id
     *
     * @return the value of change_relation_count.change_relation_count_id
     *
     * @mbg.generated
     */
    public Integer getChangeRelationCountId() {
        return changeRelationCountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_count.change_relation_count_id
     *
     * @param changeRelationCountId the value for change_relation_count.change_relation_count_id
     *
     * @mbg.generated
     */
    public void setChangeRelationCountId(Integer changeRelationCountId) {
        this.changeRelationCountId = changeRelationCountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_count.repository_id
     *
     * @return the value of change_relation_count.repository_id
     *
     * @mbg.generated
     */
    public Integer getRepositoryId() {
        return repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_count.repository_id
     *
     * @param repositoryId the value for change_relation_count.repository_id
     *
     * @mbg.generated
     */
    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_count.file_pair
     *
     * @return the value of change_relation_count.file_pair
     *
     * @mbg.generated
     */
    public String getFilePair() {
        return filePair;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_count.file_pair
     *
     * @param filePair the value for change_relation_count.file_pair
     *
     * @mbg.generated
     */
    public void setFilePair(String filePair) {
        this.filePair = filePair == null ? null : filePair.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_count.change_type1
     *
     * @return the value of change_relation_count.change_type1
     *
     * @mbg.generated
     */
    public String getChangeType1() {
        return changeType1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_count.change_type1
     *
     * @param changeType1 the value for change_relation_count.change_type1
     *
     * @mbg.generated
     */
    public void setChangeType1(String changeType1) {
        this.changeType1 = changeType1 == null ? null : changeType1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_count.change_type2
     *
     * @return the value of change_relation_count.change_type2
     *
     * @mbg.generated
     */
    public String getChangeType2() {
        return changeType2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_count.change_type2
     *
     * @param changeType2 the value for change_relation_count.change_type2
     *
     * @mbg.generated
     */
    public void setChangeType2(String changeType2) {
        this.changeType2 = changeType2 == null ? null : changeType2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_relation_count.count
     *
     * @return the value of change_relation_count.count
     *
     * @mbg.generated
     */
    public Integer getCount() {
        return count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_relation_count.count
     *
     * @param count the value for change_relation_count.count
     *
     * @mbg.generated
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
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
        ChangeRelationCount other = (ChangeRelationCount) that;
        return (this.getChangeRelationCountId() == null ? other.getChangeRelationCountId() == null : this.getChangeRelationCountId().equals(other.getChangeRelationCountId()))
            && (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getFilePair() == null ? other.getFilePair() == null : this.getFilePair().equals(other.getFilePair()))
            && (this.getChangeType1() == null ? other.getChangeType1() == null : this.getChangeType1().equals(other.getChangeType1()))
            && (this.getChangeType2() == null ? other.getChangeType2() == null : this.getChangeType2().equals(other.getChangeType2()))
            && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getChangeRelationCountId() == null) ? 0 : getChangeRelationCountId().hashCode());
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getFilePair() == null) ? 0 : getFilePair().hashCode());
        result = prime * result + ((getChangeType1() == null) ? 0 : getChangeType1().hashCode());
        result = prime * result + ((getChangeType2() == null) ? 0 : getChangeType2().hashCode());
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", changeRelationCountId=").append(changeRelationCountId);
        sb.append(", repositoryId=").append(repositoryId);
        sb.append(", filePair=").append(filePair);
        sb.append(", changeType1=").append(changeType1);
        sb.append(", changeType2=").append(changeType2);
        sb.append(", count=").append(count);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}