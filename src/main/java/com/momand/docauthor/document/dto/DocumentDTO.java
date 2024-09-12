package com.momand.docauthor.document.dto;

import com.momand.docauthor.author.dto.AuthorDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class DocumentDTO{
    private Long id;
    @NotNull(message = "Title is required for a Document.")
    private String title;
    @NotNull(message = "Body is required for a Document.")
    private String body;

    private Set<AuthorDTO> authors;
    //private Set<Long> authors;

    private Set<DocumentDTO> references;
    //private Set<Long> references;
}
