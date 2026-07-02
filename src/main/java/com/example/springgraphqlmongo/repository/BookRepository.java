package com.example.springgraphqlmongo.repository;

import com.example.springgraphqlmongo.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

	List<Book> findByAuthor(String author);

}
