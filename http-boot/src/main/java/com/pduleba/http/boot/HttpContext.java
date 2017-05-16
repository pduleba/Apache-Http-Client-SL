package com.pduleba.http.boot;

import java.util.HashMap;

public class HttpContext {

	private HashMap<String, String> bag = new HashMap<>();
	
	public String getAttribute(final String key) {
		return bag.get(key);
	}

	public void setAttribute(String key, String value) {
		bag.put(key, value);
	}

}
