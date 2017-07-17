package cn.edu.fudan.se.cochange_analysis.dsm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationUnique;
import cn.edu.fudan.se.cochange_analysis.git.bean.FilePairCount;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeRelationCountDAO;
import cn.edu.fudan.se.cochange_analysis.git.dao.FilePairCountDAO;

public class DSMGenerator {
	private GitRepository gitRepository;

	public DSMGenerator() {
	}

	public DSMGenerator(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		DSMGenerator generator = new DSMGenerator(gitRepository);
		String inputdir = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-count-rank";
		String outputdir = "D:/echo/lab/research/co-change/ICSE-2018/data/dsm";
		generator.generateTopNRelationTopNFilePairDSM(inputdir, outputdir, 32, 20);
	}

	public void generateTopNRelationTopNFilePairDSM(String inputdir, String outputdir, int threshold1, int threshold2) {
		String repositoryName = gitRepository.getRepositoryName();
		int repositoryId = gitRepository.getRepositoryId();
		String intputFileName = inputdir + "/" + gitRepository.getRepositoryName() + "_change-relation-count-rank.csv";
		FileInputStream fis = null;
		InputStreamReader isw = null;
		BufferedReader br = null;
		List<String> relationList = new ArrayList<String>();

		try {
			fis = new FileInputStream(new File(intputFileName));
			isw = new InputStreamReader(fis);
			br = new BufferedReader(isw);
			for (int i = 0; i < threshold1; i++) {
				String line = br.readLine();
				String[] tokens = line.split(",");
				relationList.add(tokens[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Collections.sort(relationList);
		System.out.println("RelationSize: " + relationList.size());
		StringBuilder matrixCell = new StringBuilder();
		for (int j = 0; j < relationList.size(); j++) {
			matrixCell.append("0");
		}

		Map<String, Integer> relationIndexMap = new HashMap<String, Integer>();
		StringBuilder relationBuilder = new StringBuilder();
		relationBuilder.append("[");
		for (int m = 0; m < relationList.size(); m++) {
			relationIndexMap.put(relationList.get(m), m);
			relationBuilder.append(relationList.get(m) + ",");
		}
		relationBuilder.deleteCharAt(relationBuilder.length() - 1);
		relationBuilder.append("]\n");

		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(repositoryId, threshold2);
		Set<String> fileSet = new HashSet<String>();
		for (FilePairCount filePairItem : filePairCountList) {
			String[] filePairs = filePairItem.getFilePair().split("\\|\\|");
			fileSet.add(filePairs[0]);
			fileSet.add(filePairs[1]);
		}

		List<String> fileList = new ArrayList<String>(fileSet);
		Collections.sort(fileList);
		Map<String, Integer> fileIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < fileList.size(); index++) {
			fileIndexMap.put(fileList.get(index), index);
		}

		System.out.println("FileSize:" + fileList.size());

		relationBuilder.append(fileList.size());
		relationBuilder.append("\n");

		StringBuilder[][] dsmMatrix = new StringBuilder[fileList.size()][fileList.size()];

		for (FilePairCount filePairCount : filePairCountList) {
			String filePair = filePairCount.getFilePair();
			String[] tokens = filePair.split("\\|\\|");
			String fileName1 = tokens[0];
			String fileName2 = tokens[1];
			List<ChangeRelationCount> changeRelationCountList = ChangeRelationCountDAO
					.selectByRepositoryIdAndFilePair(repositoryId, filePair);
			// System.out.println(changeRelationCountList.size());

			for (ChangeRelationCount changeRelationCount : changeRelationCountList) {
				String changeType1 = changeRelationCount.getChangeType1();
				String changeType2 = changeRelationCount.getChangeType2();
				String changedEntityType1 = changeRelationCount.getChangedEntityType1();
				String changedEntityType2 = changeRelationCount.getChangedEntityType2();

				String change1 = changeType1 + "(" + changedEntityType1 + ")";
				String change2 = changeType2 + "(" + changedEntityType2 + ")";
				int compareResult = change1.compareTo(change2);

				String relationType = null;

				if (compareResult <= 0) {
					relationType = change1 + "--" + change2;

					if (relationIndexMap.containsKey(relationType)) {
						if (dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)] == null) {
							dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)] = new StringBuilder(
									matrixCell.toString());
						}
						dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)]
								.setCharAt(relationIndexMap.get(relationType), '1');
					}
				} else {
					relationType = change2 + "--" + change1;

					if (relationIndexMap.containsKey(relationType)) {
						if (dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] == null) {
							dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] = new StringBuilder(
									matrixCell.toString());
						}
						dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)]
								.setCharAt(relationIndexMap.get(relationType), '1');
					}
				}
			}
		}

		StringBuilder matrixBuilder = new StringBuilder();
		for (int m = 0; m < dsmMatrix.length; m++) {
			for (int n = 0; n < dsmMatrix[0].length; n++) {
				if (dsmMatrix[m][n] == null) {
					matrixBuilder.append("0 ");
				} else {
					matrixBuilder.append(dsmMatrix[m][n].toString() + " ");
				}
			}
			matrixBuilder.deleteCharAt(matrixBuilder.length() - 1);
			matrixBuilder.append("\n");
		}
		StringBuilder fileListBuilder = new StringBuilder();
		for (String fileName : fileList) {
			fileListBuilder.append(fileName + "\n");
		}

		try {
			String outputPath = outputdir + "/" + repositoryName + "_" + threshold1 + "_" + threshold2 + ".dsm";
			// System.out.println(outputPath);
			File f = new File(outputPath);
			if (!f.exists())
				f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(relationBuilder.toString().getBytes());
			fos.write(matrixBuilder.toString().getBytes());
			fos.write(fileListBuilder.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GitRepository getGitRepository() {
		return gitRepository;
	}

	public void setGitRepository(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}
}