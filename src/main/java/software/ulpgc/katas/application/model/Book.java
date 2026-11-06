package software.ulpgc.katas.application.model;

public record Book (String isbn, String title, String author, Category category, int pages, double rating) {
}
