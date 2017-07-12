package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.IssueBug;

public class IssueBugDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static IssueBugMapper issueBugMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			issueBugMapper = sqlSession.getMapper(IssueBugMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertFilePairCommit(IssueBug issueBug) {
		issueBugMapper.insert(issueBug);
		sqlSession.commit();
	}

	public static void insertBatch(List<IssueBug> issueBugs) {
		issueBugMapper.insertBatch(issueBugs);
		sqlSession.commit();
	}
	
	public static List<IssueBug> selectByRepository(int repositoryId) {
		return issueBugMapper.selectByRepositoryId(repositoryId);
	}
}
