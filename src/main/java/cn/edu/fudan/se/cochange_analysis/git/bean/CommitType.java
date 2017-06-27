package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class CommitType extends CommitTypeKey implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column commit_type.commit_type
     *
     * @mbg.generated
     */
    private String commitType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table commit_type
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_type
     *
     * @mbg.generated
     */
    public CommitType(Integer repositoryId, String commitId, String commitType) {
        super(repositoryId, commitId);
        this.commitType = commitType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_type
     *
     * @mbg.generated
     */
    public CommitType() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column commit_type.commit_type
     *
     * @return the value of commit_type.commit_type
     *
     * @mbg.generated
     */
    public String getCommitType() {
        return commitType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column commit_type.commit_type
     *
     * @param commitType the value for commit_type.commit_type
     *
     * @mbg.generated
     */
    public void setCommitType(String commitType) {
        this.commitType = commitType == null ? null : commitType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_type
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
        CommitType other = (CommitType) that;
        return (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getCommitId() == null ? other.getCommitId() == null : this.getCommitId().equals(other.getCommitId()))
            && (this.getCommitType() == null ? other.getCommitType() == null : this.getCommitType().equals(other.getCommitType()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_type
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getCommitId() == null) ? 0 : getCommitId().hashCode());
        result = prime * result + ((getCommitType() == null) ? 0 : getCommitType().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_type
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", commitType=").append(commitType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}