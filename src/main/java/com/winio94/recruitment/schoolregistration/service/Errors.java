package com.winio94.recruitment.schoolregistration.service;

import java.util.List;

public class Errors {

    private final List<String> errors;

    public Errors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
