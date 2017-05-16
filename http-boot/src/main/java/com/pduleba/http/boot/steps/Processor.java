package com.pduleba.http.boot.steps;

import static java.text.MessageFormat.format;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.fluent.Request;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pduleba.http.boot.steps.support.HTMLExtractorService;

@Component
public class Processor extends AbstractPage {

	private HTMLExtractorService extractor;

	@Autowired
	public Processor(HTMLExtractorService extractor) {
		super(LoggerFactory.getLogger(Processor.class));
		this.extractor = extractor;
	}

	@Override
	public void execute() throws Exception {
		// given
		String url = get(KEY_NEXT_PAGE_URL);
		String headerValue = get(KEY_HEADER_SPECIAL_VALUE);
		String headerKey = get(KEY_HEADER_SPECIAL_NAME);
		String baseUrl = url.substring(0, url.lastIndexOf("/"));

		// when
		String html = Request.Get(url).addHeader(headerKey, headerValue).execute().returnContent().asString();
		Element element = extractor.element(html, "body a");
		String binURL = format("{0}{1}", baseUrl, element.attr("href"));
		byte[] binData = Request.Get(binURL).addHeader(headerKey, headerValue).execute().returnContent().asBytes();

		// then
		LOG.info("{}", html);
		LOG.info("BIN(2) ={}", encodeBinString(binData));
		LOG.info("HEX(16)={}", Hex.encodeHexString(binData));
	}

	private String encodeBinString(byte[] binData) {
		StringBuilder result = new StringBuilder();
		for(byte b : binData) {
			result.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
		}
		return result.toString();
	}

}
