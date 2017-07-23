package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.HotspotFile;
import cn.edu.fudan.se.cochange_analysis.git.bean.SnapshotFile;

public class SnapshotFileDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static SnapshotFileMapper snapshotFileMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			snapshotFileMapper = sqlSession.getMapper(SnapshotFileMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertBatch(List<SnapshotFile> snapshotFile) {
		snapshotFileMapper.insertBatch(snapshotFile);
		sqlSession.commit();
	}

}
