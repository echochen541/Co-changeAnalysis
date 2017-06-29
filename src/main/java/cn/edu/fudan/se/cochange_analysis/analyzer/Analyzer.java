package cn.edu.fudan.se.cochange_analysis.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class Analyzer {
	public static void main(String[] args) {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		List<String> result = new ArrayList<String>();
		TokenStream stream = null;
		try {
			stream = analyzer.tokenStream(null,
					"CAMEL-11072 Remove non-Maven plugin related exe......cutions from Salesforce Maven POM This removes `bundle-jar` execution from `maven-jar-plugin` plugin, and disables all known executions of `maven-bundle-plugin` and `camel-package-maven-plugin` -- these are not relevant to this module as it is a Maven plugin not a Camel component.");
			stream.reset();
			while (stream.incrementToken()) {
				result.add(stream.getAttribute(CharTermAttribute.class).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			analyzer.close();
		}
		System.out.println(result);

		SnowballStemmer stemmer = new englishStemmer();
		for (String string : result) {
			stemmer.setCurrent(string);
			stemmer.stem();
			String stemmed = stemmer.getCurrent();
			System.out.print(stemmed + " ");
		}
	}
}
