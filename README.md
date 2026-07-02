# Crime Info Service

Production-oriented Spring Boot 3 base application using MongoDB and GraphQL.

## Stack

- Java 21
- Spring Boot 3.4
- Spring Data MongoDB
- Spring for GraphQL
- Gradle (Groovy DSL)
- Testcontainers

## Prerequisites

- JDK 21
- Docker (for MongoDB and integration tests)

## Quick start

1. Start MongoDB:

```bash
docker compose up -d
```

2. Run the application:

```bash
./gradlew bootRun
```

3. Open GraphiQL at [http://localhost:8080/graphiql](http://localhost:8080/graphiql)

## Sample GraphQL operations

Create a book:

```graphql
mutation {
  createBook(input: { title: "Dune", author: "Frank Herbert", genre: "Sci-Fi" }) {
    id
    title
    author
    genre
    createdAt
  }
}
```

List books:

```graphql
query {
  books {
    id
    title
    author
    genre
  }
}
```

Get a book by ID:

```graphql
query {
  book(id: "YOUR_BOOK_ID") {
    id
    title
    author
  }
}
```

Update a book:

```graphql
mutation {
  updateBook(id: "YOUR_BOOK_ID", input: { title: "Dune: Part One" }) {
    id
    title
  }
}
```

Delete a book:

```graphql
mutation {
  deleteBook(id: "YOUR_BOOK_ID")
}
```

## Profiles

| Profile | Purpose |
|---------|---------|
| `dev` (default) | Local development with GraphiQL enabled |
| `prod` | Production; uses `MONGODB_URI` env var, GraphiQL disabled |
| `test` | Used by integration tests |

Run with a specific profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## Health check

```bash
curl http://localhost:8080/actuator/health
```

## Testing

```bash
./gradlew test
```

Integration tests use Testcontainers and require Docker. When Docker is not available, those tests are skipped automatically.

## Project structure

```
src/main/java/com/example/springgraphqlmongo/
├── domain/          # MongoDB @Document entities
├── repository/      # Spring Data repositories
├── service/         # Business logic
├── graphql/         # GraphQL controllers and DTOs
└── exception/       # Shared exceptions
```

## Environment variables

See [`.env.example`](.env.example) for production-style configuration.
