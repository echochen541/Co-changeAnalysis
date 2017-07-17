package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;

public class FilePairCountDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static FilePairCountMapper filePairCountMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			filePairCountMapper = sqlSession.getMapper(FilePairCountMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<FilePairCount> selectByRepositoryIdAndCount(int repoId, int threshold1) {
		return filePairCountMapper.selectByRepositoryIdAndCount(repoId, threshold1);
	}
}
