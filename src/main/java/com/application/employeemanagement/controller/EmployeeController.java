package com.application.employeemanagement.controller;

import com.application.employeemanagement.dto.EmployeeDTO;
import com.application.employeemanagement.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(@RequestParam int page, @RequestParam int size) {
        Page<EmployeeDTO> result = employeeService.getAllEmployees(page, size);
        log.info("Returning {} employees", result.getContent().size());
        return ResponseEntity.ok(result);    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
            log.info("Returning employee with id: {}", id);
            return ResponseEntity.ok(employeeDTO);
        } catch (RuntimeException e) {
            log.error("Employee with id: {} not found", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Validated @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        log.info("Created new employee with id: {}", createdEmployee.getId());
        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Validated @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
            log.info("Updated employee with id: {}", id);
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            log.error("Failed to update employee with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            log.info("Deleted employee with id: {}", id);
            return ResponseEntity.ok().<Void>build();
        } catch (RuntimeException e) {
            log.error("Failed to delete employee with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(@RequestParam String query,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        Page<EmployeeDTO> employees = employeeService.searchEmployees(query, page, size);
        log.info("Returning {} employees for search query: '{}'", employees.getContent().size(), query);
        return ResponseEntity.ok(employees);
    }
}
