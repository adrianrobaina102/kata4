package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.application.model.Book;
import software.ulpgc.katas.application.model.Category;

import java.util.List;

public class Main {
    public static List<Book> getBooks() {
        return List.of(
            new Book("978-0451524935", "1984", "George Orwell", Category.FICTION, 328, 4.7),
            new Book("978-0451526342", "Animal Farm", "George Orwell", Category.FICTION, 112, 4.6),
            new Book("978-0261102217", "The Hobbit", "J.R.R. Tolkien", Category.FANTASY, 310, 4.8),
            new Book("978-0261102385", "The Fellowship of the Ring", "J.R.R. Tolkien", Category.FANTASY, 423, 4.9),
            new Book("978-0441172719", "Dune", "Frank Herbert", Category.SCI_FI, 412, 4.7),
            new Book("978-0062316097", "Sapiens: A Brief History of Humankind", "Yuval Noah Harari", Category.HISTORY, 443, 4.6),
            new Book("978-1451648539", "Steve Jobs", "Walter Isaacson", Category.BIOGRAPHY, 656, 4.5),
            new Book("978-0345391803", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", Category.SCI_FI, 224, 4.4)
        );
    }
}
