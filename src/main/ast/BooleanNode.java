package main.ast;

import main.Token;

public class BooleanNode extends ExprNode {

    public final Token number;

    public BooleanNode(Token number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return number.text;
    }
}
