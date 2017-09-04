package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.RawFilterSharedDependency;
import cn.edu.fudan.se.cochange_analysis.git.bean.SharedDependency;

public class RawFilterSharedDependencyDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static RawFilterSharedDependencyMapper rawFilterSharedDependencyMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			rawFilterSharedDependencyMapper = sqlSession.getMapper(RawFilterSharedDependencyMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<RawFilterSharedDependency> selectByRepositoryId(int repositoryId) {
		return rawFilterSharedDependencyMapper.selectByRepositoryId(repositoryId);
	}
}
