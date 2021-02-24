package main.core.algorithmie;

public enum TypeHeuristique {
	MANHATTAN("Manhattan"),
	EUCLIDIENNE("euclidienne"),
	FEU("Ayutthaya") ;
	
	public String toString() {
		return s ;
	}
	
	private String s ;
	
	TypeHeuristique(String s) {
		this.s = s ; 
	}
}
