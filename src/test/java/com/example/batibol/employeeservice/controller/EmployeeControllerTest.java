package com.example.batibol.employeeservice.controller;

import com.example.batibol.employeeservice.assembler.EmployeeModelAssembler;
import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.service.ServiceEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class EmployeeControllerTest {

    @Autowired
    private EmployeeController underTest;
    @Mock
    private ServiceEmployee serviceEmployee;

    @Mock
    private EmployeeModelAssembler assembler;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new EmployeeController(serviceEmployee, assembler);
    }

    @Test
    void itShouldShouldAddNewEmployee() {
        //Given
        Employee employee = buildEmployee();
        EntityModel<Employee> employeeEntityModel = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
        when(serviceEmployee.addNewEmployee(employee)).thenReturn(employee);
        when(assembler.toModel(employee)).thenReturn(employeeEntityModel);
        //When
        ResponseEntity<?> response = underTest.newEmployee(employee);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(employeeEntityModel);

    }

    @Test
    void itShouldDeleteEmployee() {
        //Given
        Employee employee = buildEmployee();
        doNothing().when(serviceEmployee).deleteEmployeeById(employee.getId());
        //When
        ResponseEntity<?> response = underTest.deleteEmployee(employee.getId());
        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void itShouldShouldReplaceEmployee() {
        //Given
        Employee employee = buildEmployee();
        EntityModel<Employee> employeeEntityModel = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
        when(serviceEmployee.getEmployeeById(employee.getId())).thenReturn(employee);
        when(serviceEmployee.addNewEmployee(employee)).thenReturn(employee);
        when(assembler.toModel(employee)).thenReturn(employeeEntityModel);
        //When
        ResponseEntity<?> response = underTest.replaceEmployee(employee.getId(), employee);
        //Then
        assertThat(response.getBody()).isEqualTo(employeeEntityModel);
    }

    @Test
    void itShouldReturnAnSingleEmployee() {
        //Given

        Employee employee = buildEmployee();
        Long id = employee.getId();
        EntityModel<Employee> employeeEntityModel = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
        when(serviceEmployee.getEmployeeById(employee.getId())).thenReturn(employee);
        when(serviceEmployee.getEmployeeById(id)).thenReturn(employee);
        when(assembler.toModel(employee)).thenReturn(employeeEntityModel);
        //When
        EntityModel<Employee> response = underTest.getEmployeeById(id);
        //Then
        assertThat(response).isEqualTo(employeeEntityModel);

    }

    @Test
    void itShouldReturnAllEmployees() {
        //Given
        List<Employee> employees = new ArrayList<>();

        Employee employee = buildEmployee();
        employees.add(employee);
        employees.add(employee);
        EntityModel<Employee> employeeEntityModel = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));

        when(serviceEmployee.getAllEmployees()).thenReturn(employees);
        when(assembler.toModel(employee)).thenReturn(employeeEntityModel);
        List<EntityModel<Employee>> entityModelList = serviceEmployee.getAllEmployees().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Employee>> employeeCollectionModel = CollectionModel.of(entityModelList,
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
        //When
        CollectionModel<EntityModel<Employee>> response = underTest.getAllEmployees();
        //Then
        assertThat(response).isEqualTo(employeeCollectionModel);

    }

    public Employee buildEmployee() {
        return new Employee(
                1L,
                "Joe Doe",
                "HR"
        );
    }
}