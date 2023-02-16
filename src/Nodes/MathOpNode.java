package Nodes;

public class MathOpNode extends Node{
	
	enum operations{
		ADD,
		SUBTRACT,
		MULTIPLICATION,
		DIVISION,
		MOD,
	}
	
	public Node leftSide;
	public Node rightSide;
	
	@Override
	public String ToString() {
		return null;
	}
}
