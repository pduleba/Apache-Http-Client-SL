package com.pduleba.http.boot.steps;

import com.pduleba.http.boot.HttpContext;

public interface HttpPage {

	String KEY_NEXT_PAGE_URL = "key.next.page.url";
	String KEY_HEADER_SPECIAL_NAME = "key.header.special.name";
	String KEY_HEADER_SPECIAL_VALUE = "key.header.special.value";
	
	void run(HttpContext ctx) throws Exception;
}
