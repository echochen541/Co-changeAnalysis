package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.util.List;

import cn.edu.fudan.se.cochange_analysis.git.bean.IssueBug;
import cn.edu.fudan.se.cochange_analysis.git.bean.IssueBugKey;

public interface IssueBugMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(IssueBugKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    int insert(IssueBug record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    int insertSelective(IssueBug record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    IssueBug selectByPrimaryKey(IssueBugKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(IssueBug record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue_bug
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(IssueBug record);

	int insertBatch(List<IssueBug> issueBugs);

	List<IssueBug> selectByRepositoryId(int repositoryId);
}