package com.momand.docauthor.author.dto;

import com.momand.docauthor.document.dto.DocumentDTO;
import jakarta.validation.constraints.NotNull;
import java.util.Set;


public class AuthorDTO {
    Long id;
    @NotNull(message = "First Name is required.")
    private String firstName;
    @NotNull(message = "Last Name is required.")
    private String lastName;
    private Set<DocumentDTO> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "First Name is required.") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull(message = "First Name is required.") String firstName) {
        this.firstName = firstName;
    }

    public @NotNull(message = "Last Name is required.") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull(message = "Last Name is required.") String lastName) {
        this.lastName = lastName;
    }

    public Set<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentDTO> documents) {
        this.documents = documents;
    }
}
