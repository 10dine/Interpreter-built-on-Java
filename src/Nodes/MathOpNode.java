package Nodes;

import java.util.HashMap;

public class MathOpNode extends Node{
	
	public enum operations{
		ADD,
		SUBTRACT,
		MULTIPLICATION,
		DIVISION,
		MOD,
	}
	
	HashMap<operations, String> operationsString = new HashMap<operations, String>() {{
		put(operations.ADD, "+");
		put(operations.SUBTRACT, "-");
		put(operations.MULTIPLICATION, "*");
		put(operations.DIVISION, "/");
	}};
	
	private operations operator;
	private Node leftSide;
	private Node rightSide;
	
	public MathOpNode(operations operator, Node leftSide, Node rightSide){
		this.operator=operator;
		this.leftSide=leftSide;
		this.rightSide=rightSide;
	}
	
	private operations getOperator() {
		return operator;
	}
	
	public Node getLeftSide() {
		return leftSide;
	}
	
	public Node getRightSide() {
		return rightSide;
	}
	
	@Override
	public String toString() {
		return "M("+leftSide.toString()+operationsString.get(operator)+rightSide.toString()+ ")";
	}
}
