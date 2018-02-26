package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.cochange_analysis.expression.parser.ExpressionParser;
import cn.edu.fudan.se.cochange_analysis.expression.parser.ExpressionTree;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.RawParallelChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.ParallelChangeRelationCommit;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.RawParallelChangeRelationCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.ParallelChangeRelationCommitDAO;

public class ParallelChangeRelationExtrctor {
	private GitRepository repository;
	private List<ParallelChangeRelationCommit> parallelChangeRelationCommitList;

	public ParallelChangeRelationExtrctor() {
		parallelChangeRelationCommitList = new ArrayList<ParallelChangeRelationCommit>();
	}

	public ParallelChangeRelationExtrctor(GitRepository repository) {
		parallelChangeRelationCommitList = new ArrayList<ParallelChangeRelationCommit>();
		this.repository = repository;
	}

	public GitRepository getRepository() {
		return repository;
	}

	public void setRepository(GitRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		ParallelChangeRelationExtrctor extractor = new ParallelChangeRelationExtrctor(gitRepository);
		extractor.extractParallelChangeRelation();

		gitRepository = new GitRepository(2, "cassandra", "D:/echo/lab/research/co-change/projects/cassandra/.git");
		extractor = new ParallelChangeRelationExtrctor(gitRepository);
		extractor.extractParallelChangeRelation();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		extractor = new ParallelChangeRelationExtrctor(gitRepository);
		extractor.extractParallelChangeRelation();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		extractor = new ParallelChangeRelationExtrctor(gitRepository);
		extractor.extractParallelChangeRelation();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		extractor = new ParallelChangeRelationExtrctor(gitRepository);
		extractor.extractParallelChangeRelation();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		extractor = new ParallelChangeRelationExtrctor(gitRepository);
		extractor.extractParallelChangeRelation();
	}

	public void extractParallelChangeRelation() {
		int repositoryId = repository.getRepositoryId();
		System.out.println("repositoryId : " + repositoryId);
		List<RawParallelChangeRelationCommit> rawParallelChangeRelationCommitList = RawParallelChangeRelationCommitDAO
				.selectByRepositoryId(repositoryId);
		System.out.println("rawPCRCList size: " + rawParallelChangeRelationCommitList.size());

		for (RawParallelChangeRelationCommit rawPCRC : rawParallelChangeRelationCommitList) {
			if (isParallelChangeRelation(rawPCRC)) {
				// store
				if (parallelChangeRelationCommitList.size() >= 100) {
					ParallelChangeRelationCommitDAO.insertBatch(parallelChangeRelationCommitList);
					parallelChangeRelationCommitList.clear();
				}
			}
		}

		if (parallelChangeRelationCommitList.size() > 0) {
			ParallelChangeRelationCommitDAO.insertBatch(parallelChangeRelationCommitList);
		}
	}

	private boolean isParallelChangeRelation(RawParallelChangeRelationCommit rawPCRC) {
		int repositoryId = rawPCRC.getRepositoryId();
		String commitId = rawPCRC.getCommitId();
		String filePair = rawPCRC.getFilePair();
		String changeType = rawPCRC.getChangeType1();

		List<FilePairCommit> filePairCommits = FilePairCommitDAO
				.selectByRepositoryIdAndCommitIdAndFilePair(repositoryId, commitId, filePair);

		FilePairCommit filePairCommit = filePairCommits.get(0);
		String fileName1 = filePairCommit.getFileName1();
		String fileName2 = filePairCommit.getFileName2();

		List<ChangeOperationWithBLOBs> changeOperationList1 = ChangeOperationDAO
				.selectByRepositoryIdAndCommitIdAndFileNameAndChangeType(repositoryId, commitId, fileName1, changeType);
		List<ChangeOperationWithBLOBs> changeOperationList2 = ChangeOperationDAO
				.selectByRepositoryIdAndCommitIdAndFileNameAndChangeType(repositoryId, commitId, fileName2, changeType);

		for (ChangeOperationWithBLOBs co1 : changeOperationList1) {
			for (ChangeOperationWithBLOBs co2 : changeOperationList2) {
				if (isSameChangeOperation(co1, co2, changeType)) {
					ParallelChangeRelationCommit pCRC = new ParallelChangeRelationCommit(repositoryId, commitId,
							filePair, changeType, changeType, co1.getChangeOperationId(), co2.getChangeOperationId());
					parallelChangeRelationCommitList.add(pCRC);
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSameChangeOperation(ChangeOperationWithBLOBs co1, ChangeOperationWithBLOBs co2,
			String changeType) {
		String changedEntityContent1 = co1.getChangedEntityContent();
		String newEntityContent1 = co1.getNewEntityContent();
		String parentEntityContent1 = co1.getParentEntityContent();
		String rootEntityContent1 = co1.getRootEntityContent();

		String changedEntityContent2 = co2.getChangedEntityContent();
		String newEntityContent2 = co2.getNewEntityContent();
		String parentEntityContent2 = co2.getParentEntityContent();
		String rootEntityContent2 = co2.getRootEntityContent();

		// add/remove field
		if (changeType.equals("ADDITIONAL_OBJECT_STATE") || changeType.equals("REMOVED_OBJECT_STATE")) {
			String[] typeAndModifier1 = changedEntityContent1.split(" : ");
			String type1 = parseType(typeAndModifier1[1]);
			String modifier1 = typeAndModifier1[0].substring(typeAndModifier1[0].lastIndexOf(".") + 1);

			String[] typeAndModifier2 = changedEntityContent2.split(" : ");
			String type2 = parseType(typeAndModifier2[1]);
			String modifier2 = typeAndModifier2[0].substring(typeAndModifier2[0].lastIndexOf(".") + 1);

			String parsedModifier1 = parseModifier(modifier1);
			String parsedModifier2 = parseModifier(modifier2);

			if (type1.equals(type2) && parsedModifier1.equals(parsedModifier2)) {
				if (!modifier1.equals(modifier2)) {
					// System.out.println(changeType);
					// System.out.println(co1);
					// System.out.println(co2);
					// System.out.println();
				}
				return true;
			}
		}

		// change field type
		if (changeType.equals("ATTRIBUTE_TYPE_CHANGE")) {
			String[] typeAndModifier1 = parentEntityContent1.split(" : ");
			String modifier1 = typeAndModifier1[0].substring(typeAndModifier1[0].lastIndexOf(".") + 1);

			String[] typeAndModifier2 = parentEntityContent2.split(" : ");
			String modifier2 = typeAndModifier2[0].substring(typeAndModifier2[0].lastIndexOf(".") + 1);

			String parsedModifier1 = parseModifier(modifier1);
			String parsedModifier2 = parseModifier(modifier2);

			if (parsedModifier1.equals(parsedModifier2)
					&& parseType(changedEntityContent1).equals(parseType(changedEntityContent2))
					&& parseType(newEntityContent1).equals(parseType(newEntityContent2))) {
				// System.out.println(changeType);
				// System.out.println(co1);
				// System.out.println(co2);
				// System.out.println();
				return true;
			}
		}

		// field rename
		if (changeType.equals("ATTRIBUTE_RENAMING")) {
			String[] oldTypeAndModifier1 = changedEntityContent1.split(" : ");
			String oldModifier1 = oldTypeAndModifier1[0].substring(oldTypeAndModifier1[0].lastIndexOf(".") + 1);
			String oldType1 = parseType(oldTypeAndModifier1[1]);

			String[] oldTypeAndModifier2 = changedEntityContent2.split(" : ");
			String oldModifier2 = oldTypeAndModifier2[0].substring(oldTypeAndModifier2[0].lastIndexOf(".") + 1);
			String oldType2 = parseType(oldTypeAndModifier2[1]);

			String oldParsedModifier1 = parseModifier(oldModifier1);
			String oldParsedModifier2 = parseModifier(oldModifier2);

			String[] newTypeAndModifier1 = newEntityContent1.split(" : ");
			String newModifier1 = newTypeAndModifier1[0].substring(newTypeAndModifier1[0].lastIndexOf(".") + 1);
			String newType1 = parseType(newTypeAndModifier1[1]);

			String[] newTypeAndModifier2 = newEntityContent2.split(" : ");
			String newModifier2 = newTypeAndModifier2[0].substring(newTypeAndModifier2[0].lastIndexOf(".") + 1);
			String newType2 = parseType(newTypeAndModifier2[1]);

			String newParsedModifier1 = parseModifier(newModifier1);
			String newParsedModifier2 = parseModifier(newModifier2);

			if (oldType1.equals(oldType2) && oldParsedModifier1.equals(oldParsedModifier2) && newType1.equals(newType2)
					&& newParsedModifier1.equals(newParsedModifier2)) {
				// System.out.println(changeType);
				// System.out.println(co1);
				// System.out.println(co2);
				// System.out.println();
				return true;
			}
		}

		// add/remove parameter
		if (changeType.equals("PARAMETER_DELETE") || changeType.equals("PARAMETER_INSERT")) {
			String modifier1 = changedEntityContent1;
			String modifier2 = changedEntityContent2;
			String parsedModifier1 = parseModifier(modifier1);
			String parsedModifier2 = parseModifier(modifier2);

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (parsedModifier1.equals(parsedModifier2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType);
					// System.out.println(co1);
					// System.out.println(co2);
					// System.out.println();
				}
				return true;
			}
		}

		// parameter rename
		if (changeType.equals("PARAMETER_RENAMING")) {
			String oldModifier1 = changedEntityContent1;
			String newModifier1 = newEntityContent1;
			String oldModifier2 = changedEntityContent2;
			String newModifier2 = newEntityContent2;

			String oldParsedModifier1 = parseModifier(oldModifier1);
			String newParsedModifier1 = parseModifier(newModifier1);
			String oldParsedModifier2 = parseModifier(oldModifier2);
			String newParsedModifier2 = parseModifier(newModifier2);

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (oldParsedModifier1.equals(oldParsedModifier2) && newParsedModifier1.equals(newParsedModifier2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType);
					// System.out.println(co1);
					// System.out.println(co2);
					// System.out.println();
				}
				return true;
			}
		}

		// change parameter type
		if (changeType.equals("PARAMETER_TYPE_CHANGE")) {
			String modifier1 = parentEntityContent1;
			String parsedModifier1 = parseModifier(modifier1);

			String oldType1 = "dummy";
			if (changedEntityContent1.contains(": ")) {
				String[] oldTypeAndModifier1 = changedEntityContent1.split(": ");
				oldType1 = parseType(oldTypeAndModifier1[1]);
			} else {
				oldType1 = parseType(changedEntityContent1);
			}

			String newType1 = "dummy";
			if (newEntityContent1.contains(": ")) {
				String[] newTypeAndModifier1 = newEntityContent1.split(": ");
				newType1 = parseType(newTypeAndModifier1[1]);
			} else {
				newType1 = parseType(newEntityContent1);
			}

			String modifier2 = parentEntityContent2;
			String parsedModifier2 = parseModifier(modifier2);

			String oldType2 = "dummy";
			if (changedEntityContent2.contains(": ")) {
				String[] oldTypeAndModifier2 = changedEntityContent2.split(": ");
				oldType2 = parseType(oldTypeAndModifier2[1]);
			} else {
				oldType2 = parseType(changedEntityContent2);
			}

			String newType2 = "dummy";
			if (newEntityContent2.contains(": ")) {
				String[] newTypeAndModifier2 = newEntityContent2.split(": ");
				newType2 = parseType(newTypeAndModifier2[1]);
			} else {
				newType2 = parseType(newEntityContent2);
			}

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (parsedModifier1.equals(parsedModifier2) && oldType1.equals(oldType2) && newType1.equals(newType2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType);
					// System.out.println(co1);
					// System.out.println(co2);
					// System.out.println();
				}
				return true;
			}
		}

		// change return type
		if (changeType.equals("RETURN_TYPE_CHANGE")) {
			if (!co1.getParentEntityType().equals("THROW") && !co2.getParentEntityType().equals("THROW")) {
				String oldType1 = null, newType1 = null, oldType2 = null, newType2 = null;

				if (changedEntityContent1.contains(": ")) {
					String[] oldTypeAndModifier1 = changedEntityContent1.split(": ");
					oldType1 = parseType(oldTypeAndModifier1[1]);
				} else {
					oldType1 = parseType(changedEntityContent1);
				}

				if (newEntityContent1.contains(": ")) {
					String[] newTypeAndModifier1 = newEntityContent1.split(": ");
					newType1 = parseType(newTypeAndModifier1[1]);
				} else {
					newType1 = parseType(newEntityContent1);
				}

				if (changedEntityContent2.contains(": ")) {
					String[] oldTypeAndModifier2 = changedEntityContent2.split(": ");
					oldType2 = parseType(oldTypeAndModifier2[1]);
				} else {
					oldType2 = parseType(changedEntityContent2);
				}

				if (newEntityContent2.contains(": ")) {
					String[] newTypeAndModifier2 = newEntityContent2.split(": ");
					newType2 = parseType(newTypeAndModifier2[1]);
				} else {
					newType2 = parseType(newEntityContent2);
				}

				String methodName1 = getMethodName(rootEntityContent1);
				String methodName2 = getMethodName(rootEntityContent2);

				if (oldType1.equals(oldType2) && newType1.equals(newType2)) {
					if (!methodName1.equals(methodName2)) {
						// System.out.println(changeType);
						// System.out.println(co1);
						// System.out.println(co2);
						// System.out.println();
					}
					return true;
				}
			}
		}

		if (changeType.equals("RETURN_TYPE_INSERT") || changeType.equals("RETURN_TYPE_DELETE")) {
			String[] tokens1 = changedEntityContent1.split(": ");
			String[] tokens2 = changedEntityContent2.split(": ");
			String type1 = parseType(tokens1[1]);
			String type2 = parseType(tokens2[1]);

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (type1.equals(type2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType);
					// System.out.println(co1);
					// System.out.println(co2);
					// System.out.println();
				}
				return true;
			}
		}

		if ((changeType.equals("IF_STATEMENT_INSERT") || changeType.equals("IF_STATEMENT_DELETE"))) {
			String expressionStr1 = changedEntityContent1;
			ExpressionParser parser1 = new ExpressionParser(expressionStr1);
			ExpressionTree expressionTree1 = parser1.parse2Tree(parser1.parse2Expression());

			String expressionStr2 = changedEntityContent2;
			ExpressionParser parser2 = new ExpressionParser(expressionStr2);
			ExpressionTree expressionTree2 = parser2.parse2Tree(parser2.parse2Expression());

			if (expressionTree1.isSameExpressionTree(expressionTree2)) {
				// System.out.println("IF_STATEMENT_INSERT/DELETE");
				// System.out.println(expressionTree1.content);
				// System.out.println(expressionTree2.content);
				// System.out.println();
				return true;
			}
		}

		if (changeType.equals("IF_STATEMENT_CONDITION_EXPRESSION_CHANGE")) {
			String oldExpressionStr1 = changedEntityContent1;
			ExpressionParser oldParser1 = new ExpressionParser(oldExpressionStr1);
			ExpressionTree oldExpressionTree1 = oldParser1.parse2Tree(oldParser1.parse2Expression());

			String newExpressionStr1 = newEntityContent1;
			ExpressionParser newParser1 = new ExpressionParser(newExpressionStr1);
			ExpressionTree newExpressionTree1 = newParser1.parse2Tree(newParser1.parse2Expression());

			String oldExpressionStr2 = changedEntityContent2;
			ExpressionParser oldParser2 = new ExpressionParser(oldExpressionStr2);
			ExpressionTree oldExpressionTree2 = oldParser2.parse2Tree(oldParser2.parse2Expression());

			String newExpressionStr2 = newEntityContent2;
			ExpressionParser newParser2 = new ExpressionParser(newExpressionStr2);
			ExpressionTree newExpressionTree2 = newParser2.parse2Tree(newParser2.parse2Expression());

			if ((oldExpressionTree1.isSameExpressionTree(oldExpressionTree2))
					&& (newExpressionTree1.isSameExpressionTree(newExpressionTree2))) {
				// System.out.println("IF_STATEMENT_CONDITION_EXPRESSION_CHANGE");
				// System.out.println(oldExpressionTree1.content);
				// System.out.println(newExpressionTree1.content);
				// System.out.println(oldExpressionTree2.content);
				// System.out.println(newExpressionTree2.content);
				// System.out.println();
				return true;
			}
		}
		return false;
	}

	private String parseType(String rawType) {
		if (rawType.equals("Byte")) {
			return "byte";
		} else if (rawType.equals("Boolean")) {
			return "boolean";
		} else if (rawType.equals("Short")) {
			return "short";
		} else if (rawType.equals("Character")) {
			return "character";
		} else if (rawType.equals("Integer")) {
			return "int";
		} else if (rawType.equals("Long")) {
			return "long";
		} else if (rawType.equals("Float")) {
			return "float";
		} else if (rawType.equals("Double")) {
			return "double";
		} else {
			return rawType;
		}
	}

	private String getMethodName(String content) {
		int idx = content.lastIndexOf("(");
		return content.substring(content.lastIndexOf(".", idx) + 1, idx);
	}

	private static String parseModifier(String name) {
		return name.replace("_", "").toLowerCase();
	}
}
