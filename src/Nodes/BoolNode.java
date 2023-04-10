package Nodes;

public class BoolNode extends Node{
	Boolean state;
	public BoolNode(){}
	public BoolNode(Boolean state){this.state = state;}
	
	public void setState(Boolean state) {
		this.state = state;
	}
	
	public Boolean getState() {
		return state;
	}
	
	
	@Override
	public String toString() {
		return state.toString();
	}
}
