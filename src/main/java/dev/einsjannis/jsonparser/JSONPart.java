package dev.einsjannis.jsonparser;

public class JSONPart {

    private final JSONPart.Type type;
    private final String value;

    public JSONPart(JSONPart.Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(\"" + type.name() + "\", \"" + value + "\")";
    }

    public enum Type {
        OBJECT_START, OBJECT_END, ARRAY_START, ARRAY_END, COMMA, DOUBLE_POINT, STRING, NUMBER, BOOLEAN, NULL
    }
}
