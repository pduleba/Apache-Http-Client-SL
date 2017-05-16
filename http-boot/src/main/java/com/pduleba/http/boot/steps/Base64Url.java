package com.pduleba.http.boot.steps;

import static com.pduleba.http.boot.steps.Entrence.KEY_BASE64_URL_PART;
import static com.pduleba.http.boot.steps.Entrence.KEY_BASE_URL;
import static java.text.MessageFormat.format;

import java.util.Base64;
import java.util.Base64.Decoder;

import org.apache.http.client.fluent.Request;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pduleba.http.boot.steps.support.HTMLExtractorService;

@Component
public class Base64Url extends AbstractPage {

	private final static String HEADER_SPECIAL_NAME = "X-0x0ACE-Key";
	
	private HTMLExtractorService extractor;

	private Decoder decoder;

	@Autowired
	public Base64Url(HTMLExtractorService extractor) {
		super(LoggerFactory.getLogger(Base64Url.class));
		this.extractor = extractor;
		this.decoder = Base64.getDecoder();
	}

	@Override
	public void execute() throws Exception {
		// given
		String baseUrl = get(KEY_BASE_URL);
		String base64UrlPart = get(KEY_BASE64_URL_PART);

		// when
		String urlPart = new String(decoder.decode(base64UrlPart));
		String nextPageUrl = format("{0}{1}", baseUrl, urlPart);
		String html = Request.Get(nextPageUrl).execute().returnContent().asString();

		// then
		set(KEY_HEADER_SPECIAL_NAME, HEADER_SPECIAL_NAME);
		set(KEY_HEADER_SPECIAL_VALUE, extractor.extract(html, "html body span b", 1));
		set(KEY_NEXT_PAGE_URL, format("http://{0}", extractor.extract(html, "html body span.next-loc")));
	}

}
