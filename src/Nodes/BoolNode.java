package Nodes;

public class BoolNode extends Node{
	Boolean state;
	BoolNode(){}
	
	public void setState(Boolean state) {
		this.state = state;
	}
	
	public Boolean getState() {
		return state;
	}
	
	
	@Override
	public String toString() {
		return null;
	}
}
