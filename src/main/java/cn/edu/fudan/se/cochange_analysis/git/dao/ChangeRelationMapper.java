package cn.edu.fudan.se.cochange_analysis.git.dao;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationKey;

public interface ChangeRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(ChangeRelationKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation
     *
     * @mbg.generated
     */
    int insert(ChangeRelationKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table change_relation
     *
     * @mbg.generated
     */
    int insertSelective(ChangeRelationKey record);
}