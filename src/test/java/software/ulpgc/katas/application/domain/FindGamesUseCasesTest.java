package software.ulpgc.katas.application.domain;

import org.junit.jupiter.api.Test;
import software.ulpgc.katas.application.model.Game;
import software.ulpgc.katas.application.model.Platform;
import software.ulpgc.katas.application.ports.GameSerializer;
import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FindGamesUseCasesTest {

    private final List<Game> sampleGames = List.of(
            new Game(1, "The Legend of Zelda: Breath of the Wild", Platform.NINTENDO, 2017, 97),
            new Game(2, "God of War", Platform.PLAYSTATION, 2018, 94),
            new Game(3, "Halo Infinite", Platform.XBOX, 2021, 87)
    );

    private final GameSerializer plainSerializer = games ->
            games.stream().map(Game::title).collect(Collectors.joining(","));

    private Input stringInput(String value) {
        return param -> new Input.Parameter() {
            @Override
            public String asString() {
                return value;
            }

            @Override
            public Integer asInteger() {
                return null;
            }
        };
    }

    private Input integerInput(Integer value) {
        return param -> new Input.Parameter() {
            @Override
            public String asString() {
                return value != null ? value.toString() : null;
            }

            @Override
            public Integer asInteger() {
                return value;
            }
        };
    }

    @Test
    void shouldFilterGamesByValidPlatformIgnoringCase() {
        FindGamesByPlatformUseCase useCase = new FindGamesByPlatformUseCase(plainSerializer, sampleGames);

        Output output = useCase.execute(stringInput("nintendo"));

        assertEquals("The Legend of Zelda: Breath of the Wild", output.result());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidOrMissingPlatform() {
        FindGamesByPlatformUseCase useCase = new FindGamesByPlatformUseCase(plainSerializer, sampleGames);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(stringInput("sega")));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(stringInput(null)));
    }

    @Test
    void shouldFilterGamesByMinimumScore() {
        FindGamesByScoreUseCase useCase = new FindGamesByScoreUseCase(plainSerializer, sampleGames);

        Output output = useCase.execute(integerInput(94));

        assertEquals("The Legend of Zelda: Breath of the Wild,God of War", output.result());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenScoreIsOutOfRange() {
        FindGamesByScoreUseCase useCase = new FindGamesByScoreUseCase(plainSerializer, sampleGames);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(integerInput(-1)));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(integerInput(101)));
    }
}
