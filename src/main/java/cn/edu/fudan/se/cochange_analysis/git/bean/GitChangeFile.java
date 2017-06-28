package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class GitChangeFile extends GitChangeFileKey implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_file.old_path
     *
     * @mbg.generated
     */
    private String oldPath;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column change_file.new_path
     *
     * @mbg.generated
     */
    private String newPath;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table change_file
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_file
     *
     * @mbg.generated
     */
    public GitChangeFile(Integer repositoryId, String commitId, String fileName, String changeType, String oldPath, String newPath) {
        super(repositoryId, commitId, fileName, changeType);
        this.oldPath = oldPath;
        this.newPath = newPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_file
     *
     * @mbg.generated
     */
    public GitChangeFile() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_file.old_path
     *
     * @return the value of change_file.old_path
     *
     * @mbg.generated
     */
    public String getOldPath() {
        return oldPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_file.old_path
     *
     * @param oldPath the value for change_file.old_path
     *
     * @mbg.generated
     */
    public void setOldPath(String oldPath) {
        this.oldPath = oldPath == null ? null : oldPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column change_file.new_path
     *
     * @return the value of change_file.new_path
     *
     * @mbg.generated
     */
    public String getNewPath() {
        return newPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column change_file.new_path
     *
     * @param newPath the value for change_file.new_path
     *
     * @mbg.generated
     */
    public void setNewPath(String newPath) {
        this.newPath = newPath == null ? null : newPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_file
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
        GitChangeFile other = (GitChangeFile) that;
        return (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getCommitId() == null ? other.getCommitId() == null : this.getCommitId().equals(other.getCommitId()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getChangeType() == null ? other.getChangeType() == null : this.getChangeType().equals(other.getChangeType()))
            && (this.getOldPath() == null ? other.getOldPath() == null : this.getOldPath().equals(other.getOldPath()))
            && (this.getNewPath() == null ? other.getNewPath() == null : this.getNewPath().equals(other.getNewPath()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_file
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getCommitId() == null) ? 0 : getCommitId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getChangeType() == null) ? 0 : getChangeType().hashCode());
        result = prime * result + ((getOldPath() == null) ? 0 : getOldPath().hashCode());
        result = prime * result + ((getNewPath() == null) ? 0 : getNewPath().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_file
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", oldPath=").append(oldPath);
        sb.append(", newPath=").append(newPath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}