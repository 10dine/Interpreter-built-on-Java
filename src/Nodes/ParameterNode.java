package Nodes;

public class ParameterNode extends Node{
	
	private VariableReferenceNode variable;
	private Node nonVariable;
	boolean isVar = false;
	
	public ParameterNode(VariableReferenceNode variable, boolean isVar) {
		this.variable = variable;
		this.isVar = isVar;
	}
	
	public ParameterNode(Node nonVariable) {
		this.nonVariable = nonVariable;
	}
	
	public VariableReferenceNode getVariable() {
		return variable;
	}
	
	public void setVariable(VariableReferenceNode variable) {
		this.variable = variable;
	}
	
	public Node getNonVariable() {
		return nonVariable;
	}
	
	public void setNonVariable(Node nonVariable) {
		this.nonVariable = nonVariable;
	}
	
	@Override
	public String toString() {
		return "ParameterNode{" +
				"variable=" + variable +
				", nonVariable=" + nonVariable +
				'}';
	}
}
