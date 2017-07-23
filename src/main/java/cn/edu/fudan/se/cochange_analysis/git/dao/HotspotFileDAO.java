package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotFile;

public class HotspotFileDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static HotspotFileMapper hotspotFileMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			hotspotFileMapper = sqlSession.getMapper(HotspotFileMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void insertBatch(List<HotspotFile> hotspotFile) {
		hotspotFileMapper.insertBatch(hotspotFile);
		sqlSession.commit();
	}


}
