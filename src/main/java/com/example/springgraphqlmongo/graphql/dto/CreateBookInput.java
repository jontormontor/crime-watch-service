package com.example.springgraphqlmongo.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBookInput(
		@NotBlank @Size(max = 255) String title,
		@NotBlank @Size(max = 255) String author,
		@Size(max = 100) String genre
) {
}
