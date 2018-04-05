package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.Hotspot;
import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotFile;

public class HotspotDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static HotspotMapper hotspotMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			hotspotMapper = sqlSession.getMapper(HotspotMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(Hotspot hotspot) {
		hotspotMapper.insert(hotspot);
		sqlSession.commit();
	}

	public static List<Hotspot> selectByThresholds(int repositoryId, int clusterThresholdId, int hotspotThresholdId) {
		return hotspotMapper.selectByThresholds(repositoryId, clusterThresholdId, hotspotThresholdId);
	}
}
