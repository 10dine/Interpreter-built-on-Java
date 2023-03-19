package Nodes;

public class AssignmentNode extends StatementNode{
	
	private VariableReferenceNode targetVariableName;
	private Node value;
	
	public AssignmentNode(VariableReferenceNode targetVariableName, Node value) {
		this.targetVariableName = targetVariableName;
		this.value = value;
	}
	
	public VariableReferenceNode getTargetVariableName() {
		return targetVariableName;
	}
	
	public void setTargetVariableName(VariableReferenceNode targetVariableName) {
		this.targetVariableName = targetVariableName;
	}
	
	public Node getValue() {
		return value;
	}
	
	public void setValue(Node value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "AssignmentNode{ " +
				"targetVariableName = " + targetVariableName +
				"| value = " + value +
				" }";
	}
}
