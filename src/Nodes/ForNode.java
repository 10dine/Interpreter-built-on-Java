package Nodes;

import java.util.ArrayList;

public class ForNode extends StatementNode{
	
	VariableReferenceNode variable;
	Node from;
	Node to;
	ArrayList<StatementNode> StatementList = new ArrayList<StatementNode>();
	
	public ForNode(){}
	
	public ForNode(Node from, Node to, ArrayList<StatementNode> statementList) {
		this.from = from;
		this.to = to;
		StatementList = statementList;
	}
	
	public VariableReferenceNode getVariable() {
		return variable;
	}
	
	public void setVariable(VariableReferenceNode variable) {
		this.variable = variable;
	}
	
	public Node getFrom() {
		return from;
	}
	
	public void setFrom(Node from) {
		this.from = from;
	}
	
	public Node getTo() {
		return to;
	}
	
	public void setTo(Node to) {
		this.to = to;
	}
	
	public ArrayList<StatementNode> getStatementList() {
		return StatementList;
	}
	
	public void setStatementList(ArrayList<StatementNode> statementList) {
		StatementList = statementList;
	}
	
	@Override
	public String toString() {
		return "ForNode{" +
				"from=" + from +
				", to=" + to +
				", StatementList=" + StatementList +
				'}';
	}
}
