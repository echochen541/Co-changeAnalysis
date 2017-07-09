package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;

public interface ChangeRelationCountMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer changeRelationCountId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    int insert(ChangeRelationCount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    int insertSelective(ChangeRelationCount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    ChangeRelationCount selectByPrimaryKey(Integer changeRelationCountId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ChangeRelationCount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation_count
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ChangeRelationCount record);
    
    
    List<ChangeRelationCount> selectByRepoId(@Param(value="repoId")int repoId);
}