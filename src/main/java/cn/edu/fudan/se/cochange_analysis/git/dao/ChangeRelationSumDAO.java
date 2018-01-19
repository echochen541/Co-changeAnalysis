package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationSum;

public class ChangeRelationSumDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static ChangeRelationSumMapper changeRelationSumMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			changeRelationSumMapper = sqlSession.getMapper(ChangeRelationSumMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertChangeRelationCount(ChangeRelationSum changeRelationSum) {
		changeRelationSumMapper.insert(changeRelationSum);
		sqlSession.commit();
	}

	public static List<ChangeRelationSum> selectTopNByRepositoryId(int n, int repositoryId) {
		return changeRelationSumMapper.selectTopNByRepositoryId(n, repositoryId);
	}
}
