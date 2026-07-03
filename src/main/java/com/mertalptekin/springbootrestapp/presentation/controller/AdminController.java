package com.mertalptekin.springbootrestapp.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {


    // SCOPE_admin:read
    // ROLE_ADMIN
    // ADMIN rol, ROLE_ADMIN -> hasRole('ADMIN') alabiliriz. hasAuthority('ROLE_ADMIN') de alabiliriz.
    //

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ROLE_MANAGER')")
    public String getAdmins() {
        return "Admin and user data accessed.";
    }


}
