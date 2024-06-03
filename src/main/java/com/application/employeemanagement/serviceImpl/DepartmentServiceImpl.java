package com.application.employeemanagement.serviceImpl;

import com.application.employeemanagement.dto.DepartmentDTO;
import com.application.employeemanagement.mapper.DepartmentMapper;
import com.application.employeemanagement.model.Department;
import com.application.employeemanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.employeemanagement.repository.DepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return DepartmentMapper.toDTO(department);
    }

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.toEntity(departmentDTO);
        department = departmentRepository.save(department);
        return DepartmentMapper.toDTO(department);
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        existingDepartment.setName(departmentDTO.getName());
        existingDepartment = departmentRepository.save(existingDepartment);
        return DepartmentMapper.toDTO(existingDepartment);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
