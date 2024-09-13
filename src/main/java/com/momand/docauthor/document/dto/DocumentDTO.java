package com.momand.docauthor.document.dto;

import com.momand.docauthor.author.dto.AuthorDTO;
import jakarta.validation.constraints.NotNull;
import java.util.Set;


public class DocumentDTO{
    private Long id;
    @NotNull(message = "Title is required for a Document.")
    private String title;
    @NotNull(message = "Body is required for a Document.")
    private String body;

    private Set<AuthorDTO> authors;

    private Set<DocumentDTO> references;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    public Set<DocumentDTO> getReferences() {
        return references;
    }

    public void setReferences(Set<DocumentDTO> references) {
        this.references = references;
    }
}
