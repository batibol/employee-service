package com.example.batibol.employeeservice.repository;

import com.example.batibol.employeeservice.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository underTest;

    @Test
    void itShouldSaveEmployee() {
        //Given
        Employee employee = new Employee(1L,"Peter Batibol", "IT");
        //When
        Employee newEmployee = underTest.save(employee);
        //Then
       assertThat(newEmployee)
               .isEqualTo(employee);


    }

    @Test
    void itShouldNotSaveEmployeeWhenNameIsNull() {
        //Given
        Employee employee = new Employee(1L,null, "IT");
        //When
        //Then
       assertThatThrownBy(() -> underTest.save(employee))
               .hasMessageContaining("not-null property references a null or transient value : com.example.batibol.employeeservice.model.Employee.name")
               .isInstanceOf(DataIntegrityViolationException.class);

    }
}