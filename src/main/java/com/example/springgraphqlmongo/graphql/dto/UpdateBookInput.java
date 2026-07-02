package com.example.springgraphqlmongo.graphql.dto;

import jakarta.validation.constraints.Size;

public record UpdateBookInput(
		@Size(max = 255) String title,
		@Size(max = 255) String author,
		@Size(max = 100) String genre
) {
}
