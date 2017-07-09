package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class FilePairCount extends FilePairCountKey implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_pair_count.count
     *
     * @mbg.generated
     */
    private Integer count;

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
    public FilePairCount(Integer repositoryId, String filePair, Integer count) {
        super(repositoryId, filePair);
        this.count = count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_pair_count
     *
     * @mbg.generated
     */
    public FilePairCount() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_pair_count.count
     *
     * @return the value of file_pair_count.count
     *
     * @mbg.generated
     */
    public Integer getCount() {
        return count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_pair_count.count
     *
     * @param count the value for file_pair_count.count
     *
     * @mbg.generated
     */
    public void setCount(Integer count) {
        this.count = count;
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
        FilePairCount other = (FilePairCount) that;
        return (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getFilePair() == null ? other.getFilePair() == null : this.getFilePair().equals(other.getFilePair()))
            && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()));
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
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
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
        sb.append(", count=").append(count);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}