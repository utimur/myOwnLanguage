package main.ast;

import main.Token;

public class AssingNode extends ExprNode{
    public final Token op;
    public final ExprNode left;
    public final ExprNode right;

    public AssingNode(Token op, ExprNode left, ExprNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + op.text + right.toString() + ")";
    }
}
