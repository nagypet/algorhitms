package enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Action
{
    CONVERT("convert"),
    UPLOAD("upload"),
    ALL("all");

    private final String value;


    Action(String value)
    {
        this.value = value;
    }


    public static Optional<Action> fromValue(String value)
    {
        return Arrays.stream(Action.values()).filter(i -> i.value.equals(value)).findFirst();
    }
}