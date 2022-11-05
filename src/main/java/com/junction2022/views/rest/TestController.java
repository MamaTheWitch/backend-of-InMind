package com.junction2022.views.rest;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Nam Vu
 *
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping
	public String testGet() {
		return "Hello, World";
	}


	@GetMapping("/test-get")
	public String testGet(
			@RequestParam final String name) {
		return "Hello, " + name + "!";
	}

	@PostMapping(path="/test-post/{id}")
	public Map<String, Object> testPost(
			@PathVariable final Long id,
			@RequestParam final String name,
			@RequestHeader final HttpHeaders headers,
			final HttpServletRequest request) {

		final Map<String, Object> map = new TreeMap<>();
		map.put("id", id.toString());
		map.put("name", name);
		map.put("media-types",
				headers.getAccept()
					.stream()
					.sorted()
					.toList()
					.toString());
		map.put("uri", request.getRequestURL().toString() + "?" + request.getQueryString());


		final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		map.put("bimlinker.requestBaseUri", baseUrl);
		map.put("bimlinker.requestSchema", request.getScheme());
		map.put("bimlinker.requestPathInfo", request.getPathInfo());
		map.put("bimlinker.requestContextPath", request.getContextPath());
		map.put("bimlinker.requestPort", request.getLocalPort());

		return map;
	}



}