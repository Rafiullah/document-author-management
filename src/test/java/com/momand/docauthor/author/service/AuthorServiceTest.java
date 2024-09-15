package com.momand.docauthor.author.service;

import com.momand.docauthor.author.entity.AuthorEntity;
import com.momand.docauthor.author.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorProducerKafka authorProducerKafka;
    @InjectMocks
    private AuthorService authorService;

    @Test
    public void testAddNewAuthorEntity(){
        AuthorEntity expectedEntity = new AuthorEntity();
        expectedEntity.setFirstName("Rafiullah");
        expectedEntity.setLastName("Momand");

        when(authorRepository.save(expectedEntity)).thenReturn(expectedEntity);

        AuthorEntity actualEntity = authorService.addNewAuthorEntity(expectedEntity);

        verify(authorRepository).save(expectedEntity);
        verify(authorProducerKafka).sendAuthorEvent("Author with id" + actualEntity.getId() + " added");

        /*assertEquals("Rafiullah", actualEntity.getFirstName());
        assertEquals("Momand", actualEntity.getLastName());*/
        assertEquals(expectedEntity, actualEntity);
    }

}
