package com.momand.docauthor.document.controller;

import com.momand.docauthor.document.dto.DocumentDTO;
import com.momand.docauthor.document.dto.DocumentDTOBasic;
import com.momand.docauthor.document.dto.DocumentDTOBasicNoRef;
import com.momand.docauthor.document.entity.DocumentEntity;
import com.momand.docauthor.document.service.DocumentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    public List<DocumentDTOBasic> getAllDocuments() {
        var documents = documentService.findAllDocuments();
        List<DocumentDTOBasic> documentsDtoList = new ArrayList<>();
        for (var document : documents) {
            documentsDtoList.add(convertToDTOBasic(document));
        }
        return documentsDtoList;
    }

    @GetMapping("/{id}")
    public DocumentDTO getDocumentById(@PathVariable("id") Long id){
        return convertToDTO(documentService.findDocumentById(id));
    }

    @GetMapping("/idb/{idb}")
    public DocumentDTOBasic getDocumentByIdBasic(@PathVariable("idb") Long id){
        return convertToDTOBasic(documentService.findDocumentById(id));
    }

    @GetMapping("/idbr/{idbr}")
    public DocumentDTOBasicNoRef getDocumentByIdBasicNoRef(@PathVariable("idbr") Long id){
        return convertToDTOBasicNoRef(documentService.findDocumentById(id));
    }

    //add new author
    @PostMapping("/addNewDocument")
    public DocumentDTO postNewDocument(@Valid @RequestBody DocumentDTO documentDTO){
        logger.info("Received DocumentDTO: {}", documentDTO.toString());
        var entity = convertToEntity(documentDTO);
        logger.info("Converted Document Entity: {}", entity);
        var document = documentService.addNewDocument(entity);
        return convertToDTO(document);
    }

    //add new author to existing documents
    @PostMapping("/addNewDocumentToExistingAuthor/{authorId}")
    public DocumentDTO postNewAuthorForExistingDocuments(@Valid @RequestBody DocumentDTO documentDTO, @PathVariable Long authorId){
        logger.info("Received DocumentDTO: {}", documentDTO.toString());
        var entity = convertToEntity(documentDTO);
        logger.info("Converted Document Entity: {}", entity);
        var document = documentService.addNewDocumentForExistingAuthor(entity, authorId);
        return convertToDTO(document);
    }

    //add existing author to existing document
    @PostMapping("/assignDocumentToAuthor/{docId}/{authId}")
    public DocumentDTO postExistingAuthorForExistingDocuments(@PathVariable Long docId, @PathVariable Long authId){
        var document = documentService.addExistingDocumentToExistingAuthor(docId, authId);
        return convertToDTO(document);
    }

    @PutMapping("/{id}")
    public void putDocument(@PathVariable("id") Long id,
                            @Valid @RequestBody DocumentDTO documentDTO
    ) {
        if (!id.equals(documentDTO.getId())) throw new
                ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "id does not match."
        );
        var documentEntity = convertToEntity(documentDTO);
        documentService.updateDocumentEntity(id, documentEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteDocumentById(@PathVariable("id") Long id) {
        documentService.removeDocumentById(id);
    }


    @PostMapping("/addNewDocumentBasic")
    public DocumentDTOBasicNoRef postNewDocumentBasic(@Valid @RequestBody DocumentDTOBasicNoRef documentDTO){
        var entity = convertBasicNoRefDTOToEntity(documentDTO);
        var document = documentService.addNewDocument(entity);
        return convertToDTOBasicNoRef(document);
    }

    private DocumentDTO convertToDTO(DocumentEntity entity){
        return modelMapper.map(entity, DocumentDTO.class);
    }

    private DocumentEntity convertToEntity(DocumentDTO dto){
        return modelMapper.map(dto, DocumentEntity.class);

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
}
