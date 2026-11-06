package software.ulpgc.katas.application.ports;

import software.ulpgc.katas.application.model.Book;

import java.util.List;

public interface BookSerializer {
    String serialize(List<Book> book);
}
