package software.ulpgc.katas.application.ports;


import software.ulpgc.katas.application.model.Movie;

import java.util.List;

public interface MovieSerializer {
    String serialize(List<Movie> movie);
}
