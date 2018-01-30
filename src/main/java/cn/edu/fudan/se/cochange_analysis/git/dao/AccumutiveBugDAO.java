package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.cochange_analysis.git.bean.AccumutiveBug;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;

public class AccumutiveBugDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AccumutiveBugMapper accumutiveBugMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			accumutiveBugMapper = sqlSession.getMapper(AccumutiveBugMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertChangeFile(AccumutiveBug accumutiveBug) {
		accumutiveBugMapper.insert(accumutiveBug);
		sqlSession.commit();
	}

	public static List<AccumutiveBug> selectByRepositoryId(int repositoryId) {
		return accumutiveBugMapper.selectByRepositoryId(repositoryId);
	}
}
