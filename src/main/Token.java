package main;

public class Token {

    public final TokenType type;
    public final String text;
    public final int pos;

    public Token(TokenType type, String text, int pos) {
        this.type = type;
        this.text = text;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return text;
    }
}
