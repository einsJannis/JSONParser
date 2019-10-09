package dev.einsjannis.jsonparser;

import java.util.HashMap;
import java.util.List;

public class JSONObject extends HashMap<String, Object> implements ClassWithIterator {

    private int i;

    @Override
    public int getIterator() {
        return i;
    }
    @Override
    public void setIterator(int i) {
        this.i = i;
    }

    public JSONObject(List<JSONPart> parts) throws JSONSyntaxError {
        loadContents(parts);
    }

    private void loadContents(List<JSONPart> parts) throws JSONSyntaxError {
        if (parts.get(0).getType() != JSONPart.Type.OBJECT_START) {
            throw new JSONSyntaxError(parts.get(0));
        }
        i = 1;
        while (i < parts.size()) {
            if (parts.get(i).getType() != JSONPart.Type.STRING) {
                throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            if (parts.get(i).getType() != JSONPart.Type.DOUBLE_POINT) {
                throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            switch (parts.get(i).getType()) {
                case ARRAY_START:
                    this.put(parts.get(i-2).getValue(), JSONArray.scanForSelf(this, parts));
                    break;
                case OBJECT_START:
                    this.put(parts.get(i-2).getValue(), JSONObject.scanForSelf(this, parts));
                    break;
                case BOOLEAN:
                    this.put(parts.get(i-2).getValue(), parts.get(i).getValue().equals("true"));
                    break;
                case STRING:
                    this.put(parts.get(i-2).getValue(), parts.get(i).getValue());
                    break;
                case NUMBER:
                    this.put(parts.get(i-2).getValue(), Double.parseDouble(parts.get(i).getValue()));
                    break;
                case NULL:
                    this.put(parts.get(i-2).getValue(), null);
                    break;
                default:
                    throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            if (parts.get(i).getType() == JSONPart.Type.OBJECT_END) {
                return;
            } else if (parts.get(i).getType() == JSONPart.Type.COMMA) {
                i++;
                continue;
            }
            throw new JSONSyntaxError(parts.get(i));
        }
    }

    public static JSONObject scanForSelf(ClassWithIterator parent, List<JSONPart> parts) throws JSONSyntaxError {
        List<JSONPart> partList = parts.subList(parent.getIterator(), parts.size());
        int openObjects = 0;
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getType() == JSONPart.Type.OBJECT_START) {
                openObjects++;
            }
            if (partList.get(i).getType() == JSONPart.Type.OBJECT_END) {
                if (openObjects == 1) {
                    parent.setIterator(parent.getIterator() + i);
                    return new JSONObject(partList.subList(0,i+1));
                } else {
                    openObjects--;
                }
            }
        }
        throw new JSONSyntaxError(parts.get(0));
    }

    public static JSONObject fromString(String string) throws JSONSyntaxError {
        return fromChars(string.toCharArray());
    }
    public static JSONObject fromChars(char[] chars) throws JSONSyntaxError {
        return new JSONObject(new JSONLexer(chars).lex());
    }
}
