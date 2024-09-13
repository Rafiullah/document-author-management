package com.momand.docauthor.author.dto;

import com.momand.docauthor.document.dto.DocumentDTOBasicNoRef;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class AuthorDTOBasic {
    Long id;

    @NotNull(message = "First Name is required.")
    private String firstName;

    @NotNull(message = "Last Name is required.")
    private String lastName;

    private Set<DocumentDTOBasicNoRef> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<DocumentDTOBasicNoRef> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentDTOBasicNoRef> documents) {
        this.documents = documents;
    }
}
