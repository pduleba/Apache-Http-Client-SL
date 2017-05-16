package com.pduleba.http.boot.steps;

import org.apache.http.client.fluent.Request;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pduleba.http.boot.steps.support.HTMLExtractorService;

@Component
public class PrimeInit extends AbstractPage {


	final static String KEY_CHALLENGE_VALUE = "key.challenge.value";
	final static String KEY_INPUT_VERIFICATION_NAME = "key.input.verification.name";
	final static String KEY_INPUT_VERIFICATION_VALUE = "key.input.verification.value";
	final static String KEY_INPUT_SOLUTION_NAME = "key.header.solution.name";

	private HTMLExtractorService extractor;

	@Autowired
	public PrimeInit(HTMLExtractorService extractor) {
		super(LoggerFactory.getLogger(PrimeInit.class));
		this.extractor = extractor;
	}

	@Override
	public void execute() throws Exception {
		// given
		String url = get(KEY_NEXT_PAGE_URL);
		String headerValue = get(KEY_HEADER_SPECIAL_VALUE);
		String headerKey = get(KEY_HEADER_SPECIAL_NAME);

		// when
		String html = Request.Get(url).addHeader(headerKey, headerValue).execute().returnContent().asString();

		// then
		set(KEY_CHALLENGE_VALUE, extractor.extract(html, "span.challenge"));
		
		Element verification = extractor.element(html, "form input");
		set(KEY_INPUT_VERIFICATION_NAME, verification.attr("name"));
		set(KEY_INPUT_VERIFICATION_VALUE, verification.val());
		Element solution = extractor.element(html, "form input", 1);
		set(KEY_INPUT_SOLUTION_NAME, solution.attr("name"));
	}

}
