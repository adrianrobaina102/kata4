package software.ulpgc.katas.application.domain;

import software.ulpgc.katas.application.model.Game;
import software.ulpgc.katas.application.model.Platform;
import software.ulpgc.katas.application.ports.GameSerializer;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;

public class FindGamesByScoreUseCase implements UseCase{
    private final GameSerializer serializer;
    private final List<Game> GAMES;

    public FindGamesByScoreUseCase(GameSerializer serializer, List<Game> games) {
        this.serializer = serializer;
        this.GAMES = games;
    }


    @Override
    public Output execute(Input input) {
        Integer scoreParam = input.get("score").asInteger();
        int minScore = (scoreParam != null) ? scoreParam : 0;
        return () -> this.serializer.serialize(FindGamesByScore(minScore));
    }

    private List<Game> FindGamesByScore(int minScore) {
        return GAMES.stream().filter(game -> game.score() >= minScore).toList();
    }
}
