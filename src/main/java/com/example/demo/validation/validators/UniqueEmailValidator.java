package com.example.demo.validation.validators;

import com.example.demo.service.UserService;
import com.example.demo.validation.annotations.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // Face validatorul un Spring Bean pentru a permite injectarea dependințelor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    @Autowired // Injectează UserService
    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // Dacă email-ul este null sau gol, lăsăm altă validare (@NotNull, @NotBlank) să se ocupe de asta
        if (email == null || email.trim().isEmpty()) {
            return true;
        }
        // Verifică dacă există deja un utilizator cu acest email
        // Metoda getUserByUsername ar trebui să returneze null dacă nu găsește utilizatorul
        return userService.getUserByUsername(email) == null;
    }
}