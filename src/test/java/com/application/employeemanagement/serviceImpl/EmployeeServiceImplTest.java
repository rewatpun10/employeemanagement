package com.application.employeemanagement.serviceImpl;

import com.application.employeemanagement.dto.DepartmentDTO;
import com.application.employeemanagement.dto.EmployeeDTO;
import com.application.employeemanagement.exception.DuplicateEmailException;
import com.application.employeemanagement.mapper.EmployeeMapper;
import com.application.employeemanagement.model.Department;
import com.application.employeemanagement.model.Employee;
import com.application.employeemanagement.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.application.employeemanagement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    private Employee firstEmployee;
    private Employee secondEmployee;
    private EmployeeDTO firstEmployeeDTO;
    private EmployeeDTO secondEmployeeDTO;
    private Department hrDepartment;
    private DepartmentDTO hrDepartmentDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hrDepartment = new Department();
        hrDepartment.setId(1L);
        hrDepartment.setName("HR");

        hrDepartmentDTO = new DepartmentDTO();
        hrDepartmentDTO.setId(1L);
        hrDepartmentDTO.setName("HR");

        firstEmployee = new Employee();
        firstEmployee.setId(1L);
        firstEmployee.setFirstName("Luffy");
        firstEmployee.setLastName("King");
        firstEmployee.setEmail("luffy.king@gmail.com");
        firstEmployee.setCreated(LocalDateTime.now());
        firstEmployee.setDepartment(hrDepartment);

        secondEmployee = new Employee();
        secondEmployee.setId(2L);
        secondEmployee.setFirstName("Zoro");
        secondEmployee.setLastName("Master");
        secondEmployee.setEmail("zoro.master@gmail.com");
        secondEmployee.setCreated(LocalDateTime.now());
        secondEmployee.setDepartment(hrDepartment);

        firstEmployeeDTO = new EmployeeDTO();
        firstEmployeeDTO.setId(firstEmployee.getId());
        firstEmployeeDTO.setFirstName(firstEmployee.getFirstName());
        firstEmployeeDTO.setLastName(firstEmployee.getLastName());
        firstEmployeeDTO.setEmail(firstEmployee.getEmail());
        firstEmployeeDTO.setDepartment(hrDepartmentDTO);

        secondEmployeeDTO = new EmployeeDTO();
        secondEmployeeDTO.setId(secondEmployee.getId());
        secondEmployeeDTO.setFirstName(secondEmployee.getFirstName());
        secondEmployeeDTO.setLastName(secondEmployee.getLastName());
        secondEmployeeDTO.setEmail(secondEmployee.getEmail());
        secondEmployeeDTO.setDepartment(hrDepartmentDTO);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(firstEmployee, secondEmployee);
        Page<Employee> employeePage = new PageImpl<>(employees);
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toDTO(firstEmployee)).thenReturn(firstEmployeeDTO);
        when(employeeMapper.toDTO(secondEmployee)).thenReturn(secondEmployeeDTO);

        Page<EmployeeDTO> result = employeeServiceImpl.getAllEmployees(0, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("Luffy", result.getContent().get(0).getFirstName());
        assertEquals("HR", result.getContent().get(0).getDepartment().getName());
        assertEquals("Zoro", result.getContent().get(1).getFirstName());

        // Verify the method was called
        verify(employeeMapper, times(1)).toDTO(firstEmployee);
        verify(employeeMapper, times(1)).toDTO(secondEmployee);
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(firstEmployee));
        when(employeeMapper.toDTO(firstEmployee)).thenReturn(firstEmployeeDTO);

        EmployeeDTO employeeDTO = employeeServiceImpl.getEmployeeById(1L);

        assertNotNull(employeeDTO);
        assertEquals("Luffy", employeeDTO.getFirstName());
    }

    @Test
    void testCreateEmployeeDuplicateEmail() {
        when(employeeRepository.existsByEmail(firstEmployeeDTO.getEmail())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
            employeeServiceImpl.createEmployee(firstEmployeeDTO);
        });

        assertEquals("Same email already exists", exception.getMessage());

        verify(employeeRepository, times(1)).existsByEmail(firstEmployeeDTO.getEmail());
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.existsByEmail(firstEmployeeDTO.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(firstEmployee);
        when(departmentRepository.findById(firstEmployeeDTO.getDepartment().getId())).thenReturn(Optional.of(hrDepartment));
        when(employeeMapper.toEntity(firstEmployeeDTO, hrDepartment)).thenReturn(firstEmployee);
        when(employeeMapper.toDTO(firstEmployee)).thenReturn(firstEmployeeDTO);

        EmployeeDTO employeeSaved = employeeServiceImpl.createEmployee(firstEmployeeDTO);

        assertNotNull(employeeSaved);
        assertEquals("Luffy", employeeSaved.getFirstName());
        assertEquals("luffy.king@gmail.com", employeeSaved.getEmail());
    }

    @Test
    void testUpdateEmployee() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("luffy1");
        updatedEmployee.setLastName("pirateKing");
        updatedEmployee.setEmail("luffy1.pirateKing@gmail.com");
        updatedEmployee.setDepartment(hrDepartment);

        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(updatedEmployee.getId());
        updatedEmployeeDTO.setFirstName(updatedEmployee.getFirstName());
        updatedEmployeeDTO.setLastName(updatedEmployee.getLastName());
        updatedEmployeeDTO.setEmail(updatedEmployee.getEmail());
        updatedEmployeeDTO.setDepartment(hrDepartmentDTO);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(firstEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        when(departmentRepository.findById(updatedEmployeeDTO.getDepartment().getId())).thenReturn(Optional.of(hrDepartment));
        when(employeeMapper.toDTO(updatedEmployee)).thenReturn(updatedEmployeeDTO);

        EmployeeDTO result = employeeServiceImpl.updateEmployee(1L, updatedEmployeeDTO);
        assertEquals("luffy1", result.getFirstName());
        assertEquals("pirateKing", result.getLastName());
        assertEquals("luffy1.pirateKing@gmail.com", result.getEmail());
    }

    @Test
    void testDeleteEmployeeAndSuccessfulDeletion() {
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(employeeId);
        employeeServiceImpl.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void testDeleteEmployeeAndEmployeeNotFound() {
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeServiceImpl.deleteEmployee(employeeId);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, never()).deleteById(employeeId);
    }

    @Test
    void testSearchEmployees() {
        String searchParam = "Luff";
        List<Employee> filteredEmployees = Arrays.asList(firstEmployee);

        when(employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(eq(searchParam), eq(searchParam), any(Pageable.class)))
                .thenReturn(new PageImpl<>(filteredEmployees, PageRequest.of(0, 10), filteredEmployees.size()));

        when(employeeMapper.toDTO(firstEmployee)).thenReturn(firstEmployeeDTO);

        Page<EmployeeDTO> result = employeeServiceImpl.searchEmployees(searchParam, 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Luffy", result.getContent().get(0).getFirstName());
    }
}

