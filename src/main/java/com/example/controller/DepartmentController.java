package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/department")
public class DepartmentController {

	@GetMapping(path = "/test")
	public boolean test() {
		return true;
	}
}
