package cn.edu.fudan.se.cochange_analysis.git.dao;

import cn.edu.fudan.se.cochange_analysis.git.bean.CommitParent;
import cn.edu.fudan.se.cochange_analysis.git.bean.CommitParentKey;

public interface CommitParentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_parent
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(CommitParentKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_parent
     *
     * @mbg.generated
     */
    int insert(CommitParent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_parent
     *
     * @mbg.generated
     */
    int insertSelective(CommitParent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_parent
     *
     * @mbg.generated
     */
    CommitParent selectByPrimaryKey(CommitParentKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_parent
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CommitParent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commit_parent
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CommitParent record);
}