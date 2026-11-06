package software.ulpgc.katas.application.ports;

public interface Input {
    Parameter get(String score);
    interface Parameter {
        String asString();
        Integer asInteger();
    }
}
