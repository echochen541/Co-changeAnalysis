package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.ClusterAncestor;
import cn.edu.fudan.se.cochange_analysis.git.bean.OriginCluster;

public class ClusterAncestorDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static ClusterAncestorMapper clusterAncestorMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			clusterAncestorMapper = sqlSession.getMapper(ClusterAncestorMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(ClusterAncestor clusterAncestor) {
		clusterAncestorMapper.insert(clusterAncestor);
		sqlSession.commit();
	}

	public static void insertBatch(List<ClusterAncestor> clusterAncestorList) {
		clusterAncestorMapper.insertBatch(clusterAncestorList);
		sqlSession.commit();
	}
}
