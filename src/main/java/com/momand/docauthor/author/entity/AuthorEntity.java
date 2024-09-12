package com.momand.docauthor.author.entity;

import com.momand.docauthor.document.entity.DocumentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author_entity")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull(message = "First Name is required.")
    private String firstName;
    @NotNull(message = "Last Name is required")
    private String lastName;
    //@ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "authors")
    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "authors")
    private Set<DocumentEntity> documents = new HashSet<>();
}
