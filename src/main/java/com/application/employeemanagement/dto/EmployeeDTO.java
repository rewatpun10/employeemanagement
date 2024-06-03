package com.application.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private LocalDateTime created;
    private String firstName;
    private String lastName;
    private String email;
    private DepartmentDTO department;
}
