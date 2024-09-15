package com.momand.docauthor.document.controller;

import com.momand.docauthor.author.dto.AuthorDTO;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    //get list of all documents
    @GetMapping("/allDocuments")
    public List<DocumentDTO> getListOfAllDocuments(){
        Iterable<DocumentEntity> iterable = documentService.findAllDocuments();

        List<DocumentDTO> documentList = new ArrayList<>();

        //convert Iterable to List of documents without authors and references
        iterable.forEach(documentEntity -> {

            DocumentDTO documentDTO = convertToDTO(documentEntity);

            DocumentDTO newDocumentDTO = new DocumentDTO();
            newDocumentDTO.setId(documentDTO.getId());
            newDocumentDTO.setTitle(documentDTO.getTitle());
            newDocumentDTO.setBody(documentDTO.getBody());

            documentList.add(documentDTO);
        });


        return documentList ;
    }

    //get set of all references for a document
    @GetMapping("/{docId}/allDocuments")
    public Set<DocumentDTO> getAllReferencesForDocument(@PathVariable Long docId){
        DocumentEntity documentEntity = documentService.findDocumentById(docId);
        DocumentDTO documentDTO = convertToDTO(documentEntity);
        return documentDTO.getReferences();
    }

    //add reference to a document
    @PostMapping("/addReference/{documentId}/{referenceId}")
    public DocumentDTO postReferenceToDocument(@PathVariable Long documentId, @PathVariable Long referenceId){
        var document = documentService.addReferenceToDocument(documentId, referenceId);
        return convertToDTO(document);
    }

    //get all authors for a document
    @GetMapping("/{docId}/allAuthors")
    public void getAllAuthorsForDocument(@PathVariable Long docId){
    }

    //get all documents for an author id
    @GetMapping("/{authorId}/allDocuments")
    public void getAllDocumentsForAuthor(@PathVariable Long authorId){

    }

    //Get document for a specific id
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

    //add new document
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

    //Update Document by Id
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

    //Delete Document by Id
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

    // Method to convert DocumentEntity to DocumentDTO
    public DocumentDTO convertToDTO(DocumentEntity documentEntity) {
        DocumentDTO documentDTO = modelMapper.map(documentEntity, DocumentDTO.class);

        // Custom conversion logic to avoid recursive mapping for authors
        if(documentEntity.getAuthors() != null ) {
            Set<AuthorDTO> authors = documentEntity.getAuthors().stream()
                    .map(author -> {
                        AuthorDTO authorDTO = modelMapper.map(author, AuthorDTO.class);
                        authorDTO.setDocuments(null); // Break the cycle by not mapping documents
                        return authorDTO;
                    })
                    .collect(Collectors.toSet());

            documentDTO.setAuthors(authors);
        }

        // Custom conversion logic to avoid recursive mapping for reference
        if(documentEntity.getReferences() != null ) {
            Set<DocumentDTO> references = documentEntity.getReferences().stream()
                    .map(reference -> {
                        DocumentDTO referenceDTO = modelMapper.map(reference, DocumentDTO.class);
                        referenceDTO.setReferences(null);// Break the cycle by not mapping documents
                        referenceDTO.setAuthors(null);
                        return referenceDTO;
                    })
                    .collect(Collectors.toSet());

            documentDTO.setReferences(references);
        }

        return documentDTO;
    }

    // Method to convert DocumentDTO to DocumentEntity
    public DocumentEntity convertToEntity(DocumentDTO documentDTO) {
        return modelMapper.map(documentDTO, DocumentEntity.class);
    }
    /*
    private DocumentDTO convertToDTO(DocumentEntity entity){
        return modelMapper.map(entity, DocumentDTO.class);
    }

    private DocumentEntity convertToEntity(DocumentDTO dto){
        return modelMapper.map(dto, DocumentEntity.class);

    }*/

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
