package com.example.employee.books.service;



import com.example.employee.books.exception.EmployeeAlreadyAddedException;
import com.example.employee.books.exception.EmployeeNotFoundException;
import com.example.employee.books.exception.EmployeeStorageIsFullException;
import com.example.employee.books.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private static final int LIMIT = 10;
    private final Map<String, Employee> employees = new HashMap<>();
    private final ValidatorServise validatorServise;

    public EmployeeService(ValidatorServise validatorServise) {
        this.validatorServise = validatorServise;
    }

    private String getKey(String firstName, String lastName) {
        return firstName + "|" + lastName;
    }

    public Employee add(String firstName,
                        String lastName,
                        int department,
                        double salary) {
        Employee employee = new Employee(
                validatorServise.validateFirstName(firstName),
                validatorServise.validateLastName(lastName),
                department,
                salary);
        String key = getKey(firstName, lastName);
        if (employees.containsKey(key)) {
            throw new EmployeeAlreadyAddedException();
        }
        if (employees.size() < LIMIT) {
            employees.put(key, employee);
            return employee;
        }
        throw new EmployeeStorageIsFullException();
    }

    public Employee remove(String firstName,
                           String lastName) {
        String key = getKey(firstName, lastName);
        if (!employees.containsKey(key)) {
            throw new EmployeeNotFoundException();
        }
        return employees.remove(key);
    }

    public Employee find(String firstName,
                         String lastName) {
        String key = getKey(firstName, lastName);
        if (!employees.containsKey(key)) {
            throw new EmployeeNotFoundException();
        }
        return employees.get(key);
    }


    public List<Employee> getAll() {
        return new ArrayList<>(employees.values());
    }
}
