package software.ulpgc.katas.infrastructure.adapters;

import io.javalin.http.Context;
import software.ulpgc.katas.application.ports.Input;

public class JavalinContextInput implements Input {
    private final Context context;

    public JavalinContextInput(Context context) {
        this.context = context;
    }

    @Override
    public Parameter get(String parameter) {
        return new Parameter() {
            @Override
            public String asString() {
                try {
                    return context.queryParam(parameter);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Integer asInteger() {
                try {
                    String value = asString();
                    return  (value != null && !value.isBlank()) ? Integer.valueOf(value) : null;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
