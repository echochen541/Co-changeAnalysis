package cn.edu.fudan.se.cochange_analysis.preprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import cn.edu.fudan.se.cochange_analysis.git.bean.GitCommit;
import cn.edu.fudan.se.cochange_analysis.git.dao.GitCommitDAO;

public class Preprocessor {
	private Analyzer analyzer;
	private SnowballStemmer stemmer;
	private HashSet<String> keywords;

	public Preprocessor() {
		analyzer = new StandardAnalyzer();
		stemmer = new englishStemmer();
		initKeywordsSet();
	}

	private void initKeywordsSet() {
		String correctiveText = "bugfix bug cause error failure fix miss null warn wrong bad correct incorrect problem opps valid invalid fail bad dump except;";
		String adaptiveText = "add new create feature function appropriate available change compatibility config configuration text current default easier future information internal method necessary old patch protocol provide release replace require security simple structure switch context trunk useful user version install introduce faster init;";
		String perfectiveText = "clean cleanup consistent declaration definition documentation move prototype remove static style unused variable whitespace header include dead inefficient useless;";
		List<String> stemmedTokens = this.preprocess(correctiveText + adaptiveText + perfectiveText);
		keywords = new HashSet<String>(stemmedTokens);
		// System.out.println(keywords);
	}

	public static void main(String[] args) {
		Preprocessor preprocessor = new Preprocessor();
		for (int repositoryId = 1; repositoryId <= 6; repositoryId++) {
			List<GitCommit> gitCommits = GitCommitDAO.selectByRepositoryId(repositoryId);
			int i = 0;
			for (GitCommit gitCommit : gitCommits) {
				System.out.println(i++);
				String message = gitCommit.getShortMessage();
				System.out.println(gitCommit.getCommitId());
				System.out.println(message);
				List<String> stemmedTokens = preprocessor.preprocess(message);
				System.out.println(stemmedTokens);
				List<String> prunedTokens = preprocessor.prune(stemmedTokens, 5);
				System.out.println(prunedTokens);
				System.out.println();
			}
		}
	}

	public List<String> preprocess(String text) {
		List<String> stemmedTokens = new ArrayList<String>();
		TokenStream stream = null;
		stream = analyzer.tokenStream(null, text);
		try {
			stream.reset();
			while (stream.incrementToken()) {
				String token = stream.getAttribute(CharTermAttribute.class).toString();
				stemmer.setCurrent(token);
				stemmer.stem();
				String stemmed = stemmer.getCurrent();
				stemmedTokens.add(stemmed);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stemmedTokens;
	}

	public List<String> prune(List<String> stemmedTokens, int threshold) {
		List<String> prunedTokens = new ArrayList<String>(stemmedTokens);
		if (prunedTokens.size() >= threshold)
			return prunedTokens;

		Iterator<String> lt = prunedTokens.iterator();
		while (lt.hasNext()) {
			String token = lt.next();
			if (keywords.contains(token))
				return prunedTokens;
		}
		return new ArrayList<String>();
	}
}
