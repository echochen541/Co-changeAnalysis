package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class OriginCluster implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.cluster_threshold_id
     *
     * @mbg.generated
     */
    private Integer clusterThresholdId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.repository_id
     *
     * @mbg.generated
     */
    private Integer repositoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.cluster_id
     *
     * @mbg.generated
     */
    private Integer clusterId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.cluster_size
     *
     * @mbg.generated
     */
    private Double clusterSize;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.impact_num
     *
     * @mbg.generated
     */
    private Double impactNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.file_name
     *
     * @mbg.generated
     */
    private String fileName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column origin_cluster.file_type
     *
     * @mbg.generated
     */
    private Integer fileType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table origin_cluster
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table origin_cluster
     *
     * @mbg.generated
     */
    public OriginCluster(Integer clusterThresholdId, Integer repositoryId, Integer clusterId, Double clusterSize, Double impactNum, String fileName, Integer fileType) {
        this.clusterThresholdId = clusterThresholdId;
        this.repositoryId = repositoryId;
        this.clusterId = clusterId;
        this.clusterSize = clusterSize;
        this.impactNum = impactNum;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table origin_cluster
     *
     * @mbg.generated
     */
    public OriginCluster() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.cluster_threshold_id
     *
     * @return the value of origin_cluster.cluster_threshold_id
     *
     * @mbg.generated
     */
    public Integer getClusterThresholdId() {
        return clusterThresholdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.cluster_threshold_id
     *
     * @param clusterThresholdId the value for origin_cluster.cluster_threshold_id
     *
     * @mbg.generated
     */
    public void setClusterThresholdId(Integer clusterThresholdId) {
        this.clusterThresholdId = clusterThresholdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.repository_id
     *
     * @return the value of origin_cluster.repository_id
     *
     * @mbg.generated
     */
    public Integer getRepositoryId() {
        return repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.repository_id
     *
     * @param repositoryId the value for origin_cluster.repository_id
     *
     * @mbg.generated
     */
    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.cluster_id
     *
     * @return the value of origin_cluster.cluster_id
     *
     * @mbg.generated
     */
    public Integer getClusterId() {
        return clusterId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.cluster_id
     *
     * @param clusterId the value for origin_cluster.cluster_id
     *
     * @mbg.generated
     */
    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.cluster_size
     *
     * @return the value of origin_cluster.cluster_size
     *
     * @mbg.generated
     */
    public Double getClusterSize() {
        return clusterSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.cluster_size
     *
     * @param clusterSize the value for origin_cluster.cluster_size
     *
     * @mbg.generated
     */
    public void setClusterSize(Double clusterSize) {
        this.clusterSize = clusterSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.impact_num
     *
     * @return the value of origin_cluster.impact_num
     *
     * @mbg.generated
     */
    public Double getImpactNum() {
        return impactNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.impact_num
     *
     * @param impactNum the value for origin_cluster.impact_num
     *
     * @mbg.generated
     */
    public void setImpactNum(Double impactNum) {
        this.impactNum = impactNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.file_name
     *
     * @return the value of origin_cluster.file_name
     *
     * @mbg.generated
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.file_name
     *
     * @param fileName the value for origin_cluster.file_name
     *
     * @mbg.generated
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column origin_cluster.file_type
     *
     * @return the value of origin_cluster.file_type
     *
     * @mbg.generated
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column origin_cluster.file_type
     *
     * @param fileType the value for origin_cluster.file_type
     *
     * @mbg.generated
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table origin_cluster
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
        OriginCluster other = (OriginCluster) that;
        return (this.getClusterThresholdId() == null ? other.getClusterThresholdId() == null : this.getClusterThresholdId().equals(other.getClusterThresholdId()))
            && (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getClusterId() == null ? other.getClusterId() == null : this.getClusterId().equals(other.getClusterId()))
            && (this.getClusterSize() == null ? other.getClusterSize() == null : this.getClusterSize().equals(other.getClusterSize()))
            && (this.getImpactNum() == null ? other.getImpactNum() == null : this.getImpactNum().equals(other.getImpactNum()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table origin_cluster
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getClusterThresholdId() == null) ? 0 : getClusterThresholdId().hashCode());
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getClusterId() == null) ? 0 : getClusterId().hashCode());
        result = prime * result + ((getClusterSize() == null) ? 0 : getClusterSize().hashCode());
        result = prime * result + ((getImpactNum() == null) ? 0 : getImpactNum().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table origin_cluster
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", clusterThresholdId=").append(clusterThresholdId);
        sb.append(", repositoryId=").append(repositoryId);
        sb.append(", clusterId=").append(clusterId);
        sb.append(", clusterSize=").append(clusterSize);
        sb.append(", impactNum=").append(impactNum);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileType=").append(fileType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}