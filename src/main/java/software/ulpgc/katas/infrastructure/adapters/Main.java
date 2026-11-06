package software.ulpgc.katas.infrastructure.adapters;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import software.ulpgc.katas.application.domain.FindBookByAuthorUseCase;
import software.ulpgc.katas.application.domain.FindBookByCategoryUseCase;
import software.ulpgc.katas.application.model.Book;
import software.ulpgc.katas.application.model.Category;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;

public class Main {
    static void main(String[] args) {
        Javalin.create(config -> init(config)).start(8080);
    }

    private static void init(JavalinConfig config) {
        config.routes.get("/books", ctx -> {
            if (ctx.queryParam("author") != null) {
                ctx.result(getExecuteAuthor(ctx).result());
            } else if (ctx.queryParam("category") != null) {
                ctx.result(getExecuteCategory(ctx).result());
            }
            else {
                ctx.result(new JsonBookSerializer().serialize(getBooks()));
            }
        });
    }
    private static Output getExecuteAuthor(Context context) {

        return new FindBookByAuthorUseCase(new JsonBookSerializer(), getBooks()).execute(new JavalinContextInput(context));
    }
    private static Output getExecuteCategory(Context context) {

        return new FindBookByCategoryUseCase(new JsonBookSerializer(), getBooks()).execute(new JavalinContextInput(context));
    }
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
