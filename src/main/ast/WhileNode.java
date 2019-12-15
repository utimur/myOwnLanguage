package main.ast;

public class WhileNode extends ExprNode{
    public ExprNode condition;
    public ExprNode innerExpr;

    public WhileNode(ExprNode condition, ExprNode innerExpr) {
        this.condition = condition;
        this.innerExpr = innerExpr;
    }
}
