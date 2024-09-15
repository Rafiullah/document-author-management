package com.momand.docauthor.document.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.service.AuthorService;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.repository.DocumentRepository;
import com.momand.docauthor.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorService authorService;
    private final DocumentProducerKafka documentProducerKafka;

    public DocumentService(DocumentRepository documentRepository, AuthorService authorService,
                           DocumentProducerKafka documentProducerKafka) {
        this.documentRepository = documentRepository;
        this.authorService = authorService;
        this.documentProducerKafka = documentProducerKafka;
    }

    public Iterable<DocumentEntity> findAllDocuments(){
        return documentRepository.findAll();
    }

    public DocumentEntity findDocumentById(Long id){
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);
        documentProducerKafka.sendDocumentEvent("Read document with id "+ id);
        return documentEntity.orElse(null);
    }

    public void removeDocumentById(Long id){
        documentRepository.deleteById(id);
    }


    //create new Document
    @Transactional
    public DocumentEntity addNewDocument(DocumentEntity documentEntity){
        return documentRepository.save(documentEntity);
    }

    //create new document and add existing author for this document
    public DocumentEntity addNewDocumentForExistingAuthor(DocumentEntity documentEntity, Long authorId){
        AuthorEntity existingAuthor = authorService.findAuthorById(authorId);
        Set<AuthorEntity> authors = new HashSet<>();
        authors.add(existingAuthor);
        documentEntity.setAuthors(authors);
        return documentRepository.save(documentEntity);
    }

    //add existing document to existing author
    public DocumentEntity addExistingDocumentToExistingAuthor(Long documentId, Long authorId){

        DocumentEntity existingDocument = findDocumentById(documentId);
        AuthorEntity existingAuthor = authorService.findAuthorById(authorId);

        existingDocument.getAuthors().add(existingAuthor);

        return documentRepository.save(existingDocument);
    }

    //add a reference to document
    public DocumentEntity addReferenceToDocument(Long documentId, Long referenceId){

        DocumentEntity document = findDocumentById(documentId);
        DocumentEntity reference = findDocumentById(referenceId);

        document.getReferences().add(reference);
        return documentRepository.save(reference);
    }

    public void updateDocumentEntity(Long id, DocumentEntity documentEntity){
        findOrThrow(id);
        documentRepository.save(documentEntity);
    }
    private DocumentEntity findOrThrow(final Long id){
        return documentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Document not found with " + id + " not found.")
        );
    }

    public DocumentEntity addNewDocumentBasic(DocumentEntity documentEntity){
        return documentRepository.save(documentEntity);
    }


}
