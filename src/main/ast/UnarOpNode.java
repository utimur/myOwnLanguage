package main.ast;


import main.Token;

public class UnarOpNode extends ExprNode {
    public final Token operator;
    public final ExprNode operand;

    public UnarOpNode(Token operator, ExprNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return "(" + operator.text + operand.toString() + ")";
    }
}
