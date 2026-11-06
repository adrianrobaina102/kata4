package software.ulpgc.katas.application.ports;

public interface Input {
    Parameter get(String param);
    interface Parameter{
        String asString();
    }
}
