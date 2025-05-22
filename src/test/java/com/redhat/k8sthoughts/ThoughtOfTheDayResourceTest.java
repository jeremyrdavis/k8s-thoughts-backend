package com.redhat.k8sthoughts;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class ThoughtOfTheDayResourceTest {

    @InjectMock
    ThoughtOfTheDayRepository repository;

    private ThoughtOfTheDay thought1;
    private ThoughtOfTheDay thought2;

    @BeforeEach
    void setUp() {
        // Create test data
        thought1 = new ThoughtOfTheDay();
        thought1.id = 1L;
        thought1.thought = "Test thought 1";
        thought1.author = "Test Author 1";
        thought1.day = LocalDate.now();

        thought2 = new ThoughtOfTheDay();
        thought2.id = 2L;
        thought2.thought = "Test thought 2";
        thought2.author = "Test Author 2";
        thought2.day = LocalDate.now().minusDays(1);
        // Set up mock
        when(repository.listAll()).thenReturn(Arrays.asList(thought1, thought2));
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(thought1));
        when(repository.findByIdOptional(9999L)).thenReturn(Optional.empty());
        Mockito.doAnswer(invocation -> {
            ThoughtOfTheDay t = invocation.getArgument(0);
            t.id = 3L;
            return null;
        }).when(repository).persist(any(ThoughtOfTheDay.class));
        when(repository.findById(9998L)).thenReturn(null);
        when(repository.deleteById(9999L)).thenReturn(false);
        when(repository.deleteById(9997L)).thenReturn(true);
    }

    @Test
    void testListEndpoint() {
        given()
          .when().get("/api/thoughts")
          .then()
             .statusCode(200)
             .body("size()", is(2))
             .body("thought", hasItems("Test thought 1", "Test thought 2"))
             .body("author", hasItems("Test Author 1", "Test Author 2"));
    }

    @Test
    void testGetEndpoint() {
        given()
          .when().get("/api/thoughts/1")
          .then()
             .statusCode(200)
             .body("thought", is("Test thought 1"))
             .body("author", is("Test Author 1"));
    }

    @Test
    void testGetEndpointNotFound() {
        given()
          .when().get("/api/thoughts/9999")
          .then()
             .statusCode(404);
    }

    @Test
    void testRandomEndpoint() {
        given()
          .when().get("/api/thoughts/random")
          .then()
             .statusCode(200)
             .body("thought", is(anyOf(equalTo("Test thought 1"), equalTo("Test thought 2"))))
             .body("author", is(anyOf(equalTo("Test Author 1"), equalTo("Test Author 2"))));
    }

    @Test
    void testCreateEndpoint() {
        // Set up mock for persist method
        ThoughtOfTheDay newThought = new ThoughtOfTheDay();
        newThought.thought = "New test thought";
        newThought.author = "New Author";
        newThought.day = LocalDate.now();

        given()
          .contentType(ContentType.JSON)
          .body(newThought)
          .when().post("/api/thoughts")
          .then()
             .statusCode(201)
             .body("thought", is("New test thought"))
             .body("author", is("New Author"));
    }

    @Test
    void testUpdateEndpoint() {
        // Set up mock
        when(repository.findById(1L)).thenReturn(thought1);

        ThoughtOfTheDay updatedThought = new ThoughtOfTheDay();
        updatedThought.thought = "Updated thought";
        updatedThought.author = "Updated Author";
        updatedThought.day = LocalDate.now();

        given()
          .contentType(ContentType.JSON)
          .body(updatedThought)
          .when().put("/api/thoughts/1")
          .then()
             .statusCode(200)
             .body("thought", is("Updated thought"))
             .body("author", is("Updated Author"));
    }

    @Test
    void testUpdateEndpointNotFound() {
        ThoughtOfTheDay updatedThought = new ThoughtOfTheDay();
        updatedThought.thought = "Updated thought";
        updatedThought.author = "Updated Author";

        given()
          .contentType(ContentType.JSON)
          .body(updatedThought)
          .when().put("/api/thoughts/9998")
          .then()
             .statusCode(404);
    }

    @Test
    void testDeleteEndpoint() {
        given()
          .when().delete("/api/thoughts/9997")
          .then()
             .statusCode(204);
    }

    @Test
    void testDeleteEndpointNotFound() {
        given()
          .when().delete("/api/thoughts/9999")
          .then()
             .statusCode(404);
    }
}