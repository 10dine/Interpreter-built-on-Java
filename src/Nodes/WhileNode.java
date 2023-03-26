package Nodes;

import java.util.ArrayList;

public class WhileNode extends StatementNode{
	
	private BooleanCompareNode condition;
	private ArrayList<StatementNode> StatementList = new ArrayList<StatementNode>();
	
	public WhileNode(){}
	
	public WhileNode(BooleanCompareNode condition, ArrayList<StatementNode> statementList) {
		this.condition = condition;
		StatementList = statementList;
	}
	
	public BooleanCompareNode getCondition() {
		return condition;
	}
	
	public void setCondition(BooleanCompareNode condition) {
		this.condition = condition;
	}
	
	public ArrayList<StatementNode> getStatementList() {
		return StatementList;
	}
	
	public void setStatementList(ArrayList<StatementNode> statementList) {
		StatementList = statementList;
	}
	
	@Override
	public String toString() {
		return "WhileNode{" +
				"condition=" + condition +
				", StatementList=" + StatementList +
				'}';
	}
}
