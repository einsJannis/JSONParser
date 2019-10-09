package dev.einsjannis.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class JSONArray extends ArrayList<Object> implements ClassWithIterator {

    private int i;

    @Override
    public int getIterator() {
        return i;
    }
    @Override
    public void setIterator(int i) {
        this.i = i;
    }

    public JSONArray(List<JSONPart> parts) throws JSONSyntaxError {
        loadContents(parts);
    }

    private void loadContents(List<JSONPart> parts) throws JSONSyntaxError {
        if (parts.get(0).getType() != JSONPart.Type.ARRAY_START) {
            throw new JSONSyntaxError(parts.get(0));
        }
        if (parts.get(1).getType() == JSONPart.Type.ARRAY_END) {
            return;
        }
        i = 1;
        while (i < parts.size()) {
            switch (parts.get(i).getType()) {
                case ARRAY_START:
                    this.add(JSONArray.scanForSelf(this, parts));
                    break;
                case OBJECT_START:
                    this.add(JSONObject.scanForSelf(this, parts));
                    break;
                case BOOLEAN:
                    this.add(parts.get(i).getValue().equals("true"));
                    break;
                case STRING:
                    this.add(parts.get(i).getValue());
                    break;
                case NUMBER:
                    this.add(Long.valueOf(parts.get(i).getValue()));
                    break;
                case NULL:
                    this.add(null);
                    break;
                default:
                    throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            if (parts.get(i).getType() == JSONPart.Type.ARRAY_END) {
                return;
            } else if (parts.get(i).getType() == JSONPart.Type.COMMA) {
                i++;
                continue;
            }
            throw new JSONSyntaxError(parts.get(i));
        }
        throw new JSONSyntaxError(parts.get(0));
    }

    public static JSONArray scanForSelf(ClassWithIterator parent, List<JSONPart> parts) throws JSONSyntaxError {
        List<JSONPart> partList = parts.subList(parent.getIterator(), parts.size());
        int openObjects = 0;
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getType() == JSONPart.Type.ARRAY_START) {
                openObjects++;
            }
            if (partList.get(i).getType() == JSONPart.Type.ARRAY_END) {
                if (openObjects == 1) {
                    parent.setIterator(parent.getIterator() + i);
                    return new JSONArray(partList.subList(0, i + 1));
                } else {
                    openObjects--;
                }
            }
        }
        throw new JSONSyntaxError(parts.get(0));
    }

    public static JSONArray fromString(String string) throws JSONSyntaxError {
        return fromChars(string.toCharArray());
    }
    public static JSONArray fromChars(char[] chars) throws JSONSyntaxError {
        return new JSONArray(new JSONLexer(chars).lex());
    }
}
