package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotRelationSum;

public class HotspotRelationSumDAO {

	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static HotspotRelationSumMapper hotspotRelationSumMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			hotspotRelationSumMapper = sqlSession.getMapper(HotspotRelationSumMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(HotspotRelationSum hotspotRelationSum) {
		hotspotRelationSumMapper.insert(hotspotRelationSum);
		sqlSession.commit();
	}

	public static void insertBatch(List<HotspotRelationSum> hotspotRelationSumList) {
		hotspotRelationSumMapper.insertBatch(hotspotRelationSumList);
		sqlSession.commit();
	}
}
