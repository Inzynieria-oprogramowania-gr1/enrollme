package com.company.project.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "basicAuth")
public class AuthenticationController {


    // Dummy endpoint for @Ola xd
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/login")
    public void login() {
    }
}


