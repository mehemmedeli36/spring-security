package com.example.service.Impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import com.example.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired 
	private EmployeeRepository employeeRepository;

	@Override
	public Employee findById(UUID id) {
	return	employeeRepository.findById(id).orElse(null);
	}

	@Override
	public Employee add(Employee employee) {
		
		return employeeRepository.save(employee);
	}
	
	
}
