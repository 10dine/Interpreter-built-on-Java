package InterpreterDataTypes;

public class CharacterDataType extends InterpreterDataType{
	
	private Character data;
	
	public CharacterDataType(Character data, boolean cState) {
		this.data = data;
		this.cState = cState;
	}
	
	public CharacterDataType(Character data) {
		this.data = data;
	}
	
	public CharacterDataType(boolean cState){
		this.cState = cState;
	}
	
	public CharacterDataType() {}
	
	public Character getData() {
		return data;
	}
	
	public void setData(Character data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {
		if (input.length() == 1)
			data = input.charAt(0);
		else
			System.out.println("Invalid Character Input");
	}
}
