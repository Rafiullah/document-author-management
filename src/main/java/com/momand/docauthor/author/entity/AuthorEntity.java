package com.momand.docauthor.author.entity;

import com.momand.docauthor.document.entity.DocumentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author_entity")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "First Name is required.")
    private String firstName;

    @NotNull(message = "Last Name is required")
    private String lastName;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    //@JsonIgnoreProperties("authors")
    //@JsonBackReference
    private Set<DocumentEntity> documents = new HashSet<>();


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

    public Set<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentEntity> documents) {
        this.documents = documents;
    }
}
