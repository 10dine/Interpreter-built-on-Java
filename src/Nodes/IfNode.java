package Nodes;

import java.util.ArrayList;

public class IfNode extends StatementNode{
	
	private BooleanCompareNode condition;
	private ArrayList<StatementNode> StatementList = new ArrayList<StatementNode>();
	private IfNode nextIf;
	
	public IfNode(){}
	
	public IfNode(BooleanCompareNode condition, ArrayList<StatementNode> statementList, IfNode nextIf) {
		this.condition = condition;
		StatementList = statementList;
		this.nextIf = nextIf;
	}
	
	public Node getCondition() {
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
	
	public Node getNextIf() {
		return nextIf;
	}
	
	public void setNextIf(IfNode nextIf) {
		this.nextIf = nextIf;
	}
	
	@Override
	public String toString() {
		return "IfNode{" +
				"condition=" + condition +
				", StatementList=" + StatementList +
				"\n\t, nextIf ->" + nextIf +
				'}';
	}
}
