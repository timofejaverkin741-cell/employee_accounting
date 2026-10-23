package com.example.controller;

import com.example.model.Employee;
import com.example.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String active,
            Model model) {

        Boolean activeFilter = null;

        if ("true".equals(active)) {
            activeFilter = true;
        } else if ("false".equals(active)) {
            activeFilter = false;
        }

        model.addAttribute("employees",
                employeeService.search(name, activeFilter));
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("active", active == null ? "" : active);

        return "employees-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("editMode", false);
        return "employee-form";
    }

    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (employeeService.emailExists(employee.getEmail())) {
            result.rejectValue("email", "duplicate",
                    "Сотрудник с таким email уже существует");
        }

        if (result.hasErrors()) {
            model.addAttribute("editMode", false);
            return "employee-form";
        }

        employeeService.save(employee);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Сотрудник успешно добавлен");

        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getById(id));
        model.addAttribute("editMode", true);
        return "employee-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Employee existing = employeeService.getById(id);

        if (employeeService.emailExists(employee.getEmail())
                && !existing.getEmail().equalsIgnoreCase(
                employee.getEmail().trim())) {
            result.rejectValue("email", "duplicate",
                    "Этот email уже используется другим сотрудником");
        }

        if (result.hasErrors()) {
            employee.setId(id);
            model.addAttribute("editMode", true);
            return "employee-form";
        }

        employee.setId(id);
        employeeService.save(employee);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Данные сотрудника обновлены");

        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Сотрудник удалён");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Сотрудник не найден");
        }

        return "redirect:/employees";
    }
}
