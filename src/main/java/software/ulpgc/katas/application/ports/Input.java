package software.ulpgc.katas.application.ports;

public interface Input {
    Parameter get(String parameter);
    interface Parameter {
        String asString();
        Integer asInteger();
    }
}
