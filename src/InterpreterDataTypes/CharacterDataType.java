package InterpreterDataTypes;

public class CharacterDataType extends InterpreterDataType{
	private Character data;
	
	public CharacterDataType(Character data) {
		this.data = data;
	}
	
	public Character getData() {
		return data;
	}
	
	public void setData(Character data) {
		this.data = data;
	}
	
	@Override
	public String ToString() {
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
