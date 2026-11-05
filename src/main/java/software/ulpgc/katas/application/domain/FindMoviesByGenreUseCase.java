package software.ulpgc.katas.application.domain;

import software.ulpgc.katas.application.model.Genre;
import software.ulpgc.katas.application.model.Movie;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.MovieSerializer;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;

public class FindMoviesByGenreUseCase implements UseCase{
    private final MovieSerializer serializer;
    private static List<Movie> movies;

    public FindMoviesByGenreUseCase(MovieSerializer serializer, List<Movie> movies) {
        this.serializer = serializer;
        this.movies = movies;
    }

    @Override
    public Output execute(Input input) {
        Genre genre = Genre.valueOf(input.get("genre").asString().toUpperCase());
        List<Movie> movieList = findMoviesByGenre(genre);
        return () -> serializer.serialize(movieList);
    }

    private List<Movie> findMoviesByGenre(Genre genre) {
        return movies.stream().filter(p -> p.genre() == genre).toList();
    }
}
