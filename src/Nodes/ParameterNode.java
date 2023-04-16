package Nodes;

public class ParameterNode extends Node{
	
	private VariableReferenceNode variable;
	private Node nonVariable;
	boolean isVar = false;
	
	public ParameterNode(VariableReferenceNode variable, boolean isVar) {
		this.variable = variable;
		this.isVar = isVar;
	}
	
	public Node getVar(){
		if (variable != null){
			return variable;
		} else if (nonVariable != null){
			return nonVariable;
		}
		return null;
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
	
	public boolean isVar() {
		return isVar;
	}
	
	public void setVar(boolean var) {
		isVar = var;
	}
	
	@Override
	public String toString() {
		return "ParameterNode{" +
				"variable=" + variable +
				", nonVariable=" + nonVariable +
				'}';
	}
}
