package com.example.springgraphqlmongo.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
class BookGraphQlIntegrationTest {

	private static final DockerImageName MONGO_IMAGE = DockerImageName.parse("mongo:7");

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_IMAGE);

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired
	private MockMvc mockMvc;

	private HttpGraphQlTester graphQlTester() {
		WebTestClient client = MockMvcWebTestClient.bindTo(mockMvc).build();
		return HttpGraphQlTester.create(client);
	}

	@Test
	void createBookQueryAndDeleteBook() {
		String bookId = graphQlTester()
				.document("""
						mutation {
						  createBook(input: { title: "Dune", author: "Frank Herbert", genre: "Sci-Fi" }) {
						    id
						    title
						    author
						    genre
						    createdAt
						  }
						}
						""")
				.execute()
				.path("createBook.id")
				.entity(String.class)
				.get();

		graphQlTester()
				.document("""
						query($id: ID!) {
						  book(id: $id) {
						    title
						    author
						  }
						}
						""")
				.variable("id", bookId)
				.execute()
				.path("book.title")
				.entity(String.class)
				.isEqualTo("Dune");

		graphQlTester()
				.document("""
						query {
						  books(author: "Frank Herbert") {
						    id
						    title
						  }
						}
						""")
				.execute()
				.path("books")
				.entityList(Object.class)
				.hasSize(1);

		graphQlTester()
				.document("""
						mutation($id: ID!) {
						  deleteBook(id: $id)
						}
						""")
				.variable("id", bookId)
				.execute()
				.path("deleteBook")
				.entity(Boolean.class)
				.isEqualTo(true);
	}

	@Test
	void bookNotFoundReturnsGraphQlError() {
		graphQlTester()
				.document("""
						query {
						  book(id: "non-existent-id") {
						    id
						  }
						}
						""")
				.execute()
				.errors()
				.expect(error -> error.getMessage().contains("Book not found"))
				.verify();
	}

}
