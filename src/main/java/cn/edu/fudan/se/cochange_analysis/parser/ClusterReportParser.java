package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class ClusterReportParser {
	private GitRepository repository;
	
	public ClusterReportParser(GitRepository repository){
		this.repository=repository;
	}
	
	public static void main(String[] args) {
		 GitRepository gitRepository = new GitRepository(1, "camel",
				 "D:/echo/lab/research/co-change/projects/camel/.git");
		 ClusterReportParser a=new ClusterReportParser(gitRepository);
		 a.parse("camel_32_20_cluster..clsx","D:\\");
	}
	public void parse(String fileName,String dir){
		File f=new File(dir+File.separator+fileName);
		SAXReader reader = new SAXReader();
		Document document;
		try{
			document = reader.read(f);
			Element root = document.getRootElement();
			List<Element> elements=root.elements();
			System.out.println("a");
//			List<Element> nodes = root.elements("node");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}

}
