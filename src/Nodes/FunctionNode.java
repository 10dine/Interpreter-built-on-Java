package Nodes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FunctionNode holds two lists of VariableNodes and one list of StatementNodes
 */
public class FunctionNode extends Node{
	private String Name;
	private ArrayList<VariableNode> parameterList = new ArrayList<VariableNode>();
	private ArrayList<VariableNode> VariableList = new ArrayList<VariableNode>();
	private ArrayList<StatementNode> StatementList = new ArrayList<StatementNode>();
	
	public FunctionNode(String name, ArrayList<VariableNode> parameterList, ArrayList<VariableNode> variableList, ArrayList<StatementNode> statementList) {
		this.Name = name;
		this.parameterList = parameterList;
		this.VariableList = variableList;
		this.StatementList = statementList;
	}
	
	public FunctionNode(String name, ArrayList<VariableNode> parameterList) {
		Name = name;
		this.parameterList = parameterList;
	}
	
	public FunctionNode() {
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		this.Name = name;
	}
	
	public ArrayList<VariableNode> getVariableList() {
		return this.VariableList;
	}
	
	public void setVariableList(ArrayList<VariableNode> variableList) {
		this.VariableList = variableList;
	}
	
	public ArrayList<StatementNode> getStatementList() {
		return this.StatementList;
	}
	
	public void setStatementList(ArrayList<StatementNode> statementList) {
		this.StatementList = statementList;
	}
	
	public ArrayList<VariableNode> getParameterList() {
		return this.parameterList;
	}
	
	public void setParameterList(ArrayList<VariableNode> parameterList) {
		this.parameterList = parameterList;
	}
	
	public boolean isVariadic(){
		return (this.Name.equals("Read") || this.Name.equals("Write"));
	}
	
	@Override
	public String toString() {
		return "\n\t\tFunctionNode{" +
				"\n\t\t\tName= '" + Name + '\'' +
				"\n\t\t\tparameterList= " + parameterList +
				"\n\t\t\tVariableList= " + VariableList +
				"\n\t\t\tStatementList= " + StatementList +
				"\n}";
	}
}
