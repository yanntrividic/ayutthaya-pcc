package main.core.algorithmie;

public enum TypeAlgo {
	DIJSKTRA("Dijsktra"),
	ASTAR("A*");
	
	public String toString() {
		return this.s ;
	}
	
	private String s ;
	
	TypeAlgo(String s) {
		this.s = s ; 
	}
}
