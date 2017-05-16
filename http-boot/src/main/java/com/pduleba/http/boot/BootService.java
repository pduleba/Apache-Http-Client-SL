package com.pduleba.http.boot;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pduleba.http.boot.steps.Base64Url;
import com.pduleba.http.boot.steps.Entrence;
import com.pduleba.http.boot.steps.PrimeInit;
import com.pduleba.http.boot.steps.PrimeSubmit;
import com.pduleba.http.boot.steps.Processor;
import com.pduleba.http.boot.steps.HttpPage;

@Service
public class BootService implements HttpBoot {

	public static final Logger LOG = LoggerFactory.getLogger(BootService.class);

	private List<HttpPage> route = new ArrayList<>();

	@Autowired
	public BootService(Entrence entrence, Base64Url base64url, PrimeInit primeInit, PrimeSubmit primeSubmit,
			Processor processor) {
		super();
		route.add(entrence);
		route.add(base64url);
		route.add(primeInit);
		route.add(primeSubmit);
		route.add(processor);
	}

	public void run() throws Exception {
		try {
			LOG.info("Running boot...");

			final HttpContext ctx = new HttpContext();
			for (HttpPage page : route) {
				page.run(ctx);
			}

			LOG.info("Running boot... :: SUCCESS");
		} catch (Exception e) {
			LOG.error("Running boot... :: ERROR");
			throw e;
		}
	}

}
