package cn.edu.fudan.se.cochange_analysis.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;

public class JIRACrawler {
	private GitRepository repository;
	private String beginUrl;
	private int pageSize;
	private int totalSize;

	public JIRACrawler() {
	}

	public JIRACrawler(GitRepository repository, String beginUrl, int pageSize, int totalSize) {
		this.repository = repository;
		this.beginUrl = beginUrl;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
	}

	public static void main(String[] args) {
		GitRepository repository = new GitRepository(1, "camel", "D:/echo/lab/research/co-change/projects/camel/.git");
		String beginUrl = "https://issues.apache.org/jira/rest/api/latest/search?jql=";
		int pageSize = 5;
		int totalSize = 3874;

		JIRACrawler crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		crawler.crawl();
	}

	public void crawl() {
		String project = repository.getRepositoryName();
		int pageNum = totalSize / pageSize + 1;
		for (int i = 0; i < pageNum; i++) {
			String urlString = beginUrl + "project=" + project + "%20and%20issuetype=bug&maxResults=" + pageSize
					+ "&startAt=" + i;
			System.out.println(urlString);
			httpRequest(urlString, "GET", null);
			System.exit(0);
		}
	}

	private void httpRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			
			String jsonString = buffer.toString();
			System.out.println(jsonString);
			
		} catch (ConnectException ce) {
			ce.printStackTrace();
			System.out.println("Weixin server connection timed out");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("http request error:{}");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

	public GitRepository getRepository() {
		return repository;
	}

	public void setRepository(GitRepository repository) {
		this.repository = repository;
	}

	public String getBeginUrl() {
		return beginUrl;
	}

	public void setBeginUrl(String beginUrl) {
		this.beginUrl = beginUrl;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
