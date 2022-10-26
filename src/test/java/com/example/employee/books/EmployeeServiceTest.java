package com.example.employee.books;

import com.example.employee.books.exception.*;
import com.example.employee.books.model.Employee;
import com.example.employee.books.service.EmployeeService;
import com.example.employee.books.service.ValidatorServise;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


public class EmployeeServiceTest {
    EmployeeService employeeService = new EmployeeService(new ValidatorServise());


    @AfterEach
    public void afterEach() {
        employeeService.getAll().forEach(employee -> employeeService.remove(employee.getFirstName(), employee.getLastName()));

    }

    @Test
    public void addPositiveTest() {
        addOneWithCheck();
    }

    private Employee addOneWithCheck(String firstName, String lastName) {
        Employee expected = new Employee(firstName, lastName, 1, 15000);
        int sizeBefore =(employeeService.getAll().size());
        employeeService.add(expected.getFirstName(), expected.getLastName(), expected.getDepartment(), expected.getSalary());
        assertThat(employeeService.getAll())
                .isNotEmpty()
                .hasSize(sizeBefore+1)
                .contains(expected);
        assertThat(employeeService.find(expected.getFirstName(), expected.getLastName())).isEqualTo(expected);
        return expected;
    }

    private Employee addOneWithCheck() {
        return addOneWithCheck("Firstname","Lastname");
    }

    @ParameterizedTest
    @MethodSource("addNegative1Param")
    public void addNegative1Test(String firstName,
                                 String lastName,
                                 Class<Throwable> expectedExceptionType) {
        assertThatExceptionOfType(expectedExceptionType)
                .isThrownBy(() -> employeeService.add(firstName, lastName, 1, 15000));
    }

    @Test
    public void addNegative2Test() {
        Employee employee = addOneWithCheck();
        assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(() -> employeeService.add(employee.getFirstName(), employee.getLastName(), employee.getDepartment(), employee.getSalary()));
    }

    @Test
    public void addNegative3Test() {
        for (int i = 0; i < 10; i++) {
            addOneWithCheck("Firstname" + (char) ('a' + i), "Lastname" + (char) ('a' + i));
        }
        assertThatExceptionOfType(EmployeeStorageIsFullException.class)
                .isThrownBy(() -> employeeService.add("Firstname", "Lastname", 1, 15000));
    }

    @Test
    public void findPositiveTest() {
        Employee employee1 = addOneWithCheck("Name","Surname");
        Employee employee2 = addOneWithCheck("Имя","Фамилия");
        assertThat(employeeService.find(employee1.getFirstName(),employee1.getLastName()))
                .isEqualTo(employee1);
        assertThat(employeeService.find(employee2.getFirstName(),employee2.getLastName()))
                .isEqualTo(employee2);
    }
    @Test
    public void findNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->employeeService.find("test","test"));
        addOneWithCheck("First","Lastname");
        addOneWithCheck("Имя","Фамилия");
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->employeeService.find("test","test"));
    }
    @Test
    public void removePositiveTest() {
        Employee employee1 = addOneWithCheck("Name","Surname");
        Employee employee2 = addOneWithCheck("Имя","Фамилия");
        employeeService.remove(employee1.getFirstName(),employee1.getLastName());
        assertThat(employeeService.getAll())
                .isNotEmpty()
                        .hasSize(1)
                                .containsExactly(employee2) ;
    employeeService.remove(employee2.getFirstName(),employee2.getLastName());
        assertThat(employeeService.getAll().isEmpty());
    }
    @Test
    public void removeNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->employeeService.remove("test","test"));
        addOneWithCheck("First","Lastname");
        addOneWithCheck("Имя","Фамилия");
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->employeeService.remove("test","test"));
    }



    public static Stream<Arguments> addNegative1Param() {
        return Stream.of(
                Arguments.of("Ivan1", "Ivanov", IncorrectFirstNameException.class),
                Arguments.of("Ivan%", "Ivanov", IncorrectFirstNameException.class),
                Arguments.of("Ivan", "Ivanov$", IncorrectLastNameException.class),
                Arguments.of("Ivan", "Ivanov1", IncorrectLastNameException.class),
                Arguments.of("Ivan", "Ivanov-Petrov1", IncorrectLastNameException.class),
                Arguments.of("Ivan", "Ivanov-@Petrov", IncorrectLastNameException.class)
        );
    }


}
