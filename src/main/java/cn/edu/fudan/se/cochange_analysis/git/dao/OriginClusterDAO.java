package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.OriginCluster;

public class OriginClusterDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static OriginClusterMapper originClusterMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			originClusterMapper = sqlSession.getMapper(OriginClusterMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(OriginCluster originCluster) {
		originClusterMapper.insert(originCluster);
		sqlSession.commit();
	}

	public static void insertBatch(List<OriginCluster> originClusterList) {
		originClusterMapper.insertBatch(originClusterList);
		sqlSession.commit();
	}

	public static List<Integer> selectByClusterThresholdIdAndMinSizeAndMinRatio(int repositoryId,
			int clusterThresholdId, double minSize, double minRatio) {
		return originClusterMapper.selectByClusterThresholdIdAndMinSizeAndMinRatio(repositoryId, clusterThresholdId,
				minSize, minRatio);
	}

	public static List<OriginCluster> selectByRepositoryIdAndClusterThresholdIdAndClusterId(int repositoryId,
			int clusterThresholdId, int clusterId) {
		return originClusterMapper.selectByRepositoryIdAndClusterThresholdIdAndClusterId(
				repositoryId, clusterThresholdId, clusterId);
	}
}
