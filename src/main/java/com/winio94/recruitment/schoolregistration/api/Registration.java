package com.winio94.recruitment.schoolregistration.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Registration implements WithUuid {

    private final String studentUuid;

    public Registration(@JsonProperty("studentUuid") String studentUuid) {
        this.studentUuid = studentUuid;
    }

    @Override
    @JsonProperty("studentUuid")
    public String getUuid() {
        return studentUuid;
    }
}
