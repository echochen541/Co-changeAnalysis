package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.SharedDependency;

public class SharedDependencyDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static SharedDependencyMapper sharedDependencyMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			sharedDependencyMapper = sqlSession.getMapper(SharedDependencyMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<SharedDependency> selectByRepositoryId(int repositoryId) {
		return sharedDependencyMapper.selectByRepositoryId(repositoryId);
	}

	public static void insertBatch(List<SharedDependency> sharedDependencyList) {
		sharedDependencyMapper.insertBatch(sharedDependencyList);
		sqlSession.commit();
	}
}
