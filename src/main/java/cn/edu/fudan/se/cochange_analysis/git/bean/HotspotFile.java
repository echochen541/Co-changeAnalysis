package cn.edu.fudan.se.cochange_analysis.git.bean;

import java.io.Serializable;

public class HotspotFile implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hotspot_file.hotspot_id
     *
     * @mbg.generated
     */
    private Integer hotspotId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hotspot_file.repository_id
     *
     * @mbg.generated
     */
    private Integer repositoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hotspot_file.type
     *
     * @mbg.generated
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hotspot_file.file_name
     *
     * @mbg.generated
     */
    private String fileName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hotspot_file.release
     *
     * @mbg.generated
     */
    private String release;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    public HotspotFile(Integer hotspotId, Integer repositoryId, String type, String fileName, String release) {
        this.hotspotId = hotspotId;
        this.repositoryId = repositoryId;
        this.type = type;
        this.fileName = fileName;
        this.release = release;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    public HotspotFile() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hotspot_file.hotspot_id
     *
     * @return the value of hotspot_file.hotspot_id
     *
     * @mbg.generated
     */
    public Integer getHotspotId() {
        return hotspotId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hotspot_file.hotspot_id
     *
     * @param hotspotId the value for hotspot_file.hotspot_id
     *
     * @mbg.generated
     */
    public void setHotspotId(Integer hotspotId) {
        this.hotspotId = hotspotId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hotspot_file.repository_id
     *
     * @return the value of hotspot_file.repository_id
     *
     * @mbg.generated
     */
    public Integer getRepositoryId() {
        return repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hotspot_file.repository_id
     *
     * @param repositoryId the value for hotspot_file.repository_id
     *
     * @mbg.generated
     */
    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hotspot_file.type
     *
     * @return the value of hotspot_file.type
     *
     * @mbg.generated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hotspot_file.type
     *
     * @param type the value for hotspot_file.type
     *
     * @mbg.generated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hotspot_file.file_name
     *
     * @return the value of hotspot_file.file_name
     *
     * @mbg.generated
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hotspot_file.file_name
     *
     * @param fileName the value for hotspot_file.file_name
     *
     * @mbg.generated
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hotspot_file.release
     *
     * @return the value of hotspot_file.release
     *
     * @mbg.generated
     */
    public String getRelease() {
        return release;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hotspot_file.release
     *
     * @param release the value for hotspot_file.release
     *
     * @mbg.generated
     */
    public void setRelease(String release) {
        this.release = release == null ? null : release.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
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
        HotspotFile other = (HotspotFile) that;
        return (this.getHotspotId() == null ? other.getHotspotId() == null : this.getHotspotId().equals(other.getHotspotId()))
            && (this.getRepositoryId() == null ? other.getRepositoryId() == null : this.getRepositoryId().equals(other.getRepositoryId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getRelease() == null ? other.getRelease() == null : this.getRelease().equals(other.getRelease()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getHotspotId() == null) ? 0 : getHotspotId().hashCode());
        result = prime * result + ((getRepositoryId() == null) ? 0 : getRepositoryId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getRelease() == null) ? 0 : getRelease().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", hotspotId=").append(hotspotId);
        sb.append(", repositoryId=").append(repositoryId);
        sb.append(", type=").append(type);
        sb.append(", fileName=").append(fileName);
        sb.append(", release=").append(release);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}