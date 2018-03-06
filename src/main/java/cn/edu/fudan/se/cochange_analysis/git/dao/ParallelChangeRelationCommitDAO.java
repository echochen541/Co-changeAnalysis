package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.ParallelChangeRelationCommit;

public class ParallelChangeRelationCommitDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static ParallelChangeRelationCommitMapper parallelChangeRelationCommitMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			parallelChangeRelationCommitMapper = sqlSession.getMapper(ParallelChangeRelationCommitMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertBatch(List<ParallelChangeRelationCommit> parallelChangeRelationCommitList) {
		parallelChangeRelationCommitMapper.insertBatch(parallelChangeRelationCommitList);
		sqlSession.commit();
	}

	public static List<ParallelChangeRelationCommit> selectDistinctByRepositoryId(int repositoryId) {
		return parallelChangeRelationCommitMapper.selectDistinctByRepositoryId(repositoryId);
	}

	public static List<ParallelChangeRelationCommit> selectByRepositoryId(int repositoryId) {
		return parallelChangeRelationCommitMapper.selectByRepositoryId(repositoryId);
	}
}
