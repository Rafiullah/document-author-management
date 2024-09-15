package com.momand.docauthor.document.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.repository.AuthorRepository;
import com.momand.docauthor.author.service.AuthorProducerKafka;
import com.momand.docauthor.author.service.AuthorService;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentProducerKafka documentProducerKafka;
    @InjectMocks
    private DocumentService documentService;


    @Test
    public void testAddNewDocument(){
        DocumentEntity expectedDocument = new DocumentEntity();
        expectedDocument.setTitle("Rest APIs");
        expectedDocument.setBody("Develop rest apis using spring boot");

        when(documentRepository.save(expectedDocument)).thenReturn(expectedDocument);

        DocumentEntity actualDocument = documentService.addNewDocument(expectedDocument);

        verify(documentRepository).save(expectedDocument);
        verify(documentProducerKafka).sendDocumentEvent("Document with id "+ actualDocument.getId() + "added");


        assertEquals(expectedDocument, actualDocument);
    }
}
