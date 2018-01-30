package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.RawParallelChangeRelationCommit;

public class RawParallelChangeRelationCommitDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static RawParallelChangeRelationCommitMapper rawParallelChangeRelationCommitMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			rawParallelChangeRelationCommitMapper = sqlSession.getMapper(RawParallelChangeRelationCommitMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<RawParallelChangeRelationCommit> selectByRepositoryId(int repositoryId) {
		return rawParallelChangeRelationCommitMapper.selectByRepositoryId(repositoryId);
	}
}
