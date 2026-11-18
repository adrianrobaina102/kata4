package software.ulpgc.katas.application.domain;

import software.ulpgc.katas.application.model.Book;
import software.ulpgc.katas.application.ports.BookSerializer;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;

public class FindBookByAuthorUseCase implements UseCase {
    private final BookSerializer serializer;
    private final List<Book> books;

    public FindBookByAuthorUseCase(BookSerializer serializer, List<Book> books) {
        this.serializer = serializer;
        this.books = books;
    }

    @Override
    public Output execute(Input input) {
        String author = input.get("author").asString();
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("El parámetro 'author' es obligatorio");
        }
        List<Book> bookList = FindBookByAuthor(author);
        return () -> this.serializer.serialize(bookList);
    }

    private List<Book> FindBookByAuthor(String author) {
        return books.stream().filter(p -> p.author().equals(author)).toList();
    }
}
