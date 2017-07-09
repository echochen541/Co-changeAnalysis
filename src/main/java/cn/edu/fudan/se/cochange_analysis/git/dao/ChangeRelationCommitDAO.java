package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;

public class ChangeRelationCommitDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static ChangeRelationCommitMapper changeRelationCommitMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			changeRelationCommitMapper = sqlSession.getMapper(ChangeRelationCommitMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertChangeRelationCommitMapper(ChangeRelationCommit commit) {
		changeRelationCommitMapper.insert(commit);
		sqlSession.commit();
	}

}