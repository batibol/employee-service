package com.example.batibol.employeeservice.controller;

import com.example.batibol.employeeservice.assembler.EmployeeModelAssembler;
import com.example.batibol.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Service
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    @Autowired
    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @GetMapping("/employees/{employeeId}")
    public EntityModel<Employee> one(@PathVariable("employeeId") Long employeeId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(()->new EmployeeNotFoundException(employeeId));

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{employeeId}")
    ResponseEntity<?> replaceEmployee(@PathVariable("employeeId") Long employeeId, @RequestBody Employee newEmployee) {
        Boolean exists = repository.existsById(employeeId);
        if (!exists) {
            throw new EmployeeNotFoundException(employeeId);
        }
        Employee employee = repository.findById(employeeId).get();
        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());
        repository.save(employee);
        EntityModel<Employee> entityModel = assembler.toModel(employee);
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
        //return employee;
    }

    @DeleteMapping("/employees/{employeeId}")
    ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        Boolean exists = repository.existsById(employeeId);
        if (!exists) {
            throw new EmployeeNotFoundException(employeeId);
        }
        repository.deleteById(employeeId);
        return ResponseEntity.noContent().build();
    }
}
