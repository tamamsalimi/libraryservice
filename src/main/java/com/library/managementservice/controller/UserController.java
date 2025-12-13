package com.library.managementservice.controller;

import com.library.managementservice.api.dto.CreateUserRequest;
import com.library.managementservice.api.dto.CreateUserResponse;
import com.library.managementservice.service.UserService;
import com.library.managementservice.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse create(@RequestBody @Valid CreateUserRequest req) {
        if (!req.role().equals(Constants.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Only ADMIN users can be created via this endpoint");
        }
        return service.createUser(req);
    }
}
