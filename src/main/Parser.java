package main;

import main.ast.*;

import java.util.*;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;
    static Map<String, Boolean> scope = new TreeMap<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void error(String message) {
        if (pos < tokens.size()) {
            Token t = tokens.get(pos);
            throw new RuntimeException(message + " в позиции " + t.pos);
        } else {
            throw new RuntimeException(message + " в конце файла");
        }
    }

    private Token match(TokenType... expected) {
        if (pos < tokens.size()) {
            Token curr = tokens.get(pos);
            if (Arrays.asList(expected).contains(curr.type)) {
                pos++;
                return curr;
            }
        }
        return null;
    }

    private Token require(TokenType... expected) {
        Token t = match(expected);
        if (t == null)
            error("Ожидается " + Arrays.toString(expected));
        return t;
    }

    private ExprNode parseElem() {
        Token num = match(TokenType.TRUE, TokenType.FALSE);
        if (num != null)
            return new BooleanNode(num);
        Token id = match(TokenType.ID);
        if (id != null)
            return new VarNode(id);
        error("Ожидается число или переменная");
        return null;
    }

    public ExprNode parseAssign() {
        if (match(TokenType.ID) == null) {
            return null;
        }
        pos--;
        ExprNode e1 = parseElem();
        Token op;
        if ((op = match(TokenType.ASSIGN)) != null) {
            ExprNode e2 = parseElem();
            e1 = new BinOpNode(op, e1, e2);
            require(TokenType.SEMICOLON);
            return e1;
        }
        pos--;
        return null;
    }

    private ExprNode parseMnozh() {
        if (match(TokenType.LPAR) != null) {
            ExprNode e = parseExpression();
            require(TokenType.RPAR);
            return e;
        } else {
            return parseElem();
        }
    }

    public ExprNode parseUnaryOps() {
        Token t;
        while ((t = match(             // helps to recognize infix/postfix operator
                TokenType.ID,
                TokenType.PRINT,
                TokenType.NOT)) != null) {
            if (t.type == TokenType.TRUE || t.type == TokenType.FALSE || t.type == TokenType.ID) {  // may be infix
                pos--;
                ExprNode e = parseElem();
                if ((t = match(TokenType.NOT)) != null) {
                    e = new UnarOpNode(t, e);
                    require(TokenType.SEMICOLON);
                    return e;
                }
            } else {    // may be postfix
                ExprNode e = new UnarOpNode(t, parseElem());
                require(TokenType.SEMICOLON);
                return e;
            }

        }
        throw new IllegalStateException();
    }



    public ExprNode parseSlag() {
        ExprNode e1;

            e1 = parseMnozh();
            Token op;
        while ((op = match(TokenType.AND)) != null) {
            ExprNode e2 = parseMnozh();
            e1 = new BinOpNode(op, e1, e2);
        }
        return e1;
    }

    public ExprNode parseExpression() {
        ExprNode e1 = parseSlag();
        Token op;
        while ((op = match(TokenType.OR, TokenType.XOR)) != null) {
            ExprNode e2 = parseSlag();
            e1 = new BinOpNode(op, e1, e2);
        }
        return e1;
    }

    public static boolean eval(ExprNode node) {
        if (node instanceof BooleanNode) {
            BooleanNode num = (BooleanNode) node;
            return Boolean.parseBoolean(num.number.text);
        } else if (node instanceof BinOpNode) {
            BinOpNode binOp = (BinOpNode) node;
            boolean l = eval(binOp.left);
            boolean r = eval(binOp.right);
            switch (binOp.op.type) {
                case AND: return l && r;
                case OR: return l || r;
                case XOR: return l ^ r;
            }
        } else if( node instanceof UnarOpNode){
            switch (((UnarOpNode) node).operator.type) {
                case PRINT:
                    System.out.println(eval(((UnarOpNode) node).operand));
                    return false;
                case NOT:
                    return !Boolean.parseBoolean(((UnarOpNode) node).operator.text);
            }
        }else if (node instanceof VarNode) {
            VarNode var = (VarNode) node;
            if (scope.containsKey(var.id.text)) {
                return scope.get(var.id.text);
            } else {
                System.out.println("Введите значение " + var.id.text + ":");
                String line = new Scanner(System.in).nextLine();
                boolean value = Boolean.parseBoolean(line);
                scope.put(var.id.text, value);
            }
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        String text = "(true xor true) or true;";

        Lexer l = new Lexer(text);
        List<Token> tokens = l.lex();
        tokens.removeIf(t -> t.type == TokenType.SPACE);

        Parser p = new Parser(tokens);
        ExprNode node = p.parseExpression();

        boolean result = eval(node);
        System.out.println(result);
    }
}
