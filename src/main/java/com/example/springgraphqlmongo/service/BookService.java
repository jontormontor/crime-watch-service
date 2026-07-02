package com.example.springgraphqlmongo.service;

import com.example.springgraphqlmongo.domain.Book;
import com.example.springgraphqlmongo.exception.ResourceNotFoundException;
import com.example.springgraphqlmongo.graphql.dto.CreateBookInput;
import com.example.springgraphqlmongo.graphql.dto.UpdateBookInput;
import com.example.springgraphqlmongo.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;

	public Book getById(String id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
	}

	public List<Book> findAll(String author) {
		if (author != null && !author.isBlank()) {
			return bookRepository.findByAuthor(author);
		}
		return bookRepository.findAll();
	}

	public Book create(@Valid CreateBookInput input) {
		Book book = Book.builder()
				.title(input.title())
				.author(input.author())
				.genre(input.genre())
				.createdAt(Instant.now())
				.build();
		return bookRepository.save(book);
	}

	public Book update(String id, @Valid UpdateBookInput input) {
		Book book = getById(id);

		if (input.title() != null) {
			book.setTitle(input.title());
		}
		if (input.author() != null) {
			book.setAuthor(input.author());
		}
		if (input.genre() != null) {
			book.setGenre(input.genre());
		}

		return bookRepository.save(book);
	}

	public boolean delete(String id) {
		if (!bookRepository.existsById(id)) {
			throw new ResourceNotFoundException("Book not found with id: " + id);
		}
		bookRepository.deleteById(id);
		return true;
	}

}
