package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.util.List;

import cn.edu.fudan.se.cochange_analysis.git.bean.GlobalRelationSum;

public interface GlobalRelationSumMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table global_relation_sum
     *
     * @mbg.generated
     */
    int insert(GlobalRelationSum record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table global_relation_sum
     *
     * @mbg.generated
     */
    int insertSelective(GlobalRelationSum record);

	List<GlobalRelationSum> selectByTopN(int topN);
}