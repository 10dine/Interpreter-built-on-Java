package Nodes;

public class BooleanCompareNode extends Node{
	private Node left;
	private Node right;
	private boolType comparator;
	
	public enum boolType{
		COMPARATOR_EQUAL,
		COMPARATOR_NOT_EQUAL,   //must be taken care of using Hashmap
		COMPARATOR_LESS,        //must be taken care of using Hashmap
		COMPARATOR_LESS_OR_EQUALS,//must be taken care of using Hashmap
		COMPARATOR_GREATER,     //must be taken care of using Hashmap
		COMPARATOR_GREATER_OR_EQUALS,//must be taken care of using Hashmap
	}
	
	public BooleanCompareNode(Node left, Node right, boolType comparator) {
		this.left = left;
		this.right = right;
		this.comparator = comparator;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public void setLeft(Node left) {
		this.left = left;
	}
	
	public Node getRight() {
		return right;
	}
	
	public void setRight(Node right) {
		this.right = right;
	}
	
	public boolType getComparator() {
		return comparator;
	}
	
	public void setType(boolType comparator) {
		this.comparator = comparator;
	}
	
	@Override
	public String toString() {
		return String.format("(%s %s %s)", left, comparator, right);
	}
}
