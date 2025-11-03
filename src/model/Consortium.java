package model;

public enum Consortium {
	S("sim"),
	N("nao");
	
	private String value;
	
	Consortium(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
}
