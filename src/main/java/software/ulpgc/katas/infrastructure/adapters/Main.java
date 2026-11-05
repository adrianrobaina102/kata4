package software.ulpgc.katas.infrastructure.adapters;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import software.ulpgc.katas.application.domain.FindMoviesByGenreUseCase;
import software.ulpgc.katas.application.model.Genre;
import software.ulpgc.katas.application.model.Movie;
import software.ulpgc.katas.application.ports.Output;

import java.time.Year;
import java.util.List;

public class Main {
    static void main(String[] args) {
        Javalin.create(config -> init(config)).start(8080);
    }

    private static void init(JavalinConfig config) {
        config.routes.get("/movies", context -> context.result(getExecute(context).result()));
    }

    private static Output getExecute(Context context) {
        return new FindMoviesByGenreUseCase(new JsonMovieSerializer(), getMovies()).execute(new JavalinContextInput(context));
    }

    public static List<Movie> getMovies() {
        return List.of(
                new Movie("The Dark Knight", Genre.ACTION, Year.of(2008), "PG-13"),
                new Movie("Mad Max: Fury Road", Genre.ACTION, Year.of(2015), "R"),
                new Movie("Superbad", Genre.COMEDY, Year.of(2007), "R"),
                new Movie("The Hangover", Genre.COMEDY, Year.of(2009), "R"),
                new Movie("The Godfather", Genre.DRAMA, Year.of(1972), "R"),
                new Movie("Schindler's List", Genre.DRAMA, Year.of(1993), "R"),
                new Movie("The Conjuring", Genre.HORROR, Year.of(2013), "R"),
                new Movie("Alien", Genre.HORROR, Year.of(1979), "R"),
                new Movie("Inception", Genre.SCI_FI, Year.of(2010), "PG-13"),
                new Movie("Interstellar", Genre.SCI_FI, Year.of(2014), "PG-13"),
                new Movie("Se7en", Genre.THRILLER, Year.of(1995), "R"),
                new Movie("Prisoners", Genre.THRILLER, Year.of(2013), "R"),
                new Movie("Titanic", Genre.ROMANCE, Year.of(1997), "PG-13"),
                new Movie("La La Land", Genre.ROMANCE, Year.of(2016), "PG-13"),
                new Movie("The Lord of the Rings: The Fellowship of the Ring", Genre.FANTASY, Year.of(2001), "PG-13"),
                new Movie("Harry Potter and the Sorcerer's Stone", Genre.FANTASY, Year.of(2001), "PG")
        );
    }
}
