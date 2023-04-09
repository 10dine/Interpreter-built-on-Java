package Nodes;

import java.util.ArrayList;

public class FunctionCallNode extends StatementNode{
	
	private String Name;
	private ArrayList<ParameterNode> ParametertList = new ArrayList<ParameterNode>();
	
	public FunctionCallNode(){}
	
	public FunctionCallNode(String name, ArrayList<ParameterNode> ParametertList) {
		Name = name;
		this.ParametertList = ParametertList;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		this.Name = name;
	}
	
	public ArrayList<ParameterNode> getParametertList() {
		return ParametertList;
	}
	
	public void setStatementList(ArrayList<ParameterNode> ParametertList) {
		this.ParametertList = ParametertList;
	}
	
	@Override
	public String toString() {
		return "FunctionCallNode{" +
				"Name='" + Name + '\'' +
				", StatementList=" + ParametertList +
				'}';
	}
}
