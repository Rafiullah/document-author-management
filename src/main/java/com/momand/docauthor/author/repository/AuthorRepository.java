package com.momand.docauthor.author.repository;

import com.momand.docauthor.author.entity.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {

}
