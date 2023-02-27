package Nodes;

public class IntegerNode extends Node{
	
	private Integer element;
	
	public IntegerNode(Integer element){
		this.element = element;
	}
	
	private int getElement(){
		return this.element;
	}
	
	@Override
	public String ToString() {
		return null;
	}
}
