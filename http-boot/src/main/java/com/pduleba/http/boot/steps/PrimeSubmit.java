package com.pduleba.http.boot.steps;

import static com.pduleba.http.boot.steps.PrimeInit.KEY_CHALLENGE_VALUE;
import static com.pduleba.http.boot.steps.PrimeInit.KEY_INPUT_SOLUTION_NAME;
import static com.pduleba.http.boot.steps.PrimeInit.KEY_INPUT_VERIFICATION_NAME;
import static com.pduleba.http.boot.steps.PrimeInit.KEY_INPUT_VERIFICATION_VALUE;
import static java.lang.Integer.valueOf;

import java.math.BigInteger;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pduleba.http.boot.steps.support.RegexExtractorService;

@Component
public class PrimeSubmit extends AbstractPage {

	private static final String CHALLENGE_VALUE_REGEX = "\\[(\\d+)(.*?)(\\d+)\\]";
	private static final String GOOD_JOB_REGEX = "good job. let's see @ (.*)";
	private static final int PRIME_CERTAINTY = 100; // should be enough

	private RegexExtractorService extractor;

	@Autowired
	public PrimeSubmit(RegexExtractorService extractor) {
		super(LoggerFactory.getLogger(PrimeSubmit.class));
		this.extractor = extractor;
	}

	@Override
	public void execute() throws Exception {

		// given
		String url = get(KEY_NEXT_PAGE_URL);
		String headerValue = get(KEY_HEADER_SPECIAL_VALUE);
		String headerKey = get(KEY_HEADER_SPECIAL_NAME);
		String challenge = get(KEY_CHALLENGE_VALUE);
		String verificationName = get(KEY_INPUT_VERIFICATION_NAME);
		String verificationValue = get(KEY_INPUT_VERIFICATION_VALUE);
		String solutionName = get(KEY_INPUT_SOLUTION_NAME);

		// when
		String start = extractor.extract(challenge, CHALLENGE_VALUE_REGEX, 1);
		String end = extractor.extract(challenge, CHALLENGE_VALUE_REGEX, 3);
		String solution = getSolution(valueOf(start), valueOf(end));
		String html = Request.Post(url).addHeader(headerKey, headerValue)
				.bodyForm(Form.form().add(verificationName, verificationValue).add(solutionName, solution).build())
				.execute().returnContent().asString();

		// then
		set(KEY_NEXT_PAGE_URL, extractor.extract(html, GOOD_JOB_REGEX, 1));
	}

	private String getSolution(int start, int end) {
		final StringBuilder result = new StringBuilder();
		boolean nextPrime = false;

		for (int i = start + 1; i < end; i++) {
			// skip most obvious
			if (i % 2 == 0)
				continue;
			if (i % 3 == 0)
				continue;
			if (i % 5 == 0)
				continue;

			if (BigInteger.valueOf(i).isProbablePrime(PRIME_CERTAINTY)) {
				if (nextPrime) {
					result.append(",");
				} else {
					nextPrime = true;
				}
				result.append(Integer.toString(i));
			}
		}

		return result.toString();
	}

}
