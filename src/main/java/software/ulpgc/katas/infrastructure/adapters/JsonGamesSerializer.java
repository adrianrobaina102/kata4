package software.ulpgc.katas.infrastructure.adapters;

import com.google.gson.Gson;
import software.ulpgc.katas.application.model.Game;
import software.ulpgc.katas.application.ports.GameSerializer;

import java.util.List;

public class JsonGamesSerializer implements GameSerializer {
    @Override
    public String serialize(List<Game> game) {
        return new Gson().toJson(game);
    }
}
