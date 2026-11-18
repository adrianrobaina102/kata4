package software.ulpgc.katas.application.domain;

import software.ulpgc.katas.application.model.Game;
import software.ulpgc.katas.application.model.Platform;
import software.ulpgc.katas.application.ports.GameSerializer;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;

public class FindGamesByPlatformUseCase implements UseCase {
    private final GameSerializer serializer;
    private final List<Game> GAMES;

    public FindGamesByPlatformUseCase(GameSerializer serializer, List<Game> GAMES) {
        this.serializer = serializer;
        this.GAMES = GAMES;
    }

    @Override
    public Output execute(Input input) {
        String raw = input.get("platform").asString();
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("El parámetro 'platform' es obligatorio");
        }
        Platform platform = Platform.valueOf(raw.toUpperCase());
        return () -> this.serializer.serialize(findGamesByPlatform(platform));
    }

    private List<Game> findGamesByPlatform(Platform platform) {
        return GAMES.stream().filter(game -> game.platform() == platform).toList();
    }
}
