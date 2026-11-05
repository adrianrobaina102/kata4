package software.ulpgc.katas.application.domain;


import software.ulpgc.katas.application.ports.Input;
import software.ulpgc.katas.application.ports.Output;

public interface UseCase {
    Output execute(Input input);
}
