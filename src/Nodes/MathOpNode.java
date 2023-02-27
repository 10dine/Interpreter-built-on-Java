package Nodes;

public class MathOpNode extends Node{
	
	public enum operations{
		ADD,
		SUBTRACT,
		MULTIPLICATION,
		DIVISION,
		MOD,
	}
	
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
	public String ToString() {
		return null;
	}
}
