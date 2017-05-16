package com.pduleba.http.boot.steps.support;

import static java.text.MessageFormat.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegexExtractorService {

	public static final Logger LOG = LoggerFactory.getLogger(RegexExtractorService.class);


	public String extract(String value, String regex, int group) {
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(value);

			String result;

			if (matcher.find()) {
				result = matcher.group(group);
			} else {
				throw new RuntimeException(format("Unable to parse input value {0} using regex {} for group {}", value, regex, group));
			}

			return result;
		} catch (RuntimeException e) {
			LOG.info("Parsing challenge value... :: ERROR", e);
			throw e;
		}
	}

}
