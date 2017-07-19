package cn.edu.fudan.se.cochange_analysis.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		 System.out.println("Finished");
		
        
       
	}
	public void parse(String fileName,String dir){
		FileInputStream fis;
		FileOutputStream fos;
		Stack<String> stack=new Stack<String>();
		List<String> groupLines=new ArrayList<String>();
		try {
			fis = new FileInputStream(new File(dir+File.separator+fileName));
			fos=new FileOutputStream(new File(dir+File.separator+fileName.split("\\.")[0]+".csv"));
			Scanner sc=new Scanner(fis);
			int groupFlag=0;
			
			while(sc.hasNextLine()){
				String line=sc.nextLine();
				line=line.trim();
				if(line.startsWith("<cluster")){
					stack.push(line);continue;
				}
				if(line.startsWith("<group")){
					stack.push(line);continue;
				}
				if(line.startsWith("</group>")){
					stack.pop();
					if(groupFlag!=0){
						if(groupFlag>1){
							for(String tmp:groupLines){
								if(tmp.startsWith("<group")){
									String res=match(tmp,"group","name")+",\n";
									fos.write(res.getBytes());
								}else if(tmp.startsWith("<item")){
									String res=match(tmp,"item","name")+",\n";
									fos.write(res.getBytes());
								}
								
							}
							fos.write("\n".getBytes());
						}
						groupLines.clear();
						groupFlag=0;
//						System.out.println(line);
					}
					continue;
				}
				if(line.startsWith("<item")){
					String group=stack.peek();
					if(groupFlag==0){
						groupLines.add(group);
//						System.out.println(group);
						
					}
					groupFlag++;
					groupLines.add(line);
//					System.out.println(line);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static String match(String source, String element, String attr) {
        String result=null;
        String reg = "<" + element + " " + attr +"="+ "\"(.*)\"";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result=r;
            return result;
        }
        return result;
    }
}
