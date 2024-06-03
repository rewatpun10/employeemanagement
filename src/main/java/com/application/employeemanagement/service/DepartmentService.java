package com.application.employeemanagement.service;

import com.application.employeemanagement.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();

    DepartmentDTO getDepartmentById(Long id);

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);

    void deleteDepartment(Long id);
}
