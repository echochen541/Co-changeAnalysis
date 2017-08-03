package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.cochange_analysis.dsm.GenerateDSM;
import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class Parse2Tree {
	class TreeNode {
		TreeNode parent;
		List<TreeNode> children;

		public TreeNode(String line) {
			this.children = new ArrayList<TreeNode>();
			this.orignalContent = line;
			extractInfo(line);
		}

		List<Integer> subChildrenIndexList;

		String orignalContent;
		String tagName;
		String name;

		public void extractInfo(String line) {
			String[] tmp = line.split(" ");
			this.tagName = tmp[0].substring(1);
			if(tmp[0].endsWith(">")){
				this.tagName=this.tagName.substring(0, this.tagName.length()-1);
				this.name=this.tagName;
			}
			if (tmp.length >= 2 && tmp[1].startsWith("name")) {
				this.name = match(tmp[1]);
			}
		}

		public String toString() {
			return orignalContent;
		}
	}

	private int[][] dsmMatrixData;
	private List<String> dsmFileList;
	private List<String> clusterFileList;
	private TreeNode rootNode;
	private GitRepository repository;

	public Parse2Tree(GitRepository repository) {
		super();
		this.repository = repository;
	}



	public void parseDSM(String dst) {
		try {
			FileInputStream fis = new FileInputStream(new File(dst));
			Scanner sc = new Scanner(fis);
			sc.nextLine();
			int size = sc.nextInt();
			System.out.println(size);
			int[][] tempMatrix = new int[size][size];
			List<String> fileList = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					int num = 0;
					String tmp = sc.next();
					if (tmp.length() != 1) {
						num = 1;
					}
					tempMatrix[i][j] = num;
				}

			}
			dsmMatrixData = tempMatrix;
			for (int i = 0; i < size; i++) {
				String line = sc.nextLine().trim();
				if (!"".equals(line) && line != null) {
					fileList.add(line);
				}
			}
			dsmFileList = fileList;
			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public TreeNode parse(String dst) {
		FileInputStream fis;
		Stack<TreeNode> treeStack = new Stack<TreeNode>();
		TreeNode root = null;
		try {
			fis = new FileInputStream(new File(dst));
			Scanner sc = new Scanner(fis);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line = line.trim();
				if (line.startsWith("</")) {
					treeStack.pop();
				} else if (line.startsWith("<item")) {
					TreeNode parent = treeStack.peek();
					TreeNode newNode = new TreeNode(line);
					newNode.parent = parent;
					parent.children.add(newNode);
				} else {
					TreeNode newNode = new TreeNode(line);
					int size = treeStack.size();
					if (size == 0) {
						root = newNode;
					} else {
						TreeNode parent = treeStack.peek();
						newNode.parent = parent;
						parent.children.add(newNode);
					}
					treeStack.push(newNode);
				}
			}
			sc.close();
			return root;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String match(String source) {
		String result = null;
		String reg = "name=\"(.*)\"";
		Matcher m = Pattern.compile(reg).matcher(source);
		while (m.find()) {
			String r = m.group(1);
			result = r;
			return result;
		}
		return result;
	}

	public void getFileList(String dst) {
		FileInputStream fis;
		clusterFileList = new ArrayList<String>();
		try {
			fis = new FileInputStream(new File(dst));
			Scanner sc = new Scanner(fis);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line = line.trim();
				if (line.startsWith("<item")) {
					String[] tmp = line.split(" ");
					if (tmp.length >= 2 && tmp[1].startsWith("name")) {
						String fileName = match(tmp[1]);
						this.clusterFileList.add(fileName);
					}
				}
			}
			System.out.println("ClusterSize:"+this.clusterFileList.size());
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int b=0;
		for(String a:this.clusterFileList){
			b++;
			System.out.print(b+" "+a+"\n");
//			System.out.println(a);
			
		}
	}

	private Map<String, Integer> fileIndexMap;

	public void calculate() {
		fileIndexMap = new HashMap<String, Integer>();
		for (int i = 0; i < this.clusterFileList.size(); i++) {
			fileIndexMap.put(this.clusterFileList.get(i), i);
		}
		List<Integer> bigList = dfs(this.rootNode);
	}

	private List<Integer> dfs(TreeNode t) {
		String name = t.name;
		// children
		if (t.tagName.equals("item")) {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(this.fileIndexMap.get(t.name));
			return tmp;
		}
		// non-children
		List<Integer> totalList = new ArrayList<Integer>();
		for (TreeNode tmp : t.children) {
			totalList.addAll(dfs(tmp));
		}
		if (name.startsWith("org")) {
//			System.out.println(name);
			t.subChildrenIndexList = new ArrayList<Integer>(totalList);
		}
		return totalList;

	}



	public void dfsPrint(TreeNode t){
		System.out.print(t.tagName+" "+t.name);
		if(t.subChildrenIndexList!=null){
			for(Integer a:t.subChildrenIndexList){
				System.out.print(a.intValue()+" ");
			}
		}
		System.out.print("\n");
		for(TreeNode t1:t.children){
			dfsPrint(t1);
		}
	}
	public void dfsPick(TreeNode t){
		
		if(t.tagName.equals("group")&&t.name.contains("/")){
			System.out.print(t.tagName+" "+t.name);
			if(t.subChildrenIndexList!=null){
				for(Integer a:t.subChildrenIndexList){
					System.out.print(a.intValue()+" ");
				}
			}
			System.out.print("\n");
			this.pickedGroups.add(t);
		}
		for(TreeNode t1:t.children){
			dfsPick(t1);
		}
	}
	public void printResult(){
		dfsPrint(this.rootNode);
	}
	public List<TreeNode> pickedGroups;
	public void pickGroups(){
		pickedGroups=new ArrayList<TreeNode>();
		dfsPick(this.rootNode);
	}
	private int[] countDenpendencyNum(int root, List<Integer> fileGroup) {
		int numx = 0;
		int numy = 0;
		int x = root;
		for (int fileId : fileGroup) {
			int y = fileId;
			if (this.dsmMatrixData[x][y] == 1) {
				numx++;
			}
			if (this.dsmMatrixData[y][x]==1){
				numy++;
			}
		}
		int [] res={numx,numy};
		return res;
	}
	public void findHotspots(List<TreeNode> list,int t1,int t2){
		for(TreeNode tmp:list){
			int rootId=this.clusterFileList.indexOf(tmp);
			List<Integer> subList=tmp.subChildrenIndexList;
			int[] result=this.countDenpendencyNum(rootId, subList);
			int m=subList.size();
			int numx=result[0];
			int numy=result[1];
			if(m>=t1&&numx>=(m-1)*t2&&numy>=(m-1)*t2){
				System.out.println("XXX");
			}
		}
		
	}
	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		Parse2Tree a = new Parse2Tree(gitRepository);
		String inputDir = "D:\\2017.7.12\\";
		a.rootNode = a.parse(inputDir + "camel_20_top32_cluster..clsx");
		a.getFileList(inputDir + "camel_20_top32_cluster..clsx");
		a.parseDSM("D:\\2017.7.12\\camel_20_top32.dsm");
		a.calculate();
		System.out.println("Finished");
		a.printResult();
		System.out.println("###########################");
		a.pickGroups();
		System.out.println("#################################");
		a.findHotspots(a.pickedGroups,0,0);
		
	}

}
