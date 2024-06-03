package com.application.employeemanagement.mapper;

import com.application.employeemanagement.dto.EmployeeDTO;
import com.application.employeemanagement.model.Department;
import com.application.employeemanagement.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setCreated(employee.getCreated());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(DepartmentMapper.toDTO(employee.getDepartment()));
        return dto;
    }

    public Employee toEntity(EmployeeDTO dto, Department department) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setCreated(dto.getCreated());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(department);
        return employee;
    }
}
