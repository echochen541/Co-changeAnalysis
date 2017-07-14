package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.BugFixFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;

public class BugFixFileDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static BugFixFileMapper bugFixFileMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			bugFixFileMapper = sqlSession.getMapper(BugFixFileMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(BugFixFile bugFixFile) {
		bugFixFileMapper.insert(bugFixFile);
		sqlSession.commit();
	}

	public static void insertBatch(List<BugFixFile> bugFixFileList) {
		bugFixFileMapper.insertBatch(bugFixFileList);
		sqlSession.commit();
	}

}
