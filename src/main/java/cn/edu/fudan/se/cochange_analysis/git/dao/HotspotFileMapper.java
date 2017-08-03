package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.util.List;

import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotFile;

public interface HotspotFileMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer hotspotFileId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    int insert(HotspotFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    int insertSelective(HotspotFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    HotspotFile selectByPrimaryKey(Integer hotspotFileId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(HotspotFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(HotspotFile record);

	int insertBatch(List<HotspotFile> hotspotFileList);
}