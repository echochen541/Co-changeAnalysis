package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class Parse2Tree {
	class TreeNode{
		TreeNode parent;
		List<TreeNode> children;
		public TreeNode(String line){
			this.children=new ArrayList<TreeNode>();
			this.orignalContent=line;
			extractInfo(line);
		}
		
		
		String orignalContent;
		String tagName;
		String name;
		public void extractInfo(String line){
			String[] tmp=line.split(" ");
			this.tagName=tmp[0].substring(1);
			if(tmp.length==2&&tmp[1].startsWith("name")){
				this.name=match(tmp[1]);
			}
		}
		public String toString(){
			return orignalContent;
		}

		
	}
	private GitRepository repository;
	
	public Parse2Tree(GitRepository repository) {
		super();
		this.repository = repository;
	}


	
	public static void main(String[] args) {
		GitRepository gitRepository = new GitRepository(1, "camel",
				"D:/echo/lab/research/co-change/projects/camel/.git");
		Parse2Tree a = new Parse2Tree(gitRepository);
		String inputDir = "D:\\2017-07-20\\data\\cluster";
		a.parse("camel_32_20_cluster..clsx", inputDir);
		System.out.println("Finished");
	}
	
	public TreeNode parse(String fileName, String dir) {
		FileInputStream fis;
		Stack<TreeNode> treeStack=new Stack<TreeNode>();
		TreeNode root=null;
		try {
			fis = new FileInputStream(new File(dir + File.separator + fileName));
			Scanner sc = new Scanner(fis);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line = line.trim();
				if(line.startsWith("</")){
					treeStack.pop();
				}
				else if(line.startsWith("<item")){
					TreeNode parent=treeStack.peek();
					TreeNode newNode=new TreeNode(line);
					newNode.parent=parent;
					parent.children.add(newNode);
				}
				else{
					TreeNode newNode=new TreeNode(line);
					int size=treeStack.size();
					if(size==0){
						root=newNode;
					}else{
						TreeNode parent=treeStack.peek();
						newNode.parent=parent;
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
	

}
