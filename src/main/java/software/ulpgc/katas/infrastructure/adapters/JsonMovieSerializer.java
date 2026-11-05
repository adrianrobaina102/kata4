package software.ulpgc.katas.infrastructure.adapters;

import com.google.gson.Gson;
import software.ulpgc.katas.application.model.Movie;
import software.ulpgc.katas.application.ports.MovieSerializer;

import java.util.List;

public class JsonMovieSerializer implements MovieSerializer {
    @Override
    public String serialize(List<Movie> movie) {
        return new Gson().toJson(movie);
    }
}
