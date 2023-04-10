package Nodes;

public class StringNode extends Node{
	
	String element;
	
	public String getElement() {
		return element;
	}
	
	public void setElement(String element) {
		this.element = element;
	}
	
	public StringNode(String value){
		this.element = value;
	}
	
	public StringNode(){}
	
	@Override
	public String toString() {
		return element;
	}
}
