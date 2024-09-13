package com.momand.docauthor.document.repository;

import com.momand.docauthor.document.entity.DocumentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {
}
