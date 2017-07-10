package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationUnique;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;

public class ChangeOperationDAO {

	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static ChangeOperationMapper changeOperationMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			changeOperationMapper = sqlSession.getMapper(ChangeOperationMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertChangeFile(ChangeOperationWithBLOBs changeOperation) {
		changeOperationMapper.insert(changeOperation);
		sqlSession.commit();
	}

	public static void insertChanges(List<SourceCodeChange> changes, int repositoryId, String commitId,
			String filePath) {
		for (SourceCodeChange change : changes) {
			String newEntity = "";
			// if update, store new entity content
			if (change instanceof Update) {
				Update update = (Update) change;
				newEntity = update.getNewEntity().getUniqueName();
			}

			ChangeOperationWithBLOBs operation = new ChangeOperationWithBLOBs(0, repositoryId, commitId, filePath,
					change.getRootEntity().getType().toString(), change.getParentEntity().getType().toString(),
					change.getChangeType().toString(), change.getSignificanceLevel().toString(),
					change.getChangedEntity().getType().toString(), change.getRootEntity().getUniqueName().toString(),
					change.getParentEntity().getUniqueName().toString(),
					change.getChangedEntity().getUniqueName().toString(), newEntity);
			// System.out.println(operation.toString());
			try {
				changeOperationMapper.insert(operation);
				sqlSession.commit();
			} catch (Exception e) {
				System.out.println("insert: " + operation);
				e.printStackTrace();
			}
		}

	}
	public static List<ChangeOperationUnique> selectChangeOperationsByFileNameAndCommitId(String fileName,String commitId){
		return changeOperationMapper.selectChangeOperationsByFileNameAndCommitId(fileName,commitId);
	}
}
