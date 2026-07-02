package com.example.springgraphqlmongo.graphql;

import com.example.springgraphqlmongo.domain.Book;
import com.example.springgraphqlmongo.graphql.dto.CreateBookInput;
import com.example.springgraphqlmongo.graphql.dto.UpdateBookInput;
import com.example.springgraphqlmongo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@QueryMapping
	public Book book(@Argument String id) {
		return bookService.getById(id);
	}

	@QueryMapping
	public List<Book> books(@Argument String author) {
		return bookService.findAll(author);
	}

	@MutationMapping
	public Book createBook(@Argument CreateBookInput input) {
		return bookService.create(input);
	}

	@MutationMapping
	public Book updateBook(@Argument String id, @Argument UpdateBookInput input) {
		return bookService.update(id, input);
	}

	@MutationMapping
	public boolean deleteBook(@Argument String id) {
		return bookService.delete(id);
	}

}
