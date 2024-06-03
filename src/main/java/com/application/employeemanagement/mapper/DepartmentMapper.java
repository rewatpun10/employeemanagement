package com.application.employeemanagement.mapper;

import com.application.employeemanagement.dto.DepartmentDTO;
import com.application.employeemanagement.model.Department;

public class DepartmentMapper {
    public static DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    public static Department toEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        return department;
    }
}
