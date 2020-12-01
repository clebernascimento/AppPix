package com.itbam.pixceler.service.model;

import lombok.Data;

@Data
public class AuthResponse {
    private String access_token;
    private String name;
    private Company company;

    public int getId() {
        return company.getId();
    }

    @Data
    private class Company {
        private int id;
        private String name;
    }
}
