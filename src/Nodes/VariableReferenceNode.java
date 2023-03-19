package Nodes;

public class VariableReferenceNode extends Node{
	
	private String variableName;
	private Node index;
	
	public VariableReferenceNode(String variableName, Node index) {
		this.variableName = variableName;
		this.index = index;
	}
	
	public VariableReferenceNode(String variableName) {
		this.variableName = variableName;
	}
	
	public void setIndex(Node index) {
		this.index = index;
	}
	
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	public Node getIndex() {
		return index;
	}
	
	public String getVariableName() {
		return variableName;
	}
	
	@Override
	public String toString() {
		if (index != null){
			return String.format("{Referenced Variable Name: %s, Index: %s}", getVariableName(), getIndex());
		} else {
			return String.format("{Referenced Variable Name: %s}", getVariableName());
		}
	}
}
