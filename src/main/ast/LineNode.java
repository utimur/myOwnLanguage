package main.ast;

import java.util.ArrayList;
import java.util.List;

public class LineNode extends ExprNode {
    List<ExprNode> pipeline;

    public LineNode() {
        this.pipeline = new ArrayList<>();
    }

    public void addNode(ExprNode node) {
        this.pipeline.add(node);
    }

    public List<ExprNode> getPipeline() {
        return pipeline;
    }
}
