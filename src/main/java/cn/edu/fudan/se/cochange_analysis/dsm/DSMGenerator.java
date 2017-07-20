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

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.edu.fudan.se.cochange_analysis.file.util.FileUtils;
import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeRelationCount;
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
		String inputDir1 = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-count";
		String outputDir1 = "D:/echo/lab/research/co-change/ICSE-2018/data/change-relation-dsm";
		generator.generateTopNRelationTopNFilePairDSM(inputDir1, outputDir1, 32, 20);

		String inputDir2 = "D:/echo/lab/research/co-change/ICSE-2018/data/hotspot-dsm";
		String outputDir2 = inputDir2;
		generator.generateHotspotDSM(inputDir2, outputDir2);
	}

	public void generateTopNRelationTopNFilePairDSM(String inputdir, String outputdir, int threshold1, int threshold2) {
		String gitRepositoryName = gitRepository.getRepositoryName();
		int gitRepositoryId = gitRepository.getRepositoryId();
		String intputFileName = inputdir + "/" + gitRepository.getRepositoryName() + "_change-relation-count-rank.csv";

		// get top n relation
		List<String> relationList = getTopNRelation(intputFileName, threshold1);
		System.out.println("RelationSize: " + relationList.size());
		StringBuilder emptyCell = new StringBuilder();
		for (int j = 0; j < relationList.size(); j++) {
			emptyCell.append("0");
		}
		Map<String, Integer> relationIndexMap = new HashMap<String, Integer>();

		// change relation buffer
		StringBuilder relationBuilder = new StringBuilder();
		relationBuilder.append("[");
		for (int i = 0; i < relationList.size(); i++) {
			relationIndexMap.put(relationList.get(i), i);
			relationBuilder.append(relationList.get(i) + ",");
		}
		relationBuilder.deleteCharAt(relationBuilder.length() - 1);
		relationBuilder.append("]\n");

		// get top n file
		List<FilePairCount> filePairCountList = FilePairCountDAO.selectByRepositoryIdAndCount(gitRepositoryId,
				threshold2);
		List<String> fileList = getTopNFile(filePairCountList);
		Map<String, Integer> fileIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < fileList.size(); index++) {
			fileIndexMap.put(fileList.get(index), index);
		}
		System.out.println("FileSize:" + fileList.size());

		// file size buffer
		relationBuilder.append(fileList.size());
		relationBuilder.append("\n");

		StringBuilder[][] dsmMatrix = new StringBuilder[fileList.size()][fileList.size()];
		// int k = 0;
		for (FilePairCount filePairCount : filePairCountList) {
			String filePair = filePairCount.getFilePair();
			String[] tokens = filePair.split("\\|\\|");
			String fileName1 = tokens[0];
			String fileName2 = tokens[1];
			List<ChangeRelationCount> changeRelationCountList = ChangeRelationCountDAO
					.selectByRepositoryIdAndFilePair(gitRepositoryId, filePair);
			// System.out.println(++k + " : " + filePair);
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
						if (dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)] == null)
							dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)] = new StringBuilder(
									emptyCell.toString());

						dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)]
								.setCharAt(relationIndexMap.get(relationType), '1');

						if (compareResult == 0) {
							if (dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] == null)
								dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] = new StringBuilder(
										emptyCell.toString());

							dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)]
									.setCharAt(relationIndexMap.get(relationType), '1');
						}

					}
				} else {
					relationType = change2 + "--" + change1;
					if (relationIndexMap.containsKey(relationType)) {
						if (dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] == null)
							dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] = new StringBuilder(
									emptyCell.toString());

						dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)]
								.setCharAt(relationIndexMap.get(relationType), '1');
					}
				}
			}
		}

		StringBuilder matrixBuilder = new StringBuilder();
		for (int i = 0; i < dsmMatrix.length; i++) {
			for (int j = 0; j < dsmMatrix[0].length; j++) {
				if (dsmMatrix[i][j] == null) {
					matrixBuilder.append("0 ");
				} else {
					matrixBuilder.append(dsmMatrix[i][j].toString() + " ");
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
			String outputPath = outputdir + "/" + gitRepositoryName + "_" + threshold1 + "_" + threshold2 + ".dsm";
			System.out.println(outputPath);
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

	public List<String> getTopNRelation(String intputFileName, int threshold1) {
		List<String> relationList = new ArrayList<String>();
		FileInputStream fis = null;
		InputStreamReader isw = null;
		BufferedReader br = null;

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
		return relationList;
	}

	public List<String> getTopNFile(List<FilePairCount> filePairCountList) {
		Set<String> fileSet = new HashSet<String>();
		for (FilePairCount filePairItem : filePairCountList) {
			String[] filePairs = filePairItem.getFilePair().split("\\|\\|");
			fileSet.add(filePairs[0]);
			fileSet.add(filePairs[1]);
		}

		List<String> fileList = new ArrayList<String>(fileSet);
		Collections.sort(fileList);
		return fileList;
	}

	public GitRepository getGitRepository() {
		return gitRepository;
	}

	public void setGitRepository(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
	}

	private void generateHotspotDSM(String inputDir, String outputDir) {
		String gitRepositoryName = gitRepository.getRepositoryName();

		String xmlPath = inputDir + "/" + gitRepository.getRepositoryName() + "_FileDependencyCytoscape.xml";
		File f = new File(xmlPath);

		String[] types = { "Extend", "Typed", "Import", "Call", "Create", "Cast", "Implement", "Set", "Modify", "Use",
				"Throw" };
		List<String> typeList = Arrays.asList(types);
		List<String> fileList = new ArrayList<String>();

		GenerateDSM structureDSM = null;
		GenerateDSM historyDSM = null;

		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(f);
			Element root = document.getRootElement();
			List<Element> nodes = root.elements("node");
			// parse each node tag
			for (Iterator<Element> it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				int id = Integer.parseInt(elm.attribute("id").getValue());
				List<Element> children = elm.elements("att");
				for (Element child : children) {
					Attribute name = child.attribute("name");
					if (name.getValue().equals("longName")) {
						String path = child.attribute("value").getValue();
						// System.out.println(id + "\t" + parseName(path));
						if (path.contains("\\test\\") || path.contains("\\tester\\")) {
							break;
						}
						path = path.replace("\\", "/");
						String parsedName = FileUtils.parseFilePath(path, gitRepositoryName);

						String startsWithStr = "org/apache/";
						if (gitRepositoryName.equals("hbase")) {
							startsWithStr += "hadoop/hbase";
						} else {
							startsWithStr += gitRepositoryName;
						}
						if (!parsedName.startsWith(startsWithStr)) {
							break;
						}
						hm.put(id, parsedName);
						fileList.add(parsedName);
					}
				}
			}
			Collections.sort(fileList);
			System.out.println("FileListSize:" + fileList.size());
			structureDSM = new GenerateDSM(typeList, fileList);

			nodes = root.elements("edge");
			for (Iterator<Element> it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();

				int source = Integer.parseInt(elm.attribute("source").getValue());
				int target = Integer.parseInt(elm.attribute("target").getValue());
				if (!hm.containsKey(source) || !hm.containsKey(target)) {
					continue;
				}
				List<Element> children = elm.elements("att");
				for (Element child : children) {
					Attribute name = child.attribute("name");
					if (name.getValue().equals("dependency kind")) {
						String kind = child.attribute("value").getValue();
						String[] mTypeList = kind.split(", ");
						List<String> mTypeList2 = Arrays.asList(mTypeList);
						// System.out.println("source=\"" + source + "\"
						// target=\"" + target + "\"");
						structureDSM.setFileStructureRelationType(hm.get(source), hm.get(target), mTypeList2);
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		structureDSM.write2StructureDSM(outputDir + "/" + gitRepositoryName + "_sdsm.dsm");

		historyDSM = new GenerateDSM(fileList);
		int gitRepositoryId = gitRepository.getRepositoryId();
		List<FilePairCount> dataList = FilePairCountDAO.selectByRepositoryIdAndCount(gitRepositoryId, 0);
		for (FilePairCount item : dataList) {
			String[] files = item.getFilePair().split("\\|\\|");
			// System.out.println(files[0] + " , " + files[1]);
			historyDSM.setFileHistoryRelationCount(files[0], files[1], item.getCount());
			historyDSM.setFileHistoryRelationCount(files[1], files[0], item.getCount());
			// System.out.println();
		}
		historyDSM.write2HistoryDSM(outputDir + "/" + gitRepositoryName + "_sdsm-history.dsm");
	}
}