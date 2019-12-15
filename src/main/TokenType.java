package main;

import java.util.regex.Pattern;

public enum TokenType {
    TRUE("true"),
    FALSE("false"),
    SPACE("[ \t\r\n]+"),
    PRINT("print"),
    SEMICOLON(";"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    NOT("not"),
    LPAR("\\("),
    RPAR("\\)"),
    ID("[a-zA-Z_][a-zA-Z_0-9]*"),

    ASSIGN(":=");

    final Pattern pattern;

    TokenType(String regexp) {
        pattern = Pattern.compile(regexp);
    }
}
