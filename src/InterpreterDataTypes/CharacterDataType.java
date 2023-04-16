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
	
	public void setData(Character data) throws Exception {
		if(cState) {
			throw new Exception("[This Variable is constant!]");
		} else {
			this.data = data;
		}
	}
	
	@Override
	public InterpreterDataType clone() {
		return new CharacterDataType(this.data, this.cState);
	}
	
	@Override
	public String toString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) throws Exception {
		if (input.length() == 1)
			if(cState) {
				throw new Exception("[This Variable is constant!]");
			} else {
				try{
					this.data = input.charAt(0);
				} catch (Exception e) {
					throw new Exception(String.format("[This IDT(%s) does not accept: (%s) as valid input -> Error: (%s)]", this.getClass().getName(), input, e));
				}
			}
			
		else
			System.out.println("Invalid Character Input");
	}
}
