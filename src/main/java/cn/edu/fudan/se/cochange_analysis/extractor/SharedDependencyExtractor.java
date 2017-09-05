package cn.edu.fudan.se.cochange_analysis.extractor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCommit;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.RawFilterSharedDependency;
import cn.edu.fudan.se.cochange_analysis.git.bean.SharedDependency;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCommitDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.RawFilterSharedDependencyDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.SharedDependencyDAO;

public class SharedDependencyExtractor {
	private GitRepository repository;
	private List<SharedDependency> sharedDependencyList;

	public SharedDependencyExtractor() {
		sharedDependencyList = new ArrayList<SharedDependency>();
	}

	public SharedDependencyExtractor(GitRepository repository) {
		sharedDependencyList = new ArrayList<SharedDependency>();
		this.repository = repository;
	}

	public GitRepository getRepository() {
		return repository;
	}

	public void setRepository(GitRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = null;
		SharedDependencyExtractor extractor = null;
		// gitRepository = new GitRepository(1, "camel",
		// "D:/echo/lab/research/co-change/projects/camel/.git");
		// extractor = new
		// SharedDependencyExtractor(gitRepository);
		// extractor.extractSharedDependency();

		// gitRepository = new GitRepository(2, "cassandra",
		// "D:/echo/lab/research/co-change/projects/cassandra/.git");
		// extractor = new
		// SharedDependencyExtractor(gitRepository);
		// extractor.extractSharedDependency();

		gitRepository = new GitRepository(3, "cxf", "D:/echo/lab/research/co-change/projects/cxf/.git");
		extractor = new SharedDependencyExtractor(gitRepository);
		extractor.extractSharedDependency();

		gitRepository = new GitRepository(4, "hadoop", "D:/echo/lab/research/co-change/projects/hadoop/.git");
		extractor = new SharedDependencyExtractor(gitRepository);
		extractor.extractSharedDependency();

		gitRepository = new GitRepository(5, "hbase", "D:/echo/lab/research/co-change/projects/hbase/.git");
		extractor = new SharedDependencyExtractor(gitRepository);
		extractor.extractSharedDependency();

		gitRepository = new GitRepository(6, "wicket", "D:/echo/lab/research/co-change/projects/wicket/.git");
		extractor = new SharedDependencyExtractor(gitRepository);
		extractor.extractSharedDependency();
	}

	public void extractSharedDependency() {
		int repositoryId = repository.getRepositoryId();
		System.out.println("repositoryId : " + repositoryId);
		List<RawFilterSharedDependency> rawDependencyList = RawFilterSharedDependencyDAO
				.selectByRepositoryId(repositoryId);
		System.out.println("rawDependencyList size: " + rawDependencyList.size());

		for (RawFilterSharedDependency rawDependency : rawDependencyList) {
			// System.out.println(i++);
			if (isSharedDependency(rawDependency)) {
				// store
				if (sharedDependencyList.size() >= 100) {
					SharedDependencyDAO.insertBatch(sharedDependencyList);
					sharedDependencyList.clear();
				}
			}
		}
		SharedDependencyDAO.insertBatch(sharedDependencyList);
	}

	private boolean isSharedDependency(RawFilterSharedDependency rawDependency) {
		int repositoryId = rawDependency.getRepositoryId();
		String commitId = rawDependency.getCommitId();
		String filePair = rawDependency.getFilePair();
		String changeType = rawDependency.getChangeType1();
		String changedEntityType = rawDependency.getChangedEntityType1();

		// System.out.println(commitId + ", " + filePair + ", " + changeType +
		// ", " + changedEntityType);

		List<FilePairCommit> filePairCommits = FilePairCommitDAO
				.selectByRepositoryIdAndCommitIdAndFilePair(repositoryId, commitId, filePair);

		// if (filePairCommits.size() != 1) {
		// System.out.println(filePairCommits.size());
		// System.out.println(commitId + ", " + filePair + ", " + changeType +
		// ", " + changedEntityType);
		// System.out.println(filePairCommits);
		// }

		FilePairCommit filePairCommit = filePairCommits.get(0);
		String fileName1 = filePairCommit.getFileName1();
		String fileName2 = filePairCommit.getFileName2();

		// System.out.println(fileName1 + ", " + fileName2);

		List<ChangeOperationWithBLOBs> changeOperationList1 = ChangeOperationDAO
				.selectByRepositoryIdAndCommitIdAndFileNameAndChangeTypeAndChangedEntityType(repositoryId, commitId,
						fileName1, changeType, changedEntityType);
		List<ChangeOperationWithBLOBs> changeOperationList2 = ChangeOperationDAO
				.selectByRepositoryIdAndCommitIdAndFileNameAndChangeTypeAndChangedEntityType(repositoryId, commitId,
						fileName2, changeType, changedEntityType);

		// System.out.println(changeOperationList1.size());
		// System.out.println(changeOperationList2.size());

		for (ChangeOperationWithBLOBs co1 : changeOperationList1) {
			for (ChangeOperationWithBLOBs co2 : changeOperationList2) {
				if (isSameChangeOperation(co1, co2, changeType, changedEntityType)) {
					SharedDependency sharedDependency = new SharedDependency(repositoryId, commitId, filePair,
							changeType, changedEntityType, changeType, changedEntityType, co1.getChangeOperationId(),
							co2.getChangeOperationId());
					sharedDependencyList.add(sharedDependency);
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSameChangeOperation(ChangeOperationWithBLOBs co1, ChangeOperationWithBLOBs co2, String changeType,
			String changedEntityType) {
		String changedEntityContent1 = co1.getChangedEntityContent();
		String newEntityContent1 = co1.getNewEntityContent();
		String parentEntityContent1 = co1.getParentEntityContent();
		String rootEntityContent1 = co1.getRootEntityContent();

		String changedEntityContent2 = co2.getChangedEntityContent();
		String newEntityContent2 = co2.getNewEntityContent();
		String parentEntityContent2 = co2.getParentEntityContent();
		String rootEntityContent2 = co2.getRootEntityContent();

		// System.out.println(changeType + " , " + changedEntityType);
		// System.out.println(co1);
		// System.out.println(co2);
		// System.out.println();

		// add/remove field
		if (changeType.equals("ADDITIONAL_OBJECT_STATE") || changeType.equals("REMOVED_OBJECT_STATE")) {
			String[] typeAndModifier1 = changedEntityContent1.split(" : ");
			String type1 = typeAndModifier1[1];
			String modifier1 = typeAndModifier1[0].substring(typeAndModifier1[0].lastIndexOf(".") + 1);

			String[] typeAndModifier2 = changedEntityContent2.split(" : ");
			String type2 = typeAndModifier2[1];
			String modifier2 = typeAndModifier2[0].substring(typeAndModifier2[0].lastIndexOf(".") + 1);

			String parsedModifier1 = modifier1.replace("_", "").toLowerCase();
			String parsedModifier2 = modifier2.replace("_", "").toLowerCase();
			if (type1.equals(type2) && parsedModifier1.equals(parsedModifier2)) {
				if (!modifier1.equals(modifier2)) {
					// System.out.println(changeType + " , " +
					// changedEntityType);
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

			String parsedModifier1 = modifier1.replace("_", "").toLowerCase();
			String parsedModifier2 = modifier2.replace("_", "").toLowerCase();
			if (parsedModifier1.equals(parsedModifier2) && changedEntityContent1.equals(changedEntityContent2)
					&& newEntityContent1.equals(newEntityContent2)) {
				// System.out.println(changeType + " , " + changedEntityType);
				// System.out.println(co1);
				// System.out.println(co2);
				// System.out.println();
				return true;
			}
		}

		// add/remove method
		if (changeType.equals("ADDITIONAL_FUNCTIONALITY") || changeType.equals("REMOVED_FUNCTIONALITY")) {
			String methodName1 = getMethodName(changedEntityContent1);
			String methodName2 = getMethodName(changedEntityContent2);

			if (methodName1.equals(methodName2)) {
				// System.out.println(changeType + " , " + changedEntityType);
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
			String parsedModifier1 = modifier1.replace("_", "").toLowerCase();
			String parsedModifier2 = modifier2.replace("_", "").toLowerCase();

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (parsedModifier1.equals(parsedModifier2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType + " , " +
					// changedEntityType);
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
			String parsedModifier1 = modifier1.replace("_", "").toLowerCase();

			String oldType1 = "dummy";
			if (changedEntityContent1.contains(": ")) {
				String[] oldTypeAndModifier1 = changedEntityContent1.split(": ");
				oldType1 = oldTypeAndModifier1[1];
			} else {
				oldType1 = changedEntityContent1;
			}

			String newType1 = "dummy";
			if (newEntityContent1.contains(": ")) {
				String[] newTypeAndModifier1 = newEntityContent1.split(": ");
				newType1 = newTypeAndModifier1[1];
			} else {
				newType1 = newEntityContent1;
			}

			String modifier2 = parentEntityContent2;
			String parsedModifier2 = modifier2.replace("_", "").toLowerCase();

			String oldType2 = "dummy";
			if (changedEntityContent2.contains(": ")) {
				String[] oldTypeAndModifier2 = changedEntityContent2.split(": ");
				oldType2 = oldTypeAndModifier2[1];
			} else {
				oldType1 = changedEntityContent1;
			}

			String newType2 = "dummy";
			if (newEntityContent2.contains(": ")) {
				String[] newTypeAndModifier2 = newEntityContent2.split(": ");
				newType2 = newTypeAndModifier2[1];
			} else {
				newType2 = newEntityContent2;
			}

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (parsedModifier1.equals(parsedModifier2) && oldType1.equals(oldType2) && newType1.equals(newType2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType + " , " +
					// changedEntityType);
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
					oldType1 = oldTypeAndModifier1[1];
				} else {
					oldType1 = changedEntityContent1;
				}

				if (newEntityContent1.contains(": ")) {
					String[] newTypeAndModifier1 = newEntityContent1.split(": ");
					newType1 = newTypeAndModifier1[1];
				} else {
					newType1 = newEntityContent1;
				}

				if (changedEntityContent2.contains(": ")) {
					String[] oldTypeAndModifier2 = changedEntityContent2.split(": ");
					oldType2 = oldTypeAndModifier2[1];
				} else {
					oldType2 = changedEntityContent2;
				}

				if (newEntityContent2.contains(": ")) {
					String[] newTypeAndModifier2 = newEntityContent2.split(": ");
					newType2 = newTypeAndModifier2[1];
				} else {
					newType2 = newEntityContent2;
				}

				String methodName1 = getMethodName(rootEntityContent1);
				String methodName2 = getMethodName(rootEntityContent2);

				if (oldType1.equals(oldType2) && newType1.equals(newType2)) {
					if (!methodName1.equals(methodName2)) {
						// System.out.println(changeType + " , " +
						// changedEntityType);
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
			String type1 = tokens1[1];
			String type2 = tokens2[1];

			String methodName1 = getMethodName(rootEntityContent1);
			String methodName2 = getMethodName(rootEntityContent2);

			if (type1.equals(type2)) {
				if (!methodName1.equals(methodName2)) {
					// System.out.println(changeType + " , " +
					// changedEntityType);
					// System.out.println(co1);
					// System.out.println(co2);
					// System.out.println();
				}
				return true;
			}
		}
		return false;
	}

	private String getMethodName(String content) {
		int idx = content.lastIndexOf("(");
		return content.substring(content.lastIndexOf(".", idx) + 1, idx);
	}
}
