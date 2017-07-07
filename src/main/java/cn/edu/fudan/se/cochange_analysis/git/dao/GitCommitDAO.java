package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;

public class GitCommitDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static GitCommitMapper commitMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			commitMapper = sqlSession.getMapper(GitCommitMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertCommit(GitCommit commit) {
		commitMapper.insert(commit);
		sqlSession.commit();
	}
	
	public static List<GitCommit> selectByRepositoryId(int repositoryId){
		return commitMapper.selectByRepositoryId(repositoryId);
	}
	
	public static void insertFilteredCommit(GitCommit commit) {
		commitMapper.insertFilter(commit);
		sqlSession.commit();
	}
}