package com.momand.docauthor.author.controller;

import com.momand.docauthor.author.dto.AuthorDTO;
import com.momand.docauthor.author.dto.AuthorDTOBasic;
import com.momand.docauthor.author.dto.AuthorDTOBasicNoRef;
import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.service.AuthorService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequestMapping("/api/v1/authors")
@RestController
public class AuthorController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    public AuthorController(AuthorService authorService, ModelMapper modelMapper) {
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable("id") Long id){
        return convertToDTO(authorService.findAuthorById(id));
    }

    @GetMapping("/idb/{idb}")
    public AuthorDTOBasic getAuthorByIdBasic(@PathVariable("idb") Long id){
        return convertToDTOBasic(authorService.findAuthorById(id));
    }

    @GetMapping("/idbr/{idbr}")
    public AuthorDTOBasicNoRef getAuthorByIdBasicNoRef(@PathVariable("idbr") Long id){
        return convertToDTOBasicNoRef(authorService.findAuthorById(id));
    }

    //add new author
    @PostMapping("/addNewAuthor")
    public AuthorDTO postNewAuthor(@Valid @RequestBody AuthorDTO authorDTO){
        logger.info("Received AuthorDTO: {}", authorDTO.toString());
        var entity = convertToEntity(authorDTO);
        logger.info("Converted Author Entity: {}", entity);
        var author = authorService.addNewAuthorEntity(entity);
        return convertToDTO(author);
    }

    //add new author to existing documents
    @PostMapping("/addNewAuthorToExistingDocument/{docId}")
    public AuthorDTO postNewAuthorForExistingDocuments(@Valid @RequestBody AuthorDTO authorDTO, @PathVariable Long docId){
        logger.info("Received AuthorDTO: {}", authorDTO.toString());
        var entity = convertToEntity(authorDTO);
        logger.info("Converted Author Entity: {}", entity);
        var author = authorService.addNewAuthorForExistingDocument(entity, docId);
        return convertToDTO(author);
    }

    //add existing author to existing document
    @PostMapping("/assignAuthorToDocument/{authId}/{docId}")
    //public String postExistingAuthorForExistingDocuments(@PathVariable Long authId, @PathVariable Long docId){
    public AuthorDTO postExistingAuthorForExistingDocuments(@PathVariable Long authId, @PathVariable Long docId){
        var author = authorService.addExistingAuthorToExistingDocument(authId, docId);
        return convertToDTO(author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthorById(@PathVariable("id") Long id){
        authorService.removeAuthorById(id);
    }

    // For-each loop
    @GetMapping("/allfor")
    public List<AuthorDTO> getAuthorsForLoop() {
        var authors = authorService.findAllAuthors();
        List<AuthorDTO> authorsDtoList = new ArrayList<>();

        for (var author : authors) {
            authorsDtoList.add(convertToDTO(author));
        }

        return authorsDtoList;
    }

    @GetMapping("/oldall")
    public List<AuthorDTO> getAuthors() {
        var authorsList = StreamSupport
                .stream(authorService.findAllAuthors().spliterator(), false)
                .collect(Collectors.toList());
        return authorsList
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<AuthorDTO> getAuthorsOptimized() {
        return StreamSupport
                .stream(authorService.findAllAuthors().spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuthorDTO convertToDTO(AuthorEntity entity){
        return modelMapper.map(entity, AuthorDTO.class);
    }

    private AuthorEntity convertToEntity(AuthorDTO dto){
        return modelMapper.map(dto, AuthorEntity.class);
    }

    private AuthorDTOBasic convertToDTOBasic(AuthorEntity entity){
        return modelMapper.map(entity, AuthorDTOBasic.class);
    }

    private AuthorEntity convertToEntity(AuthorDTOBasic dto){
        return modelMapper.map(dto, AuthorEntity.class);
    }

    private AuthorDTOBasicNoRef convertToDTOBasicNoRef(AuthorEntity entity){
        return modelMapper.map(entity, AuthorDTOBasicNoRef.class);
    }

    private AuthorEntity convertToEntity(AuthorDTOBasicNoRef dto){
        return modelMapper.map(dto, AuthorEntity.class);
    }
}
