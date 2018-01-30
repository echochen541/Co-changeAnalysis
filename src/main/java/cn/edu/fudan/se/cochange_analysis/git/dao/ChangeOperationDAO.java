package cn.edu.fudan.se.cochange_analysis.git.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
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
		List<ChangeOperationWithBLOBs> operations = new ArrayList<ChangeOperationWithBLOBs>();
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
			operations.add(operation);
			// changeOperationMapper.insert(operation);
			// sqlSession.commit();
		}
		changeOperationMapper.insertBatch(operations);
		sqlSession.commit();
	}

	public static List<ChangeOperationWithBLOBs> selectByRepositoryIdAndCommitIdAndFileNameAndChangeTypeAndChangedEntityType(
			int repositoryId, String commitId, String fileName, String changeType, String changedEntityType) {
		return changeOperationMapper.selectByRepositoryIdAndCommitIdAndFileNameAndChangeTypeAndChangedEntityType(
				repositoryId, commitId, fileName, changeType, changedEntityType);
	}

	public static List<ChangeOperationWithBLOBs> selectByChangeTypeAndChangedEntityType(String changeType,
			String changedEntityType) {
		return changeOperationMapper.selectByChangeTypeAndChangedEntityType(changeType, changedEntityType);
	}

	public static List<String> selectUniqueChangeTypesByCommitIdAndFileName(String commitId, String fileName) {
		return changeOperationMapper.selectUniqueChangeTypesByCommitIdAndFileName(commitId, fileName);
	}

	public static List<ChangeOperationWithBLOBs> selectByChangeType(String changeType) {
		return changeOperationMapper.selectByChangeType(changeType);
	}

	public static List<ChangeOperationWithBLOBs> selectByRepositoryIdAndCommitIdAndFileNameAndChangeType(
			int repositoryId, String commitId, String fileName, String changeType) {
		return changeOperationMapper.selectByRepositoryIdAndCommitIdAndFileNameAndChangeType(repositoryId, commitId,
				fileName, changeType);
	}
}
