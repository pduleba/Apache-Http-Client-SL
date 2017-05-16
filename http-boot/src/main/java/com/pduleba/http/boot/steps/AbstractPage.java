package com.pduleba.http.boot.steps;

import java.util.Objects;

import org.slf4j.Logger;

import com.pduleba.http.boot.HttpContext;

public abstract class AbstractPage implements HttpPage {

	final Logger LOG;
	private HttpContext ctx;
	
	AbstractPage(Logger lOG) {
		super();
		LOG = lOG;
	}

	@Override
	public void run(HttpContext ctx) throws Exception {
		try {
			this.ctx = ctx;
			LOG.info("Connecting...");
			execute();
			LOG.info("Connecting... :: SUCCESS");
		} catch (Exception e) {
			LOG.error("Connecting... :: ERROR", e);
			throw e;
		}
	}

	void set(String key, String value) {
		Objects.requireNonNull(key, "key is null");
		Objects.requireNonNull(value, "value is null");
		ctx.setAttribute(key, value);
	}

	String get(String key) {
		return ctx.getAttribute(key);
	}
	
	abstract void execute() throws Exception;
	
}
