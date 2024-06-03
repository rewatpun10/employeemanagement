package com.application.employeemanagement.serviceImpl;

import com.application.employeemanagement.dto.EmployeeDTO;
import com.application.employeemanagement.exception.DuplicateEmailException;
import com.application.employeemanagement.mapper.EmployeeMapper;
import com.application.employeemanagement.model.Department;
import com.application.employeemanagement.repository.DepartmentRepository;
import com.application.employeemanagement.service.EmployeeService;
import jakarta.transaction.Transactional;
import com.application.employeemanagement.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.application.employeemanagement.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    public Page<EmployeeDTO> getAllEmployees(int page, int size) {
        Page<EmployeeDTO> result = employeeRepository.findAll(PageRequest.of(page, size))
                .map(employeeMapper::toDTO);
        log.info("Fetched {} employees", result.getContent().size());
        return result;
    }

    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new RuntimeException("Employee not found");
                });
        EmployeeDTO employeeDTO = employeeMapper.toDTO(employee);
        log.info("Fetched employee: {}", employeeDTO);
        return employeeDTO;
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            log.error("Duplicate email found: {}", employeeDTO.getEmail());
            throw new DuplicateEmailException("Same email already exists");
        }

        Department department = departmentRepository.findById(employeeDTO.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Employee employee = employeeMapper.toEntity(employeeDTO, department);
        employee = employeeRepository.save(employee);
        EmployeeDTO savedEmployeeDTO = employeeMapper.toDTO(employee);
        log.info("Created new employee: {}", savedEmployeeDTO);
        return savedEmployeeDTO;
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee cannot be found with id: {}", id);
                    return new RuntimeException("Employee not found");
                });

        Department department = departmentRepository.findById(employeeDTO.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        existingEmployee.setFirstName(employeeDTO.getFirstName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setDepartment(department);

        existingEmployee = employeeRepository.save(existingEmployee);
        EmployeeDTO updatedEmployeeDTO = employeeMapper.toDTO(existingEmployee);
        log.info("Updated employee: {}", updatedEmployeeDTO);
        return updatedEmployeeDTO;    }


    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            log.error("Employee cannot be found with id: {}", id);
            throw new RuntimeException("Employee not found");
        }
    }

    public Page<EmployeeDTO> searchEmployees(String query, int page, int size) {
        Page<Employee> employees = employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query, PageRequest.of(page, size));
        Page<EmployeeDTO> result = employees.map(employeeMapper::toDTO);
        log.info("Found {} employees for query: '{}'", result.getContent().size(), query);
        return result;    }
}
