package com.momand.docauthor.author.dto;

import com.momand.docauthor.document.dto.DocumentDTOBasicNoRef;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class AuthorDTODocsOnly {

    private Set<DocumentDTOBasicNoRef> documents;

    public Set<DocumentDTOBasicNoRef> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentDTOBasicNoRef> documents) {
        this.documents = documents;
    }
}
