package com.application.employeemanagement.service;

import com.application.employeemanagement.dto.EmployeeDTO;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    Page<EmployeeDTO> getAllEmployees(int page, int size);

    EmployeeDTO getEmployeeById(Long id);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    Page<EmployeeDTO> searchEmployees(String query, int page, int size);
}
