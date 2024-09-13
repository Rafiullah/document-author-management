package com.momand.docauthor.document.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.service.AuthorService;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.repository.DocumentRepository;
import com.momand.docauthor.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorService authorService;

    public DocumentService(DocumentRepository documentRepository, AuthorService authorService) {
        this.documentRepository = documentRepository;
        this.authorService = authorService;
    }

    public Iterable<DocumentEntity> findAllDocuments(){
        return documentRepository.findAll();
    }

    public DocumentEntity findDocumentById(Long id){
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);
        System.out.println("The authors list is: " + documentEntity.get().getAuthors());
        return documentEntity.orElse(null);
    }

    public void removeDocumentById(Long id){
        documentRepository.deleteById(id);
    }


    //create new Document
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

        System.out.println("the author is: " + existingAuthor);
        System.out.println("the document is: " + existingDocument);

        Set<AuthorEntity> authors = new HashSet<>();
        authors.add(existingAuthor);

        existingDocument.setAuthors(authors);

        return documentRepository.save(existingDocument);

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
