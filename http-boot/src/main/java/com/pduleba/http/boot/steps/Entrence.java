package com.pduleba.http.boot.steps;

import org.apache.http.client.fluent.Request;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pduleba.http.boot.steps.support.HTMLExtractorService;

@Component
public class Entrence extends AbstractPage {

	final static String KEY_BASE_URL = "key.base.url";
	final static String KEY_BASE64_URL_PART = "key.base64.url.part";

	private HTMLExtractorService extractor;

	private String url;

	@Autowired
	public Entrence(HTMLExtractorService extractor, @Value("${url.entrence}") String url) {
		super(LoggerFactory.getLogger(Entrence.class));
		this.extractor = extractor;
		this.url = url;
	}

	@Override
	public void execute() throws Exception {
		// given
		set(KEY_BASE_URL, url);
		
		// when
		String html = Request.Get(url).execute().returnContent().asString();
		
		// then
		set(KEY_BASE64_URL_PART, extractor.extract(html, "span.guess-what"));
	}

}
