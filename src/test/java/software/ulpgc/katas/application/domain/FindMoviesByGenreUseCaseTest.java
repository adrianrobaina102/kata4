package software.ulpgc.katas.application.domain;

import org.junit.jupiter.api.Test;
import software.ulpgc.katas.application.model.Genre;
import software.ulpgc.katas.application.model.Movie;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.MovieSerializer;
import software.ulpgc.katas.application.ports.Output;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FindMoviesByGenreUseCaseTest {

    private final List<Movie> sampleMovies = List.of(
            new Movie("The Dark Knight", Genre.ACTION, Year.of(2008), "PG-13"),
            new Movie("Inception", Genre.SCI_FI, Year.of(2010), "PG-13"),
            new Movie("Mad Max: Fury Road", Genre.ACTION, Year.of(2015), "R")
    );

    private final MovieSerializer plainSerializer = movies ->
            movies.stream().map(Movie::title).collect(Collectors.joining(","));

    private Input inputWith(String value) {
        return param -> () -> value;
    }

    @Test
    void shouldFilterMoviesByValidGenreIgnoringCase() {
        FindMoviesByGenreUseCase useCase = new FindMoviesByGenreUseCase(plainSerializer, sampleMovies);

        Output output = useCase.execute(inputWith("action"));

        assertEquals("The Dark Knight,Mad Max: Fury Road", output.result());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGenreIsMissing() {
        FindMoviesByGenreUseCase useCase = new FindMoviesByGenreUseCase(plainSerializer, sampleMovies);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith(null)));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith("")));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGenreDoesNotExist() {
        FindMoviesByGenreUseCase useCase = new FindMoviesByGenreUseCase(plainSerializer, sampleMovies);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith("inexistente")));
    }
}
