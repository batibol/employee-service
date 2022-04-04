package com.example.batibol.employeeservice.controller;

import com.example.batibol.employeeservice.assembler.EmployeeModelAssembler;
import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.service.ServiceEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Service
@RequestMapping("/api/v1")
public class EmployeeController {
    private final ServiceEmployee employeeService;
    private final EmployeeModelAssembler assembler;

    @Autowired
    public EmployeeController(
                              ServiceEmployee employeeService,
                              EmployeeModelAssembler assembler) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<EntityModel<Employee>> employees = employeeService.getAllEmployees().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@Valid @RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(employeeService.addNewEmployee(newEmployee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @GetMapping("/employees/{employeeId}")
    public EntityModel<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{employeeId}")
    ResponseEntity<?> replaceEmployee(@PathVariable("employeeId") Long employeeId, @RequestBody Employee newEmployee) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());
        employeeService.addNewEmployee(employee);
        EntityModel<Employee> entityModel = assembler.toModel(employee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);

    }

    @DeleteMapping("/employees/{employeeId}")
    ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.noContent().build();
    }
}
