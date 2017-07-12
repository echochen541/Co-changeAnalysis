package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperation;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;
import cn.edu.fudan.se.cochange_analysis.git.bean.UniqueChangeOperation;

public interface ChangeOperationMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer changeOperationId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	int insert(ChangeOperationWithBLOBs record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	int insertSelective(ChangeOperationWithBLOBs record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	ChangeOperationWithBLOBs selectByPrimaryKey(Integer changeOperationId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(ChangeOperationWithBLOBs record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeyWithBLOBs(ChangeOperationWithBLOBs record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table change_operation
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(ChangeOperation record);

	int insertBatch(List<ChangeOperationWithBLOBs> operations);

	List<UniqueChangeOperation> selectUniqueChangeOperationsByCommitIdAndFileName(@Param(value = "commitId") String commitId,
			@Param(value = "fileName") String fileName);
}