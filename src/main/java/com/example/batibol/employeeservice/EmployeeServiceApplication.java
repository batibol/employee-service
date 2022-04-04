package com.example.batibol.employeeservice;

import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);

	}
	/*@Bean
	CommandLineRunner commandLineRunner (EmployeeRepository employeeRepository) {
		return args -> {
			employeeRepository.save(new Employee("Peter Kadima", "IT"));
			employeeRepository.save(new Employee("Kevin Ipakala", "Business"));
			employeeRepository.save(new Employee("Jonathan Kembo", "Support"));

		};
	}*/

}
