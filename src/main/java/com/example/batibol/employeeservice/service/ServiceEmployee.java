package com.example.batibol.employeeservice.service;

import com.example.batibol.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ServiceEmployee {
    private EmployeeRepository employeeRepository;

    @Autowired
    public ServiceEmployee(
            EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addNewEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(()->new EmployeeNotFoundException(employeeId));
    }
    public void deleteEmployeeById(Long employeeId) {
        Boolean exists = employeeRepository.existsById(employeeId);
        if (!exists) {
            throw new EmployeeNotFoundException(employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }
}
