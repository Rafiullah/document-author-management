package com.momand.docauthor.document.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.service.AuthorService;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.repository.DocumentRepository;
import com.momand.docauthor.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        return findOrThrow(id);
    }

    @Transactional
    public void removeDocumentById(Long id){
        documentRepository.deleteById(id);
    }

    //create new Document
    @Transactional
    public DocumentEntity addNewDocument(DocumentEntity documentEntity){
        var addedEntity = documentRepository.save(documentEntity);
        documentProducerKafka.sendDocumentEvent("Document with id "+ addedEntity.getId() + "added");
        return addedEntity;
    }

    //create new document and add existing author for this document
    @Transactional
    public DocumentEntity addNewDocumentForExistingAuthor(DocumentEntity documentEntity, Long authorId){
        AuthorEntity existingAuthor = authorService.findAuthorById(authorId);
        Set<AuthorEntity> authors = new HashSet<>();
        authors.add(existingAuthor);
        documentEntity.setAuthors(authors);
        return documentRepository.save(documentEntity);
    }

    //add existing document to existing author
    @Transactional
    public DocumentEntity addExistingDocumentToExistingAuthor(Long documentId, Long authorId){
        DocumentEntity existingDocument = findDocumentById(documentId);
        AuthorEntity existingAuthor = authorService.findAuthorById(authorId);
        existingDocument.getAuthors().add(existingAuthor);
        return documentRepository.save(existingDocument);
    }

    //add a reference to document
    @Transactional
    public DocumentEntity addReferenceToDocument(Long documentId, Long referenceId){
        DocumentEntity document = findDocumentById(documentId);
        DocumentEntity reference = findDocumentById(referenceId);
        document.getReferences().add(reference);
        return documentRepository.save(reference);
    }

    @Transactional
    public DocumentEntity updateDocumentEntity(Long id, DocumentEntity documentEntity){
        findOrThrow(id);
        var entity = documentRepository.save(documentEntity);
        documentProducerKafka.sendDocumentEvent("Update document "+ id);
        return entity;
    }

    private DocumentEntity findOrThrow(final Long id){
        return documentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Document with " + id + " not found.")
        );
    }

    @Transactional
    public DocumentEntity addNewDocumentBasic(DocumentEntity documentEntity){
        return documentRepository.save(documentEntity);
    }


}
