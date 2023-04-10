package Nodes;

public class CharNode extends Node{
	
	char element;
	
	public CharNode(char c){
		this.element = c;
	}
	
	public void setElement(char element) {
		this.element = element;
	}
	
	public char getElement() {
		return element;
	}
	
	@Override
	public String toString() {
		return ""+element;
	}
}
