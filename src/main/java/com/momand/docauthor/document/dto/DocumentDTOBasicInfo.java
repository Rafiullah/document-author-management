package com.momand.docauthor.document.dto;

import com.momand.docauthor.author.dto.AuthorDTOBasicNoRef;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class DocumentDTOBasicInfo {
    //private Long id;
    @NotNull(message = "Title is required for a Document.")
    private String title;
    private Set<AuthorDTOBasicNoRef> authors;
    private Set<DocumentDTOBasicInfo> references;

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Set<AuthorDTOBasicNoRef> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTOBasicNoRef> authors) {
        this.authors = authors;
    }

    public Set<DocumentDTOBasicInfo> getReferences() {
        return references;
    }

    public void setReferences(Set<DocumentDTOBasicInfo> references) {
        this.references = references;
    }
}
