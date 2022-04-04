package com.example.batibol.employeeservice.controller;

import com.example.batibol.employeeservice.assembler.EmployeeModelAssembler;
import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.service.ServiceEmployee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerAPITest {

    private EmployeeController underTest;
    @Mock
    private ServiceEmployee serviceEmployee;
    @Mock
    private EmployeeModelAssembler assembler;

    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
       MockitoAnnotations.openMocks(this);
        underTest = new EmployeeController(serviceEmployee, assembler);
    }

    @Test
    void itShouldAddNewEmployee() throws Exception {
        //Given
        Employee employee = buildEmployee();
        EntityModel<Employee> employeeEntityModel = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
        when(serviceEmployee.addNewEmployee(employee)).thenReturn(employee);
        when(assembler.toModel(employee)).thenReturn(employeeEntityModel);
        //When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(employee))));
        //Then
        resultActions.andDo(print()).andExpect(status().isCreated());


    }

    public Employee buildEmployee() {
        return new Employee(
                1L,
                "Joe Doe",
                "HR"
        );
    }
    private String objectToJson(Object object) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }catch (JsonProcessingException e){
            fail("failed to convert object to json");
            return null;
        }
    }
}
