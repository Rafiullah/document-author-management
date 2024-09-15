-- Insert data for authors
INSERT INTO author_entity (id, first_name, last_name) VALUES (1, 'John', 'Doe');
INSERT INTO author_entity (id, first_name, last_name) VALUES (2, 'Ahmad', 'Kateb');
INSERT INTO author_entity (id, first_name, last_name) VALUES (3, 'Emily', 'Johnson');
INSERT INTO author_entity (id, first_name, last_name) VALUES (4, 'Gabi', 'Brown');

-- Insert data for documents
INSERT INTO document_entity (id, title, body) VALUES (1, 'Document One', 'This is the body of Document One.');
INSERT INTO document_entity (id, title, body) VALUES (2, 'Document Two', 'This is the body of Document Two.');
INSERT INTO document_entity (id, title, body) VALUES (3, 'Document Three', 'This is the body of Document Three.');
INSERT INTO document_entity (id, title, body) VALUES (4, 'Document Four', 'This is the body of Document Four.');

-- Insert data for article_author (relationship between authors and documents)
INSERT INTO article_author (article_id, author_id) VALUES (1, 1);
INSERT INTO article_author (article_id, author_id) VALUES (1, 2);
INSERT INTO article_author (article_id, author_id) VALUES (2, 2);
INSERT INTO article_author (article_id, author_id) VALUES (3, 3);
INSERT INTO article_author (article_id, author_id) VALUES (4, 4);
INSERT INTO article_author (article_id, author_id) VALUES (4, 1);

-- Insert data for document_references (references between documents)
INSERT INTO document_references (document_id, reference_document_id) VALUES (1, 2);
INSERT INTO document_references (document_id, reference_document_id) VALUES (2, 3);
INSERT INTO document_references (document_id, reference_document_id) VALUES (3, 4);
INSERT INTO document_references (document_id, reference_document_id) VALUES (4, 1);

--- Adjust the ids
SELECT setval('author_entity_id_seq', (SELECT MAX(id) FROM author_entity));
SELECT setval('document_entity_id_seq', (SELECT MAX(id) FROM document_entity));