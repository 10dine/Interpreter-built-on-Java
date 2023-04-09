package Nodes;

public class VariableNode extends Node {
	
	public enum type{
		integar,
		real,
		bool,
		character,
		string,
		array,
		arrayInt,
		arrayStr,
		arrayFlt,
		abyss
	}
	
	private String Name;
	private type type;
	private Node value;
	private boolean cSate = false;//this constant
	
	private Node min;
	private Node max;
	private Node arraySize;
	
	public VariableNode(String name, Node value, boolean cSate) throws NodeErrorException {
		this.Name = name;
		this.value = value;
		
		switch (value.getClass().getName()){
			case "Nodes.IntegerNode" -> this.type = type.integar;
			case "Nodes.RealNode" -> this.type = type.real;
			case "Nodes.StringNode" -> this.type = type.string;
			case "Nodes.CharNode" -> this.type = type.character;
			case "Nodes.BoolNode" -> this.type = type.bool;
			//case ARRAY -> tempConstantNode = new StringNode();
			default -> throw new NodeErrorException("Error in variable creation!");
		}
		
		this.cSate = cSate;
	}
	
	public VariableNode(String Name, type type, boolean x, Node min, Node max){
		this.Name = Name;
		this.type = type;
		this.cSate = x;
		this.min = min;
		this.max = max;
	}
	
	public VariableNode(String Name, type type, boolean x, Node arraySize){
		this.Name = Name;
		this.type = type;
		this.cSate = x;
		this.arraySize = arraySize;
	}
	
	public VariableNode(String Name, type type, boolean cState){
		this.Name = Name;
		this.type = type;
		this.cSate = cState;
	}

	public VariableNode(String Name, type type){
		this.Name = Name;
		this.type = type;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public VariableNode.type getType() {
		return type;
	}
	
	public void setType(VariableNode.type type) {
		this.type = type;
	}
	
	public Node getValue() {
		return value;
	}
	
	public void setValue(Node value) {
		this.value = value;
	}
	
	public boolean iscSate() {
		return cSate;
	}
	
	public void setcSate(boolean cSate) {
		this.cSate = cSate;
	}
	
	public Node getMin() {
		return min;
	}
	
	public void setMin(Node min) {
		this.min = min;
	}
	
	public Node getMax() {
		return max;
	}
	
	public void setMax(Node max) {
		this.max = max;
	}
	
	public Node getArraySize() {
		return arraySize;
	}
	
	public void setArraySize(Node arraySize) {
		this.arraySize = arraySize;
	}
	
	@Override
	public String toString() {
		if (value == null){
			return String.format(
					"[Variable Nodes.Node - Name: %s, Type: %s, Constant?: %b, Min: %s, Max: %s]",
					this.Name, this.type, this.cSate, this.min, this.max
			);
			
		} else {
			switch(type) {
				case integar -> {
					return String.format(
							"[Variable Nodes.Node - Name: %s, Type: %s, value: %d, Constant?: %b, Min: %s, Max: %s]",
							this.Name, this.type, ((IntegerNode) this.value).getElement(), this.cSate, this.min, this.max
					);
				}
				case real -> {
					return String.format(
							"[Variable Nodes.Node - Name: %s, Type: %s, value: %f, Constant?: %b, Min: %s, Max: %s]",
							this.Name, this.type, ((RealNode) this.value).getElement(), this.cSate, this.min, this.max
					);
				}
				case bool -> {
					return String.format(
							"[Variable Nodes.Node - Name: %s, Type: %s, value: %b, Constant?: %b]",
							this.Name, this.type, ((BoolNode) this.value).getState(), this.cSate
					);
				}
				case character -> {
					return String.format(
							"[Variable Nodes.Node - Name: %s, Type: %s, value: %c, Constant?: %b]",
							this.Name, this.type, ((CharNode) this.value).getElement(), this.cSate
					);
				}
				case string -> {
					return String.format(
							"[Variable Nodes.Node - Name: %s, Type: %s, value: %s, Constant?: %b, Min: %s, Max: %s]",
							this.Name, this.type, ((StringNode) this.value).getElement(), this.cSate, this.min, this.max
					);
				}
				default -> {
					return String.format(
							"[Variable Nodes.Node - Name: %s, Type: %s, Constant?: %b]",
							this.Name, this.type, this.cSate
					);
				}
			}
		}
		
	}
}
