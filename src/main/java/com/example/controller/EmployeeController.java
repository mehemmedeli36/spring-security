package com.example.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Employee;
import com.example.service.EmployeeService;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	@GetMapping(path = "/{id}")
	public Employee get(@PathVariable UUID id) {
		return employeeService.findById(id);
	}
	
	@PostMapping
	public Employee add(@RequestBody Employee employee) {
		return employeeService.add(employee);
	}
}
