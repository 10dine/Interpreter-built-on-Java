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
	private boolean cSate = false;
	
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
	
	public VariableNode(String Name, type type, boolean x){
		this.Name = Name;
		this.type = type;
		this.cSate = x;
	}

	public VariableNode(String Name, type type){
		this.Name = Name;
		this.type = type;
	}
	
	@Override
	public String toString() {
		if (value == null){
			return String.format(
				"[Variable Node - Name: %s, Type: %s, Constant?: %b]",
				this.Name, this.type, this.cSate
			);
			
		} else {
			switch(type) {
				case integar -> {
					return String.format(
							"[Variable Node - Name: %s, Type: %s, value: %d, Constant?: %b]",
							this.Name, this.type, ((IntegerNode) this.value).getElement(), this.cSate
					);
				}
				case real -> {
					return String.format(
							"[Variable Node - Name: %s, Type: %s, value: %f, Constant?: %b]",
						this.Name, this.type, ((RealNode) this.value).getElement(), this.cSate
					);
				}
				case bool -> {
					return String.format(
							"[Variable Node - Name: %s, Type: %s, value: %b, Constant?: %b]",
							this.Name, this.type, ((BoolNode) this.value).getState(), this.cSate
					);
				}
				case character -> {
					return String.format(
							"[Variable Node - Name: %s, Type: %s, value: %c, Constant?: %b]",
							this.Name, this.type, ((CharNode) this.value).getElement(), this.cSate
					);
				}
				case string -> {
					return String.format(
							"[Variable Node - Name: %s, Type: %s, value: %s, Constant?: %b]",
							this.Name, this.type, ((StringNode) this.value).getElement(), this.cSate
					);
				}
				default -> {
					return String.format(
							"[Variable Node - Name: %s, Type: %s, Constant?: %b]",
							this.Name, this.type, this.cSate
					);
				}
			}
		}
		
	}
}
