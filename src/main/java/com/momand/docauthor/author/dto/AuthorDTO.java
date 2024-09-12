package com.momand.docauthor.author.dto;

import com.momand.docauthor.document.dto.DocumentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class AuthorDTO {
    Long id;
    @NotNull(message = "First Name is required.")
    private String firstName;
    @NotNull(message = "Last Name is required.")
    private String lastName;
    private Set<DocumentDTO> documents;
    //private Set<Long> documents;
}
