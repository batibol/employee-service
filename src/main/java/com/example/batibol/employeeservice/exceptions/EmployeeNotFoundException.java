package com.example.batibol.employeeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ResponseBody

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(Long id) {
        super("employee with id : " + id + " was not found");
    }
}
