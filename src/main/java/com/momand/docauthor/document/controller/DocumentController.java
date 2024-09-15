package com.momand.docauthor.document.controller;

import com.momand.docauthor.author.dto.AuthorDTOBasicNoRef;
import com.momand.docauthor.document.dto.DocumentDTO;
import com.momand.docauthor.document.dto.DocumentDTOBasic;
import com.momand.docauthor.document.dto.DocumentDTOBasicInfo;
import com.momand.docauthor.document.dto.DocumentDTOBasicNoRef;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequestMapping("/api/v1/documents")
@RestController
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;
    private final ModelMapper modelMapper;

    public DocumentController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Retrieves all documents along with their contents, authors and references")
    @GetMapping
    public List<DocumentDTOBasic> getDocuments() {
        var documents = documentService.findAllDocuments();
        List<DocumentDTOBasic> documentsDtoList = new ArrayList<>();
        for (var document : documents) {
            documentsDtoList.add(convertToDTOBasic(document));
        }
        return documentsDtoList;
    }

    @Operation(summary = "Retrieves a list of documents")
    @GetMapping("/list-docs")
    public List<DocumentDTOBasicNoRef> getDocumentsWithoutRefsOrAuthors() {
        var documents = documentService.findAllDocuments();
        List<DocumentDTOBasicNoRef> documentsDtoList = new ArrayList<>();
        for (var document : documents) {
            documentsDtoList.add(convertToDTOBasicNoRef(document));
        }
        return documentsDtoList;
    }

    @Operation(summary = "Retrieves references of a document")
    @GetMapping("/{docId}/references")
    public Set<DocumentDTO> getAllReferencesForDocument(@PathVariable Long docId){
        DocumentEntity documentEntity = documentService.findDocumentById(docId);
        DocumentDTO documentDTO = convertToDTO(documentEntity);
        return documentDTO.getReferences();
    }

    @Operation(summary = "Retrieves authors of a document")
    @GetMapping("/{docId}/authors")
    public Set<AuthorDTOBasicNoRef> getAuthorsForDocument(@PathVariable Long docId){
        DocumentEntity documentEntity = documentService.findDocumentById(docId);
        DocumentDTOBasic documentDTO = convertToDTOBasic(documentEntity);
        return documentDTO.getAuthors();
    }

    @Operation(summary = "Retrieves a specific document containing title, contents, author and references")
    @GetMapping("/{id}")
    public DocumentDTOBasic getDocumentById(@PathVariable("id") Long id){
        return convertToDTOBasic(documentService.findDocumentById(id));
    }

    @Operation(summary = "Retrieves a specific document's title and content")
    @GetMapping("/{id}/document-title-content")
    public DocumentDTOBasicNoRef getListOfDocument(@PathVariable("id") Long id){
        return convertToDTOBasicNoRef(documentService.findDocumentById(id));
    }

    @Operation(summary = "Retrieves a specific document's title, authors and references")
    @GetMapping("/{id}/short-info")
    public DocumentDTOBasicInfo getDocumentShortInfo(@PathVariable("id") Long id){
        return convertToDTOBasicInfo(documentService.findDocumentById(id));
    }

    @Operation(summary = "Creates/Adds a new document")
    @PostMapping
    public DocumentDTOBasic postNewDocument(@Valid @RequestBody DocumentDTOBasic documentDTOBasic){
        logger.info("Received DocumentDTO: {}", documentDTOBasic.toString());
        var entity = convertBasicDTOToEntity(documentDTOBasic);
        logger.info("Converted Document Entity: {}", entity.toString());
        var document = documentService.addNewDocument(entity);
        return convertToDTOBasic(document);
    }

    @Operation(summary = "Adds a document without authors or references")
    @PostMapping("/add-document-only")
    public DocumentDTOBasicNoRef postNewDocumentBasic(@Valid @RequestBody DocumentDTOBasicNoRef documentDTO){
        var entity = convertBasicNoRefDTOToEntity(documentDTO);
        var document = documentService.addNewDocument(entity);
        return convertToDTOBasicNoRef(document);
    }

    @Operation(summary = "Creates a new document and assigns an existing author to it")
    @PutMapping("/add-new-document-to-existing-author/{authorId}")
    public DocumentDTO postNewAuthorForExistingDocuments(@Valid @RequestBody DocumentDTO documentDTO, @PathVariable Long authorId){
        logger.info("Received DocumentDTO: {}", documentDTO.toString());
        var entity = convertToEntity(documentDTO);
        logger.info("Converted Document Entity: {}", entity);
        var document = documentService.addNewDocumentForExistingAuthor(entity, authorId);
        return convertToDTO(document);
    }

    @Operation(summary = "Assigns a document to an author, both author and documents are available already")
    @PutMapping("/assign-document-to-author/{docId}/{authId}")
    public DocumentDTOBasic postExistingAuthorForExistingDocuments(@PathVariable Long docId, @PathVariable Long authId){
        var document = documentService.addExistingDocumentToExistingAuthor(docId, authId);
        return convertToDTOBasic(document);
    }

    @Operation(summary = "Adds a reference to a document")
    @PostMapping("/add-reference/{documentId}/{referenceId}")
    public DocumentDTO postReferenceToDocument(@PathVariable Long documentId, @PathVariable Long referenceId){
        var document = documentService.addReferenceToDocument(documentId, referenceId);
        return convertToDTO(document);
    }

    @Operation(summary = "Updates a document")
    @PutMapping("/{id}")
    public void putDocument(@PathVariable("id") Long id,
                            @Valid @RequestBody DocumentDTOBasic documentDTOBasic
    ) {
        /*if (!id.equals(documentDTO.getId())) throw new
                ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "id does not match."
        );*/
        var documentEntity = convertBasicDTOToEntity(documentDTOBasic);
        documentService.updateDocumentEntity(id, documentEntity);
    }

    @Operation(summary = "Deletes a document")
    @DeleteMapping("/{id}")
    public void deleteDocumentById(@PathVariable("id") Long id) {
        documentService.removeDocumentById(id);
    }

    // Method to convert DocumentDTO to DocumentEntity
    public DocumentEntity convertToEntity(DocumentDTO documentDTO) {
        return modelMapper.map(documentDTO, DocumentEntity.class);
    }

    private DocumentDTO convertToDTO(DocumentEntity entity){
        return modelMapper.map(entity, DocumentDTO.class);
    }

    private DocumentDTOBasic convertToDTOBasic(DocumentEntity entity){
        return modelMapper.map(entity, DocumentDTOBasic.class);
    }

    private DocumentEntity convertBasicDTOToEntity(DocumentDTOBasic dto){
        return modelMapper.map(dto, DocumentEntity.class);

    }

    private DocumentDTOBasicNoRef convertToDTOBasicNoRef(DocumentEntity entity){
        return modelMapper.map(entity, DocumentDTOBasicNoRef.class);
    }

    private DocumentEntity convertBasicNoRefDTOToEntity(DocumentDTOBasicNoRef documentDTOBasicNoRef){
        return modelMapper.map(documentDTOBasicNoRef, DocumentEntity.class);
    }

    private DocumentDTOBasicInfo convertToDTOBasicInfo(DocumentEntity entity){
        return modelMapper.map(entity, DocumentDTOBasicInfo.class);
    }
}
