package Nodes;

public class RealNode extends Node{
	
	private Float element;
	
	public RealNode(Float element){
		this.element = element;
	}
	
	private float getElement(){
		return this.element;
	}
	
	@Override
	public String toString() {
		return element.toString();
	}
}
