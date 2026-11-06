package software.ulpgc.katas.infrastructure.adapters;
import com.google.gson.Gson;
import software.ulpgc.katas.application.model.Book;
import software.ulpgc.katas.application.ports.BookSerializer;

import java.util.List;

public class JsonBookSerializer implements BookSerializer {

    @Override
    public String serialize(List<Book> book) {
        return new Gson().toJson(book);
    }
}
