package com.momand.docauthor.author.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.repository.AuthorRepository;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.repository.DocumentRepository;
import com.momand.docauthor.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final DocumentRepository documentRepository;

    public AuthorService(AuthorRepository authorRepository, DocumentRepository documentRepository) {
        this.authorRepository = authorRepository;
        this.documentRepository = documentRepository;
    }

    public Iterable<AuthorEntity> findAllAuthors(){
        return authorRepository.findAll();
    }

    @Transactional
    public AuthorEntity findAuthorById(Long id){
        return findOrThrow(id);
    }

    public void removeAuthorById(Long id){
        authorRepository.deleteById(id);
    }

    //create new author
    public AuthorEntity addNewAuthorEntity(AuthorEntity authorEntity){
        return authorRepository.save(authorEntity);
    }

    //create new author and add existing document for this author
    public AuthorEntity addNewAuthorForExistingDocument(AuthorEntity authorEntity, Long documentId){
        DocumentEntity existingDocument =

                documentRepository.findById(documentId)
                        .orElseThrow(()-> new NotFoundException("Document with id" + documentId + " not found."));
        Set<DocumentEntity> documents = new HashSet<>();
        documents.add(existingDocument);
        authorEntity.setDocuments(documents);
        return authorRepository.save(authorEntity);
    }

    //add existing author to existing document
    public AuthorEntity addExistingAuthorToExistingDocument(Long authorId, Long documentId){
        AuthorEntity existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(()-> new NotFoundException("Document with id" + documentId + " not found."));

        DocumentEntity existingDocument = documentRepository.findById(documentId)
                .orElseThrow(()-> new NotFoundException("Document with id" + documentId + " not found."));


        System.out.println("the author is: " + existingAuthor);
        System.out.println("the document is: " + existingDocument);

        Set<DocumentEntity> documents = new HashSet<>();
        documents.add(existingDocument);

        existingAuthor.setDocuments(documents);

        return authorRepository.save(existingAuthor);
    }

    public void updateAuthorEntity(Long id, AuthorEntity authorEntity){
        findOrThrow(id);
        authorRepository.save(authorEntity);
    }

    private AuthorEntity findOrThrow(final Long id){
        return authorRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Author with id" + id + " not found."));
    }

}




