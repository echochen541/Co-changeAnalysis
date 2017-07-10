package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;

public class FilePairCommitDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static FilePairCommitMapper filePairCommitMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			filePairCommitMapper = sqlSession.getMapper(FilePairCommitMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertFilePairCommit(FilePairCommit filePairCommit) {
		filePairCommitMapper.insert(filePairCommit);
		sqlSession.commit();
	}
	public static List<FilePairCommit> selectByFilePairName(String filePair,int repoId){
		return filePairCommitMapper.selectByFilePairName(filePair,repoId);
	}
	public static void insertBatch(List<FilePairCommit> filePairCommits) {
		filePairCommitMapper.insertBatch(filePairCommits);
		sqlSession.commit();
	}
}
