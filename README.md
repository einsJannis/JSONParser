# This repository

This repository contains my first kinda released library and my first shot at parsing.
I'm not developing this any futher and this should not be used by anyone.
This simply serves the purpose of archiving my history.

# JSON parser

This is an Java API for loading/parsing JSON.

## How to use

Before parsing JSON you should check if it is an array or an object (this only refers to the outermost and any other gets detected automatically).

### Parsing a JSON object

Call the factory method `fromString(String string)` from class `JSONObject`. The `string` variable should be your JSON object as a String.
Done.

### Parsing a JSON array

Call the factory method `fromString(String string)` from class `JSONArray`. The `string` variable should be your JSON array as a String.
Done.

## Any questions or suggestions?

Contact me (links to everything are on my website).

## Thanks

Thanks go to Flexusma for helping me find some bugs and write some of the JavaDocs.
