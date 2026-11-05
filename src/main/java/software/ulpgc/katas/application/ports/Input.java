package software.ulpgc.katas.application.ports;

import java.lang.reflect.Parameter;

public interface Input {
    Parameter get(String genre);
    interface Parameter {
        String asString();
    }
}
