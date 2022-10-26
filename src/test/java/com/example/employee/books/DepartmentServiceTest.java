package com.example.employee.books;


import com.example.employee.books.exception.EmployeeNotFoundException;
import com.example.employee.books.model.Employee;
import com.example.employee.books.service.DepartmentService;
import com.example.employee.books.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    public void beforeEach() {
        when(employeeService.getAll()).thenReturn(
                Arrays.asList(
                        new Employee("Ivan","Petrov",2,20000),
                        new Employee("Oleg","Neiman",2,10000),
                        new Employee("Jein","Ostrov",2,30000),
                        new Employee("Alex","Vilkin",3,9000),
                        new Employee("Nik","Jyder",3,10085)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findEmployeeWithMaxSalaryFromDepartmentTestParams")
    public void findEmployeeWithMaxSalaryFromDepartmentTest(int department,
                                          Employee expected) {
        assertThat(departmentService.findEmployeeWithMaxSalaryFromDepartment(department)).isEqualTo(expected);
    }
    @Test
    public void findEmployeeWithMaxSalaryFromDepartmentNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->departmentService.findEmployeeWithMaxSalaryFromDepartment(1));
    }

    @ParameterizedTest
    @MethodSource("findEmployeesFromDepartmentTestParams")
    public void findEmployeesFromDepartmentTest(int department,
                                                            Collection<Employee> expected) {
        assertThat(departmentService.findEmployeesFromDepartment(department)).containsExactlyElementsOf(expected);
    }
    @Test
    public void findEmployeesTest() {
        assertThat(departmentService.findEmployees()).containsExactlyInAnyOrderEntriesOf(
                Map.of(
                        2,List.of(
                                new Employee("Ivan","Petrov",2,20000),
                                new Employee("Oleg","Neiman",2,10000),
                                new Employee("Jein","Ostrov",2,30000)
                        ),
                        3,List.of(
                                new Employee("Alex","Vilkin",3,9000),
                                new Employee("Nik","Jyder",3,10085)
                        )
                )
        );
    }


    @ParameterizedTest
    @MethodSource("findEmployeeWithMinSalaryFromDepartmentTestParams")
    public void findEmployeeWithMinSalaryFromDepartmentTest(int department,
                                                            Employee expected) {
        assertThat(departmentService.findEmployeeWithMinSalaryFromDepartment(department)).isEqualTo(expected);
    }
    @Test
    public void findEmployeeWithMinSalaryFromDepartmentNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->departmentService.findEmployeeWithMinSalaryFromDepartment(1));
    }

    public static Stream<Arguments> findEmployeeWithMaxSalaryFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(2,new Employee("Jein","Ostrov",2,30000)),
                Arguments.of(3,new Employee("Nik","Jyder",3,10085))
        );
    }

    public static Stream<Arguments> findEmployeeWithMinSalaryFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(2,new Employee("Oleg","Neiman",2,10000)),
                Arguments.of(3,new Employee("Alex","Vilkin",3,9000))
        );
    }
    public static Stream<Arguments> findEmployeesFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(1,
                        Collections.emptyList()
                ),
                Arguments.of(2,
                        List.of(
                        new Employee("Ivan","Petrov",2,20000),
                        new Employee("Oleg","Neiman",2,10000),
                        new Employee("Jein","Ostrov",2,30000)
                        )
                ),
                Arguments.of(3,
                        List.of(
                        new Employee("Alex","Vilkin",3,9000),
                        new Employee("Nik","Jyder",3,10085)
                        )
                )
        );
    }













}
