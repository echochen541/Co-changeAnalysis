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
import cn.edu.fudan.se.cochange_analysis.git.dao.SnapshotFileDAO;

public class DSMGenerator {
	private GitRepository gitRepository;

	public DSMGenerator() {
	}

	public DSMGenerator(GitRepository gitRepository) {
		this.gitRepository = gitRepository;
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

				int compareResult = changeType1.compareTo(changeType2);

				String relationType = null;

				if (compareResult <= 0) {
					relationType = changeType1 + "--" + changeType2;
					if (relationIndexMap.containsKey(relationType)) {
						if (dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)] == null) {
							dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)] = new StringBuilder(
									emptyCell.toString());
						}
						
						dsmMatrix[fileIndexMap.get(fileName1)][fileIndexMap.get(fileName2)]
								.setCharAt(relationIndexMap.get(relationType), '1');
					}
				} else {
					relationType = changeType2 + "--" + changeType1;
					if (relationIndexMap.containsKey(relationType)) {
						if (dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] == null) {
							dsmMatrix[fileIndexMap.get(fileName2)][fileIndexMap.get(fileName1)] = new StringBuilder(
									emptyCell.toString());
						}
						
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

	public List<String> getSnapshotFile(String release) {
		List<String> fileList = SnapshotFileDAO.selectFileByRepositoryIdAndRelease(gitRepository.getRepositoryId(),
				release);
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

						path = path.replace("\\", "/");

						if (FileUtils.isTestFile(path)) {
							break;
						}

						String parsedName = FileUtils.parseFilePath(path, gitRepositoryName);

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