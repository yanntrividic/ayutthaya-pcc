package main.core;

/**
 * Classe permettant de stocker les informations relatives à une arête
 * @see Graphe
 * @see Sommet
 */
public class Arete {
	private int source;
	private int destination;
	private double poids;

	/**
	 * Constructeur d'une arête orientée et pondérée
	 * @param source L'identifiant unique du sommet duquel l'arête sort
	 * @param destination L'identifiant unique du sommet dans lequel l'arête entre
	 * @param poids de l'arête
	 */
	public Arete(int source, int destination, double poids) {
		this.source = source;
		this.destination = destination;
		this.poids = poids;
	}

	@Override
	public String toString() {
		return "(S="+source+", D="+destination+", W="+poids+")";
	}

	/**
	 * Getter du poids de l'arête
	 * @return poids de l'arête
	 */
	public double getPoids() {
		return poids;
	}

	/**
	 * Getter du sommet destination
	 * @return destination identifiant unique du sommet dans lequel l'arête entre
	 */
	public int getDestination() {
		return destination;
	}
}
