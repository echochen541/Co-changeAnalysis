package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.util.List;

import cn.edu.fudan.se.cochange_analysis.git.bean.AccumutiveBug;
import cn.edu.fudan.se.cochange_analysis.git.bean.AccumutiveBugKey;

public interface AccumutiveBugMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table accumutive_bug
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(AccumutiveBugKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table accumutive_bug
     *
     * @mbg.generated
     */
    int insert(AccumutiveBug record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table accumutive_bug
     *
     * @mbg.generated
     */
    int insertSelective(AccumutiveBug record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table accumutive_bug
     *
     * @mbg.generated
     */
    AccumutiveBug selectByPrimaryKey(AccumutiveBugKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table accumutive_bug
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AccumutiveBug record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table accumutive_bug
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AccumutiveBug record);

	List<AccumutiveBug> selectByRepositoryId(int repositoryId);
}