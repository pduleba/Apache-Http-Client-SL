package com.pduleba.http.boot.steps.support;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HTMLExtractorService {

	public static final Logger LOG = LoggerFactory.getLogger(HTMLExtractorService.class);

	public String extract(String html, String cssQuery) throws IOException {
		return extract(html, cssQuery, 0);
	}
	
	public String extract(String html, String cssQuery, int index) throws IOException {
		Element e = element(html, cssQuery, index);
		return e == null ? null : e.text();
	}
	
	public Element element(String html, String cssQuery) throws IOException {
		return element(html, cssQuery, 0);
	}
	
	public Element element(String html, String cssQuery, int index) throws IOException {
		try {
			Document doc = Jsoup.parse(html);
			
			Elements results = doc.select(cssQuery);
			return results.size() > index ? results.get(index) : null;
		} catch (Exception e) {
			LOG.error("Unable to search for {} cssQuery in html document", cssQuery);
			throw new IOException(e);
		}
	}

}
