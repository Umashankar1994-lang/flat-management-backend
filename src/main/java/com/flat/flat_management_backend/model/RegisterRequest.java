package com.flat.flat_management_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String flatNo;
    private String name;
    private String password;
    private String role; // ADMIN or MEMBER
}
