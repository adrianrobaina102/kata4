package software.ulpgc.katas.application.domain;

import software.ulpgc.katas.application.model.Book;
import software.ulpgc.katas.application.model.Category;
import software.ulpgc.katas.application.ports.BookSerializer;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;

public class FindBookByCategoryUseCase implements UseCase{
    private final BookSerializer serializer;
    private final List<Book> books;

    public FindBookByCategoryUseCase(BookSerializer serializer, List<Book> books) {
        this.serializer = serializer;
        this.books = books;
    }

    @Override
    public Output execute(Input input) {
        String author = input.get("category").asString();
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("El parámetro 'category' es obligatorio");
        }
        List<Book> bookList = FindBookByAuthor(Category.valueOf(author.toUpperCase()));
        return () -> this.serializer.serialize(bookList);
    }

    private List<Book> FindBookByAuthor(Category category) {
        return books.stream().filter(p -> p.category().equals(category)).toList();
    }
}
