package com.example.batibol.employeeservice.repository;

import com.example.batibol.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeRepository extends JpaRepository <Employee, Long>{
}
