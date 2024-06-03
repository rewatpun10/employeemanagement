package com.application.employeemanagement.controller;

import com.application.employeemanagement.dto.DepartmentDTO;
import com.application.employeemanagement.dto.EmployeeDTO;
import com.application.employeemanagement.service.EmployeeService;
import com.application.employeemanagement.utils.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    private static final String BASE_URL = "/api/employees";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeDTO firstEmployeeDTO;
    private EmployeeDTO secondEmployeeDTO;


    @BeforeEach
    void setUp() {
        firstEmployeeDTO = new EmployeeDTO();
        firstEmployeeDTO.setId(1L);
        firstEmployeeDTO.setFirstName("Luffy");
        firstEmployeeDTO.setLastName("King");
        firstEmployeeDTO.setEmail("luffy.king@gmail.com");
        firstEmployeeDTO.setDepartment(new DepartmentDTO(1L, "HR"));

        secondEmployeeDTO = new EmployeeDTO();
        secondEmployeeDTO.setId(2L);
        secondEmployeeDTO.setFirstName("Zoro");
        secondEmployeeDTO.setLastName("Master");
        secondEmployeeDTO.setEmail("zoro.master@gmail.com");
        secondEmployeeDTO.setDepartment(new DepartmentDTO(1L, "HR"));
    }

    @Test
    void shouldReturnAllEmployees() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(firstEmployeeDTO, secondEmployeeDTO);
        Page<EmployeeDTO> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), employees.size());

        when(employeeService.getAllEmployees(0, 10)).thenReturn(employeePage);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(2)))
                .andExpect(jsonPath("$.content[0].firstName", is("Luffy")))
                .andExpect(jsonPath("$.content[1].firstName", is("Zoro")));
    }


    @Test
    void shouldReturnEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(firstEmployeeDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is(firstEmployeeDTO.getFirstName())));
    }

    @Test
    void shouldCreateNewEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(firstEmployeeDTO);

        EmployeeDTO newEmployee = new EmployeeDTO();
        newEmployee.setFirstName("Luffy");
        newEmployee.setLastName("King");
        newEmployee.setEmail("luffy.king@gmail.com");
        newEmployee.setDepartment(new DepartmentDTO(1L, "HR"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Luffy")))
                .andExpect(jsonPath("$.lastName", is("King")))
                .andExpect(jsonPath("$.email", is("luffy.king@gmail.com")));

    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        EmployeeDTO existingEmployeeDTO = new EmployeeDTO();
        existingEmployeeDTO.setId(1L);
        existingEmployeeDTO.setFirstName("Luffy");
        existingEmployeeDTO.setLastName("King");
        existingEmployeeDTO.setEmail("luffy.king@gmail.com");
        existingEmployeeDTO.setDepartment(new DepartmentDTO(1L, "HR"));

        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(1L);
        updatedEmployeeDTO.setFirstName("Luffy1");
        updatedEmployeeDTO.setLastName("PirateKing");
        updatedEmployeeDTO.setEmail("luffy1.pirateking@gmail.com");
        updatedEmployeeDTO.setDepartment(new DepartmentDTO(1L, "HR"));

        when(employeeService.getEmployeeById(1L)).thenReturn(existingEmployeeDTO);
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDTO.class))).thenReturn(updatedEmployeeDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Luffy")))
                .andExpect(jsonPath("$.lastName", is("King")))
                .andExpect(jsonPath("$.email", is("luffy.king@gmail.com")));

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(updatedEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Luffy1")))
                .andExpect(jsonPath("$.lastName", is("PirateKing")))
                .andExpect(jsonPath("$.email", is("luffy1.pirateking@gmail.com")));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void shouldSearchEmployees() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(firstEmployeeDTO);
        Page<EmployeeDTO> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), employees.size());

        when(employeeService.searchEmployees("Luff", 0, 10)).thenReturn(employeePage);

        mockMvc.perform(get(BASE_URL + "/search")
                        .param("query", "Luff")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("Luffy")));
    }
}
