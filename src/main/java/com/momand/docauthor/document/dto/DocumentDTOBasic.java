package com.momand.docauthor.document.dto;

import com.momand.docauthor.author.dto.AuthorDTOBasicNoRef;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class DocumentDTOBasic {
    //private Long id;
    @NotNull(message = "Title is required for a Document.")
    private String title;
    @NotNull(message = "Body is required for a Document.")
    private String body;

    private Set<AuthorDTOBasicNoRef> authors;

    private Set<DocumentDTOBasic> references;


    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }*/

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public Set<AuthorDTOBasicNoRef> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTOBasicNoRef> authors) {
        this.authors = authors;
    }

    public Set<DocumentDTOBasic> getReferences() {
        return references;
    }

    public void setReferences(Set<DocumentDTOBasic> references) {
        this.references = references;
    }
}
