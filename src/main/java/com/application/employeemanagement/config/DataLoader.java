package com.application.employeemanagement.config;

import jakarta.transaction.Transactional;
import com.application.employeemanagement.model.Department;
import com.application.employeemanagement.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.application.employeemanagement.repository.DepartmentRepository;
import com.application.employeemanagement.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final List<String> FIRST_NAMES = Arrays.asList(
            "John", "Jane", "Michael", "Michelle", "Chris", "Christina",
            "James", "Jessica", "David", "Emily", "Daniel", "Emma",
            "Matthew", "Olivia", "Joshua", "Sophia", "Andrew", "Isabella"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis",
            "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas",
            "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia"
    );

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Starting DataLoader...");

        Department hrDepartment = new Department();
        hrDepartment.setName("HR");

        Department itDepartment = new Department();
        itDepartment.setName("IT");

        Department networkingDepartment = new Department();
        networkingDepartment.setName("NETWORK");

        Department marketingDepartment = new Department();
        marketingDepartment.setName("MARKET");

        departmentRepository.saveAll(Arrays.asList(hrDepartment, itDepartment, networkingDepartment, marketingDepartment));
        List<Department> departments = Arrays.asList(hrDepartment, itDepartment, networkingDepartment, marketingDepartment);

        Employee emp1 = new Employee();
        emp1.setFirstName("Luffy");
        emp1.setLastName("King");
        emp1.setEmail("luffy.king@gmail.com");
        emp1.setCreated(LocalDateTime.now());
        emp1.setDepartment(hrDepartment);

        Employee emp2 = new Employee();
        emp2.setFirstName("Zoro");
        emp2.setLastName("Master");
        emp2.setEmail("zoro.master@gmail.com");
        emp2.setCreated(LocalDateTime.now());
        emp2.setDepartment(itDepartment);

        employeeRepository.saveAll(Arrays.asList(emp1, emp2));

        // Generate random employees for pagination and search
        Random random = new Random();
        for (int i = 1; i <= 35; i++) {
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@example.com";

            Employee emp = new Employee();
            emp.setFirstName(firstName);
            emp.setLastName(lastName);
            emp.setEmail(email);
            emp.setCreated(LocalDateTime.now());
            emp.setDepartment(departments.get(random.nextInt(departments.size())));

            employeeRepository.save(emp);
        }
        logger.info("DataLoader finished.");
    }
}
