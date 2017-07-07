package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitChangeFile;

public class GitChangeFileDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static GitChangeFileMapper changeFileMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			changeFileMapper = sqlSession.getMapper(GitChangeFileMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertChangeFile(GitChangeFile changeFile) {
		changeFileMapper.insert(changeFile);
		sqlSession.commit();
	}

	public static List<GitChangeFile> selectByRepositoryIdAndCommitId(int repositoryId, String commitId) {
		return changeFileMapper.selectByRepositoryIdAndCommitId(repositoryId, commitId);
	}
	
	public static void insertChangeFileFilter(GitChangeFile changeFile) {
		changeFileMapper.insertFilter(changeFile);
		sqlSession.commit();
	}
}
