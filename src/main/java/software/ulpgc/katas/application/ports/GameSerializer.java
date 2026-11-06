package software.ulpgc.katas.application.ports;

import software.ulpgc.katas.application.model.Game;

import java.util.List;

public interface GameSerializer {
    String serialize(List<Game> game);
}
