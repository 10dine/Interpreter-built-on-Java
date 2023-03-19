package Nodes;

public class IntegerNode extends Node{
	
	private Integer element;
	
	public IntegerNode(){}
	
	public IntegerNode(Integer element){
		this.element = element;
	}
	
	private IntegerNode(int element) {
		this.element = element;
	}
	
	public void setElement(Integer element) {
		this.element = element;
	}
	
	public int getElement(){
		return this.element;
	}
	
	public void negate(){ element = -1*element;}
	
	@Override
	public String toString() {
		return element.toString();
	}
}
