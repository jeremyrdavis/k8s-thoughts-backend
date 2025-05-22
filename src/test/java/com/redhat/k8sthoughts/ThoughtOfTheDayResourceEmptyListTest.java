package com.redhat.k8sthoughts;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ThoughtOfTheDayResourceEmptyListTest {

    @InjectMock
    ThoughtOfTheDayRepository repository;

    @BeforeEach
    void setUp() {
        // Create test data
        when(repository.listAll()).thenReturn(Collections.emptyList());

    }
    @Test
    void testRandomEndpointNoThoughts() {
        // Set up mock to return empty list

        given()
                .when().get("/api/thoughts/random")
                .then()
                .statusCode(404);
    }

}
