package cn.edu.fudan.se.cochange_analysis.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitRepository;
import cn.edu.fudan.se.cochange_analysis.git.bean.IssueBug;
import cn.edu.fudan.se.cochange_analysis.git.dao.IssueBugDAO;

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
		int pageSize = 100;
		int totalSize = 3874;

		JIRACrawler crawler = new JIRACrawler(repository, beginUrl, pageSize, totalSize);
		crawler.crawl();
	}

	public void crawl() {
		String project = repository.getRepositoryName();
		int repositoryId = repository.getRepositoryId();
		int pageNum = totalSize / pageSize + 1;
		for (int i = 0; i < pageNum; i++) {
			String urlString = null;
			if (project.equals("hadoop"))
				urlString = beginUrl
						+ "project%20in%20(HDFS,%20HADOOP,%20MAPREDUCE,%20HDT,%20YARN)%20and%20issuetype=bug&maxResults="
						+ pageSize + "&startAt=" + i * pageSize;
			else
				urlString = beginUrl + "project=" + project + "%20and%20issuetype=bug&maxResults=" + pageSize
						+ "&startAt=" + i * pageSize;

			System.out.println(urlString);
			List<IssueBug> issueBugs = httpRequest(repositoryId, urlString, "GET", null);
			IssueBugDAO.insertBatch(issueBugs);
		}
	}

	private List<IssueBug> httpRequest(int repositoryId, String requestUrl, String requestMethod, String outputStr) {
		List<IssueBug> issueBugs = new ArrayList<IssueBug>();
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
			// //System.out.println(jsonString);

			JSONObject jsonObject = JSONObject.parseObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray("issues");

			// System.out.println(jsonArray.size());

			// System.exit(0);

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject issueObject = jsonArray.getJSONObject(i);
				String key = issueObject.getString("key");
				// System.out.println("key : " + key);

				JSONObject fields = issueObject.getJSONObject("fields");

				String summary = fields.getString("summary");
				// System.out.println("summary : " + summary);

				JSONObject assignee = fields.getJSONObject("assignee");
				String assigneeName = "Unassigned";
				if (assignee != null)
					assigneeName = assignee.getString("displayName");
				// System.out.println("assignee : " + assigneeName);

				JSONObject reporter = fields.getJSONObject("reporter");
				String reporterName = "Unreported";
				if (reporter != null)
					reporterName = reporter.getString("displayName");
				// System.out.println("reporter : " + reporterName);

				JSONObject status = fields.getJSONObject("status");
				String statusName = "";
				if (status != null)
					statusName = status.getString("name");
				// System.out.println("status : " + statusName);

				JSONObject resolution = fields.getJSONObject("resolution");
				String resolutionName = "Unresolved";
				if (resolution != null)
					resolutionName = resolution.getString("name");
				// System.out.println("resolution : " + resolutionName);

				Date created = fields.getDate("created");
				// System.out.println("created : " + created);

				Date updated = fields.getDate("updated");
				// System.out.println("updated : " + updated);
				// System.out.println();

				IssueBug issueBug = new IssueBug(repositoryId, key, summary, assigneeName, reporterName, statusName,
						resolutionName, created, updated);
				issueBugs.add(issueBug);
			}

		} catch (ConnectException ce) {
			ce.printStackTrace();
			System.out.println("jira server connection timed out");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("http request error:{}");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return issueBugs;
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
