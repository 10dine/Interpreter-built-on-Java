package Nodes;

public class RealNode extends Node{
	
	private Float element;
	
	public RealNode(){}
	
	public RealNode(Float element){
		this.element = element;
	}
	
	public float getElement(){
		return this.element;
	}
	
	public void negate(){ element = -1*element;}
	
	@Override
	public String toString() {
		return element.toString();
	}
}
