package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class FilePairCountKey implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_pair_count.repository_id
     *
     * @mbg.generated
     */
    private Integer repositoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_pair_count.file_pair
     *
     * @mbg.generated
     */
    private String filePair;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table file_pair_count
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_pair_count
     *
     * @mbg.generated
     */
    public FilePairCountKey(Integer repositoryId, String filePair) {
        this.repositoryId = repositoryId;
        this.filePair = filePair;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_pair_count
     *
     * @mbg.generated
     */
    public FilePairCountKey() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_pair_count.repository_id
     *
     * @return the value of file_pair_count.repository_id
     *
     * @mbg.generated
     */
    public Integer getRepositoryId() {
        return repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_pair_count.repository_id
     *
     * @param repositoryId the value for file_pair_count.repository_id
     *
     * @mbg.generated
     */
    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_pair_count.file_pair
     *
     * @return the value of file_pair_count.file_pair
     *
     * @mbg.generated
     */
    public String getFilePair() {
        return filePair;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_pair_count.file_pair
     *
     * @param filePair the value for file_pair_count.file_pair
     *
     * @mbg.generated
     */
    public void setFilePair(String filePair) {
        this.filePair = filePair == null ? null : filePair.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_pair_count
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
        FilePairCountKey other = (FilePairCountKey) that;
        return (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getFilePair() == null ? other.getFilePair() == null : this.getFilePair().equals(other.getFilePair()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_pair_count
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getFilePair() == null) ? 0 : getFilePair().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_pair_count
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", repositoryId=").append(repositoryId);
        sb.append(", filePair=").append(filePair);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}