package dev.einsjannis.jsonparser;

import dev.einsjannis.jsonparser.JSONPart;

public class JSONSyntaxError extends Exception {

    private char errorChar;
    private int errorPosition;
    private JSONPart errorPart;

    public JSONSyntaxError(int errorPosition, char errorChar) {
        this.errorChar = errorChar;
        this.errorPosition = errorPosition;
    }
    public JSONSyntaxError(JSONPart errorPart) {
        this.errorPart = errorPart;
    }

    @Override
    public String getMessage() {
        if (errorPart != null) {
            return "Error in json at part: " + errorPart.toString();
        } else {
            return "Error in json at position " + String.valueOf(errorPosition) + " with char: '" + String.valueOf(errorChar) + "'.";
        }
    }
}
