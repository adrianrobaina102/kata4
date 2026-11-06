package software.ulpgc.katas.infrastructure;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import software.ulpgc.katas.application.domain.FindGamesByPlatformUseCase;
import software.ulpgc.katas.application.domain.FindGamesByScoreUseCase;
import software.ulpgc.katas.application.model.Game;
import software.ulpgc.katas.application.model.Platform;
import software.ulpgc.katas.application.ports.Output;
import software.ulpgc.katas.infrastructure.adapters.JavalinContextInput;
import software.ulpgc.katas.infrastructure.adapters.JsonGamesSerializer;

import java.util.List;

public class Main {
    static void main(String[] args) {
        Javalin.create(config -> init(config)).start(8080);
    }

    private static void init(JavalinConfig config) {
        config.routes.get("/games", ctx -> {
            if (ctx.queryParam("platform") != null) {
                ctx.result(getExecutePlatform(ctx).result());
            }
            else if (ctx.queryParam("score") != null) {
                ctx.result(getExecuteScore(ctx).result());
            }
            else {
                ctx.result(new JsonGamesSerializer().serialize(getGames()));
            }
        });
    }

    private static Output getExecutePlatform(Context context) {

        return new FindGamesByPlatformUseCase(new JsonGamesSerializer(), getGames()).execute(new JavalinContextInput(context));
    }
    private static Output getExecuteScore(Context context) {

        return new FindGamesByScoreUseCase(new JsonGamesSerializer(), getGames()).execute(new JavalinContextInput(context));
    }

    public static List<Game> getGames() {
        return List.of(
            new Game(1, "The Legend of Zelda: Breath of the Wild", Platform.NINTENDO, 2017, 97),
            new Game(2, "Super Mario Odyssey", Platform.NINTENDO, 2017, 97),
            new Game(3, "God of War", Platform.PLAYSTATION, 2018, 94),
            new Game(4, "The Last of Us Part I", Platform.PLAYSTATION, 2013, 95),
            new Game(5, "Bloodborne", Platform.PLAYSTATION, 2015, 92),
            new Game(6, "Halo Infinite", Platform.XBOX, 2021, 87),
            new Game(7, "Forza Horizon 5", Platform.XBOX, 2021, 92),
            new Game(8, "Gears 5", Platform.XBOX, 2019, 84),
            new Game(9, "Half-Life: Alyx", Platform.PC, 2020, 93),
            new Game(10, "Baldur's Gate 3", Platform.PC, 2023, 96),
            new Game(11, "Cyberpunk 2077", Platform.PC, 2020, 86)
        );
    }
}
