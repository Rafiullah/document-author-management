package com.momand.docauthor.author.controller;

import com.momand.docauthor.author.dto.AuthorDTO;
import com.momand.docauthor.author.dto.AuthorDTOBasic;
import com.momand.docauthor.author.dto.AuthorDTOBasicNoRef;
import com.momand.docauthor.author.dto.AuthorDTODocsOnly;
import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.service.AuthorService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    //getAuthorsWithDocs
    @GetMapping
    public List<AuthorDTOBasic> getDocuments() {
        return StreamSupport
                .stream(authorService.findAllAuthors().spliterator(), false)
                .map(this::convertToDTOBasic)
                .collect(Collectors.toList());
    }

    @GetMapping("/list-authors-details-only")
    public List<AuthorDTOBasicNoRef> getAuthorsWithoutDocs(){
        return StreamSupport
                .stream(authorService.findAllAuthors().spliterator(), false)
                .map(this::convertToDTOBasicNoRef)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuthorDTOBasic getAuthorByIdBasic(@PathVariable("id") Long id){
        return convertToDTOBasic(authorService.findAuthorById(id));
    }

    @GetMapping("/{id}/author-details")
    public AuthorDTOBasicNoRef getAuthorByIdBasicNoRef(@PathVariable("id") Long id){
        return convertToDTOBasicNoRef(authorService.findAuthorById(id));
    }

    @GetMapping("/{id}/list-documents-only")
    public AuthorDTODocsOnly getListOfDocsByAnAuthor(@PathVariable("id") Long id){
        return convertToDTODocsOnly(authorService.findAuthorById(id));
    }

    @PostMapping
    public AuthorDTOBasicNoRef postNewOther(@Valid @RequestBody AuthorDTOBasicNoRef authorDTOBasicNoRef){
        var entity = convertToEntity(authorDTOBasicNoRef);
        var author = authorService.addNewAuthorEntity(entity);
        return convertToDTOBasicNoRef(author);
    }

    @PutMapping("/{id}")
    public void putAuthor(@PathVariable("id") Long id,
                            @Valid @RequestBody AuthorDTOBasic authorDTOBasic
    ) {
        if (!id.equals(authorDTOBasic.getId())) throw new
                ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "id does not match."
        );
        var authorEntity = convertToEntity(authorDTOBasic);
        authorService.updateAuthorEntity(id, authorEntity);
    }

    //@DeleteMapping("/{id}")
    @RequestMapping(value="/{id}", method={RequestMethod.DELETE, RequestMethod.GET})
    public void deleteAuthorById(@PathVariable("id") Long id){
        authorService.removeAuthorById(id);
    }

    private AuthorDTO convertToDTO(AuthorEntity entity){
        return modelMapper.map(entity, AuthorDTO.class);
    }

    //Method to convert AuthorDTO to AuthorEntity
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

    private AuthorDTODocsOnly convertToDTODocsOnly(AuthorEntity entity){
        return modelMapper.map(entity, AuthorDTODocsOnly.class);
    }

    private AuthorEntity convertToEntity(AuthorDTODocsOnly dto){
        return modelMapper.map(dto, AuthorEntity.class);
    }

}
