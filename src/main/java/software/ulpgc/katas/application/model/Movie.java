package software.ulpgc.katas.application.model;

import java.time.Year;

public record Movie (String title, Genre genre,
                     Year year, String rating) {
}
