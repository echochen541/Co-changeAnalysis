package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;
import java.util.Date;

public class IssueBug extends IssueBugKey implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.summary
     *
     * @mbg.generated
     */
    private String summary;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.assignee
     *
     * @mbg.generated
     */
    private String assignee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.reporter
     *
     * @mbg.generated
     */
    private String reporter;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.status
     *
     * @mbg.generated
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.resolution
     *
     * @mbg.generated
     */
    private String resolution;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.created
     *
     * @mbg.generated
     */
    private Date created;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_bug.updated
     *
     * @mbg.generated
     */
    private Date updated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    public IssueBug(Integer repositoryId, String key, String summary, String assignee, String reporter, String status, String resolution, Date created, Date updated) {
        super(repositoryId, key);
        this.summary = summary;
        this.assignee = assignee;
        this.reporter = reporter;
        this.status = status;
        this.resolution = resolution;
        this.created = created;
        this.updated = updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    public IssueBug() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.summary
     *
     * @return the value of issue_bug.summary
     *
     * @mbg.generated
     */
    public String getSummary() {
        return summary;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.summary
     *
     * @param summary the value for issue_bug.summary
     *
     * @mbg.generated
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.assignee
     *
     * @return the value of issue_bug.assignee
     *
     * @mbg.generated
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.assignee
     *
     * @param assignee the value for issue_bug.assignee
     *
     * @mbg.generated
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee == null ? null : assignee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.reporter
     *
     * @return the value of issue_bug.reporter
     *
     * @mbg.generated
     */
    public String getReporter() {
        return reporter;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.reporter
     *
     * @param reporter the value for issue_bug.reporter
     *
     * @mbg.generated
     */
    public void setReporter(String reporter) {
        this.reporter = reporter == null ? null : reporter.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.status
     *
     * @return the value of issue_bug.status
     *
     * @mbg.generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.status
     *
     * @param status the value for issue_bug.status
     *
     * @mbg.generated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.resolution
     *
     * @return the value of issue_bug.resolution
     *
     * @mbg.generated
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.resolution
     *
     * @param resolution the value for issue_bug.resolution
     *
     * @mbg.generated
     */
    public void setResolution(String resolution) {
        this.resolution = resolution == null ? null : resolution.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.created
     *
     * @return the value of issue_bug.created
     *
     * @mbg.generated
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.created
     *
     * @param created the value for issue_bug.created
     *
     * @mbg.generated
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_bug.updated
     *
     * @return the value of issue_bug.updated
     *
     * @mbg.generated
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_bug.updated
     *
     * @param updated the value for issue_bug.updated
     *
     * @mbg.generated
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
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
        IssueBug other = (IssueBug) that;
        return (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getKey() == null ? other.getKey() == null : this.getKey().equals(other.getKey()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getAssignee() == null ? other.getAssignee() == null : this.getAssignee().equals(other.getAssignee()))
            && (this.getReporter() == null ? other.getReporter() == null : this.getReporter().equals(other.getReporter()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getResolution() == null ? other.getResolution() == null : this.getResolution().equals(other.getResolution()))
            && (this.getCreated() == null ? other.getCreated() == null : this.getCreated().equals(other.getCreated()))
            && (this.getUpdated() == null ? other.getUpdated() == null : this.getUpdated().equals(other.getUpdated()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getKey() == null) ? 0 : getKey().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getAssignee() == null) ? 0 : getAssignee().hashCode());
        result = prime * result + ((getReporter() == null) ? 0 : getReporter().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getResolution() == null) ? 0 : getResolution().hashCode());
        result = prime * result + ((getCreated() == null) ? 0 : getCreated().hashCode());
        result = prime * result + ((getUpdated() == null) ? 0 : getUpdated().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", summary=").append(summary);
        sb.append(", assignee=").append(assignee);
        sb.append(", reporter=").append(reporter);
        sb.append(", status=").append(status);
        sb.append(", resolution=").append(resolution);
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}