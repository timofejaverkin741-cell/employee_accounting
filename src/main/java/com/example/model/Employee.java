package com.example.model;

import jakarta.validation.constraints.Digits;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ФИО не должно быть пустым")
    @Size(min = 2, max = 100,
            message = "ФИО должно содержать от 2 до 100 символов")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Введите корректный адрес электронной почты")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Телефон не должен быть пустым")
    @Pattern(
            regexp = "^\\+?[0-9()\\-\\s]{10,20}$",
            message = "Введите корректный номер телефона"
    )
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Укажите должность")
    @Size(max = 100, message = "Должность не должна превышать 100 символов")
    @Column(nullable = false, length = 100)
    private String position;

    @NotNull(message = "Укажите зарплату")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Зарплата должна быть больше нуля")
    @Digits(integer = 10, fraction = 2,
            message = "Зарплата не должна превышать 10 цифр до запятой и 2 после")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false)
    private boolean active = true;

    public Employee() {
    }

    public Employee(String fullName, String email, String phone,
                    String position, BigDecimal salary) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
