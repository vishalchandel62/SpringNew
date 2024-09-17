package com.credentek.msme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class helloWord {

	@GetMapping({ "/hello" })
	public String firstPage() {
		return "Hello World";
	}
}
