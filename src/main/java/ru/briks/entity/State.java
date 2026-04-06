package ru.briks.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum State {
    NEW("11"),
    USED("12");

    private final String code;

    private static final Map<String, State> codesMap = new HashMap<>();

    static {
        Arrays.stream(State.values())
                .forEach(state -> codesMap.put(state.code, state));
    }

    State(String code) {
        this.code = code;
    }

    public static State ofCode(String code) {
        if (codesMap.containsKey(code)) {
            return codesMap.get(code);
        }

        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
