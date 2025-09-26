package com.example.service;

import java.util.UUID;

import com.example.model.Employee;

public interface EmployeeService {

	Employee findById(UUID id);
	Employee add(Employee employee);
}
