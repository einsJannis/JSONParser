package dev.einsjannis.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class JSONLexer {
    private final char[] chars;
    private int i = 0;

    public JSONLexer(char[] chars) {
        this.chars = chars;
    }

    public List<JSONPart> lex() throws JSONSyntaxError {
        List<JSONPart> parts = new ArrayList<JSONPart>();
        while ( i < chars.length ) {
            Character c = Character.valueOf(chars[i]);
            i++;
            JSONPart.Type type;
            String value = null;
            if ( c == '\n' || c == ' ' || c == '\t' ) {
                continue;
            } else if ( c == '{' ) {
                type = JSONPart.Type.OBJECT_START;
            } else if ( c == '}' ) {
                type = JSONPart.Type.OBJECT_END;
            } else if ( c == '[' ) {
                type = JSONPart.Type.ARRAY_START;
            } else if ( c == ']' ) {
                type = JSONPart.Type.ARRAY_END;
            } else if ( c == ',' ) {
                type = JSONPart.Type.COMMA;
            } else if ( c == ':' ) {
                type = JSONPart.Type.DOUBLE_POINT;
            } else if ( c == '"' || c == '\'' ) {
                type = JSONPart.Type.STRING;
                value = scanForString(c);
            } else if ( ".0123456789".contains(c.toString()) ) {
                type = JSONPart.Type.NUMBER;
                value = scanForNumber(c);
            } else if ( "tf".contains(c.toString()) ) {
                type = JSONPart.Type.BOOLEAN;
                value = scanForBoolean(c);
            } else if ( c == 'n' ) {
                type = JSONPart.Type.NULL;
                value = scanForNull();
            } else {
                throw new JSONSyntaxError(i-1, c);
            }
            parts.add(new JSONPart(type, value));
        }
        return parts;
    }

    private String scanForNumber(Character firstChar) throws JSONSyntaxError {
        StringBuilder builder = new StringBuilder();
        builder.append(firstChar);
        while ( i < chars.length ) {
            Character c = Character.valueOf(chars[i]);
            i++;
            if ( !( "0123456789".contains(c.toString()) || ( c == '.' && !builder.toString().contains(".") ) ) ) {
                i--;
                return builder.toString();
            }
            builder.append(c);
        }
        throw new JSONSyntaxError(i-1,chars[i-1]);
    }

    private String scanForNull() throws JSONSyntaxError {
        if ( chars[i] == 'u' && chars[i+1] == 'l' && chars[i+2] == 'l' ) {
            i+=3;
            return null;
        }
        throw new JSONSyntaxError(i-1,chars[i-1]);
    }

    private String scanForBoolean(char firstChar) throws JSONSyntaxError {
        if ( firstChar == 't' && chars[i] == 'r' && chars[i+1] == 'u' && chars[i+2] == 'e' ) {
            i += 3;
            return "true";
        }
        if ( firstChar == 'f' && chars[i] == 'a' && chars[i+1] == 'l' && chars[i+2] == 's' && chars[i+3] == 'e' ) {
            i += 4;
            return "false";
        }
        throw new JSONSyntaxError(i-1,chars[i-1]);
    }

    private String scanForString(char endChar) throws JSONSyntaxError {
        StringBuilder builder = new StringBuilder();
        while ( i < chars.length ) {
            Character c = Character.valueOf(chars[i]);
            i++;
            if (c == endChar) {
                return builder.toString();
            }
            builder.append(c);
        }
        throw new JSONSyntaxError(i-1,chars[i-1]);
    }
}
