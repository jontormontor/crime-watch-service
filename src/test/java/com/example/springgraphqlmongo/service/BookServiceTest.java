package com.example.springgraphqlmongo.service;

import com.example.springgraphqlmongo.domain.Book;
import com.example.springgraphqlmongo.exception.ResourceNotFoundException;
import com.example.springgraphqlmongo.graphql.dto.CreateBookInput;
import com.example.springgraphqlmongo.graphql.dto.UpdateBookInput;
import com.example.springgraphqlmongo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	@Test
	void getByIdReturnsBookWhenFound() {
		Book book = sampleBook("1");
		when(bookRepository.findById("1")).thenReturn(Optional.of(book));

		Book result = bookService.getById("1");

		assertThat(result).isEqualTo(book);
	}

	@Test
	void getByIdThrowsWhenNotFound() {
		when(bookRepository.findById("missing")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> bookService.getById("missing"))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("missing");
	}

	@Test
	void findAllReturnsAllBooksWhenAuthorNotProvided() {
		List<Book> books = List.of(sampleBook("1"), sampleBook("2"));
		when(bookRepository.findAll()).thenReturn(books);

		assertThat(bookService.findAll(null)).containsExactlyElementsOf(books);
	}

	@Test
	void findAllFiltersByAuthor() {
		List<Book> books = List.of(sampleBook("1"));
		when(bookRepository.findByAuthor("Author")).thenReturn(books);

		assertThat(bookService.findAll("Author")).containsExactlyElementsOf(books);
	}

	@Test
	void createPersistsBookWithTimestamp() {
		CreateBookInput input = new CreateBookInput("Title", "Author", "Fiction");
		when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
			Book saved = invocation.getArgument(0);
			saved.setId("generated-id");
			return saved;
		});

		Book result = bookService.create(input);

		assertThat(result.getId()).isEqualTo("generated-id");
		assertThat(result.getTitle()).isEqualTo("Title");
		assertThat(result.getAuthor()).isEqualTo("Author");
		assertThat(result.getGenre()).isEqualTo("Fiction");
		assertThat(result.getCreatedAt()).isNotNull();
	}

	@Test
	void updateModifiesExistingBook() {
		Book existing = sampleBook("1");
		when(bookRepository.findById("1")).thenReturn(Optional.of(existing));
		when(bookRepository.save(existing)).thenReturn(existing);

		Book result = bookService.update("1", new UpdateBookInput("New Title", null, "Sci-Fi"));

		assertThat(result.getTitle()).isEqualTo("New Title");
		assertThat(result.getAuthor()).isEqualTo("Author");
		assertThat(result.getGenre()).isEqualTo("Sci-Fi");
	}

	@Test
	void deleteRemovesExistingBook() {
		when(bookRepository.existsById("1")).thenReturn(true);

		boolean deleted = bookService.delete("1");

		assertThat(deleted).isTrue();
		verify(bookRepository).deleteById("1");
	}

	@Test
	void deleteThrowsWhenNotFound() {
		when(bookRepository.existsById("missing")).thenReturn(false);

		assertThatThrownBy(() -> bookService.delete("missing"))
				.isInstanceOf(ResourceNotFoundException.class);
	}

	private Book sampleBook(String id) {
		return Book.builder()
				.id(id)
				.title("Title")
				.author("Author")
				.genre("Fiction")
				.createdAt(Instant.parse("2025-01-01T00:00:00Z"))
				.build();
	}

}
