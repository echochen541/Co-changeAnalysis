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
    int insert(HotspotFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hotspot_file
     *
     * @mbg.generated
     */
    int insertSelective(HotspotFile record);

	int insertBatch(List<HotspotFile> hotspotFile);
}