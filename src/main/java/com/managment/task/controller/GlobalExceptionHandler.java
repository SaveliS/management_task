package com.managment.task.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.managment.task.exception.LoginAlreadyExistsException;
import com.managment.task.exception.RolesNotSelectedException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model){
        model.addAttribute("errorMessage", "Сущность не найдена: " + ex.getMessage());
        return "error/customErrorPage";
    }

    @ExceptionHandler(RolesNotSelectedException.class)
    public String handleRolesNotSelectedException(RolesNotSelectedException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/employee/new";
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public String handleLoginAlreadyExists(LoginAlreadyExistsException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/employee/new";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model){
        model.addAttribute("errorMessage", "Произошла ошибка. Попробуйте позже. Ошибка: " + ex.getMessage());
        return "error/customErrorPage";
    }
}
