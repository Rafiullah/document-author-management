package com.momand.docauthor.author.dto;

import jakarta.validation.constraints.NotNull;

public class AuthorDTOBasicNoRef {

    @NotNull(message = "First Name is required.")
    private String firstName;

    @NotNull(message = "Last Name is required.")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
