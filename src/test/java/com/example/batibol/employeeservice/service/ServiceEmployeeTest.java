package com.example.batibol.employeeservice.service;

import com.example.batibol.employeeservice.exceptions.EmployeeNotFoundException;
import com.example.batibol.employeeservice.model.Employee;
import com.example.batibol.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

class ServiceEmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private ServiceEmployee underTest;

    @Captor
    private ArgumentCaptor<Employee> argumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ServiceEmployee(employeeRepository);
    }

    @Test
    void itShouldSaveNewEmployee() {
        //Given
        Employee employee = new Employee(1L, "John Doe", "IT");
        //When
        underTest.addNewEmployee(employee);
        //Then
       then(employeeRepository).should().save(argumentCaptor.capture());
        Employee argumentCaptorValue = argumentCaptor.getValue();
        assertThat(argumentCaptorValue).isEqualTo(employee);

    }

    @Test
    void itShouldReturnAllEmployees() {
        //Given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1L, "James Carter", "IT"));
        employees.add(new Employee(2L, "John Doe", "HR"));
        // ... list of employees
        given(employeeRepository.findAll()).willReturn(employees);
        //When
        List<Employee> returnedEmployees = underTest.getAllEmployees();
        //Then
        assertThat(returnedEmployees).isEqualTo(employees);

    }

    @Test
    void itShouldItShouldReturnAnEmployeeGivenEmployeeId() {
        //Given
        long id = 1L;
        Employee employee = new Employee(id, "John Doe", "HR");
        given(employeeRepository.findById(id)).willReturn(Optional.of(employee));
        //When
        //Then
        assertThat(underTest.getEmployeeById(id))
                .isEqualTo(employee);

    }

    @Test
    void itShouldShouldReturnAnEmployeeGivenEmployeeId() {
        //Given
        long id = 1L;
        given(employeeRepository.findById(id)).willReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() ->underTest.getEmployeeById(id))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("employee with id : " + id + " was not found");

    }

    @Test
    void itShouldShouldDeleteEmployeeGivenEmployeeId() {
        //Given
        long id = 1L;
        Employee employee = new Employee(id, "John Doe", "HR");
        given(employeeRepository.existsById(id)).willReturn(true);
        doNothing().when(employeeRepository).deleteById(id);
        //When
        underTest.deleteEmployeeById(id);
        //Then


    }

    @Test
    void itShouldShouldThrownAnException() {
        //Given
        long id = 1L;
        given(employeeRepository.existsById(id)).willReturn(false);
        //When
        //Then
        assertThatThrownBy(() -> underTest.deleteEmployeeById(id))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("employee with id : " + id + " was not found");

    }
}