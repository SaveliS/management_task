package com.managment.task.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model){
        model.addAttribute("errorMessage", "Произошла ошибка. Попробуйте позже. Ошибка: " + ex.getMessage());
        return "error/customErrorPage";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model){
        model.addAttribute("errorMessage", "Сущность не найдена: " + ex.getMessage());
        return "error/customErrorPage";
    }
}
