package com.momand.docauthor.author.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.repository.AuthorRepository;
import com.momand.docauthor.document.repository.DocumentRepository;
import com.momand.docauthor.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthorService {

    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository;
    private final DocumentRepository documentRepository;
    private final AuthorProducerKafka authorProducerKafka;

    public AuthorService(AuthorRepository authorRepository, DocumentRepository documentRepository, AuthorProducerKafka authorProducerKafka) {
        this.authorRepository = authorRepository;
        this.documentRepository = documentRepository;
        this.authorProducerKafka = authorProducerKafka;
    }

    public Iterable<AuthorEntity> findAllAuthors(){
        return authorRepository.findAll();
    }

    public AuthorEntity findAuthorById(Long id){
        return findOrThrow(id);
    }

    @Transactional
    public void removeAuthorById(Long id){
        authorRepository.deleteById(id);
        authorProducerKafka.sendAuthorEvent("Author with id "+ id + " removed");
    }

    //create new author
    @Transactional
    public AuthorEntity addNewAuthorEntity(AuthorEntity authorEntity){
        var addedAuthorEntity =  authorRepository.save(authorEntity);
        authorProducerKafka.sendAuthorEvent("Author with id" + addedAuthorEntity.getId() + " added");
        return addedAuthorEntity;
    }

    @Transactional
    public void updateAuthorEntity(Long id, AuthorEntity authorEntity){
        findOrThrow(id);
        authorRepository.save(authorEntity);
        authorProducerKafka.sendAuthorEvent("Author with id" + id + " updated");
    }

    private AuthorEntity findOrThrow(final Long id){
        return authorRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Author with id" + id + " not found."));
    }

}




