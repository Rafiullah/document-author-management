package com.momand.docauthor.document.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentDTOBasic {
    //private Long id;
    @NotNull(message = "Title is required for a Document.")
    private String title;
    @NotNull(message = "Body is required for a Document.")
    private String body;

}
