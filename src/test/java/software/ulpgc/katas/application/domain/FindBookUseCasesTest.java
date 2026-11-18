package software.ulpgc.katas.application.domain;

import org.junit.jupiter.api.Test;
import software.ulpgc.katas.application.model.Book;
import software.ulpgc.katas.application.model.Category;
import software.ulpgc.katas.application.ports.BookSerializer;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FindBookUseCasesTest {

    private final List<Book> sampleBooks = List.of(
            new Book("978-0451524935", "1984", "George Orwell", Category.FICTION, 328, 4.7),
            new Book("978-0261102217", "The Hobbit", "J.R.R. Tolkien", Category.FANTASY, 310, 4.8),
            new Book("978-0441172719", "Dune", "Frank Herbert", Category.SCI_FI, 412, 4.7)
    );

    private final BookSerializer plainSerializer = books ->
            books.stream().map(Book::title).collect(Collectors.joining(","));

    private Input inputWith(String value) {
        return param -> () -> value;
    }

    @Test
    void shouldFilterBooksByAuthor() {
        FindBookByAuthorUseCase useCase = new FindBookByAuthorUseCase(plainSerializer, sampleBooks);

        Output output = useCase.execute(inputWith("George Orwell"));

        assertEquals("1984", output.result());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenAuthorIsMissing() {
        FindBookByAuthorUseCase useCase = new FindBookByAuthorUseCase(plainSerializer, sampleBooks);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith(null)));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith("   ")));
    }

    @Test
    void shouldFilterBooksByValidCategoryIgnoringCase() {
        FindBookByCategoryUseCase useCase = new FindBookByCategoryUseCase(plainSerializer, sampleBooks);

        Output output = useCase.execute(inputWith("fantasy"));

        assertEquals("The Hobbit", output.result());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidOrMissingCategory() {
        FindBookByCategoryUseCase useCase = new FindBookByCategoryUseCase(plainSerializer, sampleBooks);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith("terror")));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(inputWith("")));
    }
}
