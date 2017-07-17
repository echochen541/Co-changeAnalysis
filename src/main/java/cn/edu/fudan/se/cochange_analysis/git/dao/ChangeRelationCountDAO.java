package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationUnique;

public class ChangeRelationCountDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static ChangeRelationCountMapper changeRelationCountMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			changeRelationCountMapper = sqlSession.getMapper(ChangeRelationCountMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertChangeRelationCount(ChangeRelationCount commit) {
		changeRelationCountMapper.insert(commit);
		sqlSession.commit();
	}
	public static List<ChangeRelationCount> selectAllChangeRelationCount(int repoId,String myFilePair,int threshold ){
		return changeRelationCountMapper.selectByRepoId(repoId,myFilePair,threshold);
	}
	public static List<ChangeRelationUnique> selectDistinctChangeType(int repoId,int threshold){
		return changeRelationCountMapper.selectDistinctChangeType(repoId,threshold);
	}
	public static List<ChangeRelationCount> selectByRepositoryId(int repositoryId) {
		return changeRelationCountMapper.selectByRepositoryId(repositoryId);
	}
	
	public static List<ChangeRelationCount> selectByRepositoryIdAndFilePair(int repositoryId, String filePair) {
		return changeRelationCountMapper.selectByRepositoryIdAndFilePair(repositoryId, filePair);
	}
}
