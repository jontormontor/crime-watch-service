package com.example.springgraphqlmongo.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {

	@Id
	private String id;

	@NotBlank
	@Size(max = 255)
	private String title;

	@NotBlank
	@Size(max = 255)
	private String author;

	@Size(max = 100)
	private String genre;

	private Instant createdAt;

}
