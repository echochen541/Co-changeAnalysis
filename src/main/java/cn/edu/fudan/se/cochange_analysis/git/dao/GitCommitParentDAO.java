package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommitParentKey;

public class GitCommitParentDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static GitCommitParentMapper commitParentMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			commitParentMapper = sqlSession.getMapper(GitCommitParentMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertCommitParent(GitCommitParentKey commitParent) {
		commitParentMapper.insert(commitParent);
		sqlSession.commit();
	}

	public static List<GitCommitParentKey> selectByRepositoryIdAndCommitId(int repositoryId, String commitId) {
		return commitParentMapper.selectByRepositoryIdAndCommitId(repositoryId, commitId);
	}
}