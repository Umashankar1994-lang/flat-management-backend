package com.flat.flat_management_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String flatNo;
    private String password;
}
