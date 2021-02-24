package main.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Une des classes principales du projet. Structure de données permettant de représenter les différents problèmes donnés.
 * @see Sommet
 * @see Arete
 */
public class Graphe {
	
	private ArrayList<Sommet> listeSommets ;
	private int numSommets ;
	private int nbColonnes ;
	private int nbLignes ;
	
	HashMap<String, Double> typesDeSol ; // Permet de garder une trace du type de sol associé au sommet
	HashMap<Double, String> couleursDeSol ; // sert à l'affichage

	private int numSource ; // Position des sommets particuliers du projet Ayutthaya
	private int numArrivee ; 
	private int numSourceFeu ;

	/**
	 * Constructeur par défaut d'un graphe, ne fait qu'initialiser les différents attributs nécessaires au bon fonctionnement du programme
	 * @see Sommet
	 */
	public Graphe() {
		this.listeSommets = new ArrayList<Sommet>();
		this.numSommets = 0 ; // correspond au nombre de sommets, permet à la fois de donner une clé unique à chaque sommet et de compter le nombre de sommets
		this.nbColonnes = -1 ; // les attributs initialisés à -1 permettent de déterminer s'ils ont déjà vu une valeur leur être attribuée ou non
		this.nbLignes = -1 ;
		this.numSource = -1 ;
		this.numArrivee = -1 ;
		this.numSourceFeu = -1 ;
		this.typesDeSol = new HashMap<String, Double>();
		this.couleursDeSol = new HashMap<Double, String>();
	}
	
	/**
	 * Méthode ajoutant un sommet à un graphe. Chaque sommet ajouté a un identifiant unique nomSommets, on attribue un poids au sommet
	 * @param tempsSommet poids attribué au sommet
	 * @see calculerPoidsArete(int, int, boolean)
	 */
	public void addSommet(double tempsSommet) {
		Sommet v = new Sommet(numSommets);
		v.setTempsSommet(tempsSommet) ;
		listeSommets.add(v);
		numSommets = numSommets + 1 ; // permet d'avoir un numSommets unique pour chaque sommet et de garder le compte du nombre de sommets du graphe
	}
	
	/**
	 * Méthode ajoutant une arête orientée et pondérée entre un sommet source et un sommet destination
	 * @param source L'identifiant unique du sommet duquel l'arête sort
	 * @param destination L'identifiant unique du sommet dans lequel l'arête entre
	 * @param poids poids de l'arête
	 */
	public void ajouterArete(int source, int destination, double poids) {
		Arete arete = new Arete(source, destination, poids);
		listeSommets.get(source).addVoisin(arete);
	}

	/**
	 * Méthode permettant d'ajouter ou de mettre à jour les arêtes d'un sommet selon le type de problème posé
	 * @param s Le sommet dans lequel on cherche à ajouter des arêtes
	 * @param ouAjouterAretes Quatre booléens permettant de savoir si le sommet est situé au bord du graphe ou non
	 * @param diagonale Dans le cas où le problème implique de prendre en compte cases diagonales, mettre la valeur à true
	 * @return la liste d'arêtes à associer au sommet s
	 */
	private LinkedList<Arete> ajouterJusqueHuitAretes(int s, HashMap<String, Boolean> ouAjouterAretes, boolean diagonale) {
		
		LinkedList<Arete> listeAdjacence = new LinkedList<Arete>() ; // on initialise une liste vide
		
		if(ouAjouterAretes.get("gauche")) listeAdjacence.add(new Arete(s, s-1, calculerPoidsArete(s, s-1, false))) ; // à gauche si on est pas sur le bord gauche
		if(ouAjouterAretes.get("droite")) listeAdjacence.add(new Arete(s, s+1, calculerPoidsArete(s, s+1, false))) ; // à droite si on est pas sur le bord droit, etc.
		if(ouAjouterAretes.get("haut")) listeAdjacence.add(new Arete(s, s-this.nbColonnes, calculerPoidsArete(s, s-this.nbColonnes, false))) ;
		if(ouAjouterAretes.get("bas")) listeAdjacence.add(new Arete(s, s+this.nbColonnes, calculerPoidsArete(s, s+this.nbColonnes, false))) ;
		
		if(diagonale) { // dans le cas où nombre problème implique d'utiliser les diagonales
			if(ouAjouterAretes.get("haut") && ouAjouterAretes.get("gauche"))
				listeAdjacence.add(new Arete(s, s-this.nbColonnes-1, calculerPoidsArete(s, s-this.nbColonnes-1, true))) ;
			if(ouAjouterAretes.get("haut") && ouAjouterAretes.get("droite"))
				listeAdjacence.add(new Arete(s, s-this.nbColonnes+1, calculerPoidsArete(s, s-this.nbColonnes+1, true))) ;
			if(ouAjouterAretes.get("bas") && ouAjouterAretes.get("gauche"))
				listeAdjacence.add(new Arete(s, s+this.nbColonnes-1, calculerPoidsArete(s, s+this.nbColonnes-1, true))) ;
			if(ouAjouterAretes.get("bas") && ouAjouterAretes.get("droite")) 
				listeAdjacence.add(new Arete(s, s+this.nbColonnes+1, calculerPoidsArete(s, s+this.nbColonnes+1, true))) ;
		}
		return listeAdjacence ;
	}

	/**
	 * Méthode permettant d'itérer à travers tous les sommets du graphe et de leur ajouter des arêtes / mettre à jour la valeur de leurs arêtes
	 * @param diagonale Dans le cas où le problème implique de prendre en compte cases diagonales, mettre la valeur à true
	 */
	public void parcoursSommetsGrapheRectangulaire(boolean diagonale) {
		for (int ligne=0; ligne < this.nbLignes; ligne++) {	// pour chaque ligne...
			for (int col=0; col < this.nbColonnes; col++) {	// ... et pour chaque colonne 
				int source = ligne*this.nbColonnes+col;		
				HashMap<String, Boolean> ouAjouterAretes = this.listeSommets.get(source).ouAjouterAretes(this) ;			// on ajoute 4 arêtes si le graphe ne prend pas en compte les diagonales
				listeSommets.get(source).setListeAdjacence(ajouterJusqueHuitAretes(source, ouAjouterAretes, diagonale)) ; 	// ou sinon huit arêtes
			}
		}
	}

	/**
	 * Méthode calculant le poids d'une arête. 
	 * @param source L'identifiant unique du sommet duquel l'arête sort
	 * @param destination L'identifiant unique du sommet dans lequel l'arête entre
	 * @param diagonale Dans le cas où le problème implique de prendre en compte cases diagonales, mettre la valeur à true
	 * @return un set de quatre valeurs booléennes correspondant aux données importantes relatives à cette tâche
	 */
	private double calculerPoidsArete(int source, int destination, boolean diagonale) {
		if(diagonale) {
			return ((int) (listeSommets.get(source).getTempsSommet() + listeSommets.get(destination).getTempsSommet()))/Math.sqrt(2) ; // Théorème de Pythagore
		}
		return ((int) (listeSommets.get(source).getTempsSommet() + listeSommets.get(destination).getTempsSommet()))/2 ;
	}
	
	/**
	 * Méthode relative au projet Ayutthaya uniquement. Permet au feu de se propager.
	 */
	public void propagationFeuSommets() {
		ArrayList<Sommet> ontPrisFeu = new ArrayList<Sommet>() ; // Liste permettant de stocker les sommets qui viennent de prendre feu pour ne pas qu'une boucle se crée
		LinkedList<Sommet> sommetsVoisinsActualiser = new LinkedList<Sommet>() ; // liste des sommets dont il faudra recalculer le poids des arêtes
		
		ArrayList<Double> valeursTypesDeSol = new ArrayList<Double>() ; // on stocke les valeurs des sommets pouvant prendre feu
		valeursTypesDeSol.add(this.typesDeSol.get(".")) ; // les cases de type sol classique peuvent prendre feu
		valeursTypesDeSol.add(this.typesDeSol.get("S")) ; // si la sortie prend feu avant que le sommet ait été visité, on arête l'algorithme
		//valeursTypesDeSol.add(this.typesDeSol.get("D")) ; // Dans certains cas, la case de départ peut bloquer le feu, ligne à décommenter si on ne veut pas que cela puisse être possible

		for(Sommet s : this.listeSommets) { // pour chaque sommet du graphe...
			if(s.getTempsSommet() == this.typesDeSol.get("F").doubleValue() && !ontPrisFeu.contains(s)) { // ... on regarde si la case est en feu et si elle ne vient pas simplement de prendre feu

				LinkedList<Arete> listeAdjacenceActualisee = new LinkedList<Arete>() ; 
				for(Arete a : s.getListeAdjacence()) sommetsVoisinsActualiser.add(this.listeSommets.get(a.getDestination())) ; // pour chaque sommet voisin du sommet qui va prendre feu, on met ses voisins dans une liste
				HashMap<String, Boolean> ouActualiserAretes = s.ouAjouterAretes(this) ; // quels sommets voisins doivent prendre feu ?
				
				if(ouActualiserAretes.get("gauche")) { // s'il existe un sommet à gauche du sommet en feu
					Sommet sGauche = this.listeSommets.get(s.getCle()-1) ; 
					if(valeursTypesDeSol.contains(sGauche.getTempsSommet())) { // et qu'il peut prendre feu
						sGauche.senflamme(this); // on l'allume
						ontPrisFeu.add(sGauche) ; // et on l'ajoute dans la liste des sommets qui viennet de prendre feu
					}
				}
				if(ouActualiserAretes.get("droite")) {
					Sommet sDroit = listeSommets.get(s.getCle()+1) ;
					if(valeursTypesDeSol.contains(sDroit.getTempsSommet())) {
						sDroit.senflamme(this);
						ontPrisFeu.add(sDroit) ;
					}
				}
				if(ouActualiserAretes.get("haut")) {
					Sommet sHaut = this.listeSommets.get(s.getCle()-this.getNbColonnes()) ;
					if(valeursTypesDeSol.contains(sHaut.getTempsSommet())) {
						sHaut.senflamme(this);
						ontPrisFeu.add(sHaut) ;
					}
				}
				if(ouActualiserAretes.get("bas")) {
					Sommet sBas = this.listeSommets.get(s.getCle()+this.getNbColonnes()) ;
					if(valeursTypesDeSol.contains(sBas.getTempsSommet())) {
						sBas.senflamme(this);
						ontPrisFeu.add(sBas) ;
					}
				}

				s.setListeAdjacence(listeAdjacenceActualisee) ; //actualisation des arêtes voisines dont la valeur a changé

				while(!sommetsVoisinsActualiser.isEmpty()) { // on actualise les valeurs des arêtes voisines
					Sommet voisin = sommetsVoisinsActualiser.poll();
					voisin.setListeAdjacence(this.ajouterJusqueHuitAretes(voisin.getCle(), voisin.ouAjouterAretes(this), false)) ;
				}
			}
		}
	}
	
	/**
	 * Méthode permettant de résorber la propagation du feu (le graphe est remis à son état d'origine)
	 */
	public void resorberFeu() {
		LinkedList<Sommet> sommetsAretesAActualiser = new LinkedList<Sommet>() ; // liste des sommets dont il faudra recalculer le poids des arêtes
		for(Sommet s : this.listeSommets) {
			sommetsAretesAActualiser.add(s) ;
			s.feuSeResorbe(this) ;
		}
		
		Sommet arrivee = this.listeSommets.get(this.numArrivee) ;
		arrivee.setTempsSommet(this.getTypesDeSol().get("S"));
		sommetsAretesAActualiser.add(arrivee) ;
		
		Sommet feu = this.listeSommets.get(this.getNumFeu()) ;
		feu.setTempsSommet(this.getTypesDeSol().get("F")) ;
		sommetsAretesAActualiser.add(feu) ;
		
		while(!sommetsAretesAActualiser.isEmpty()) { // on actualise les valeurs de chacune des arêtes voisines d'un sommet dont le temps à la source a été actualisé
			Sommet s = sommetsAretesAActualiser.poll();
			s.setListeAdjacence(this.ajouterJusqueHuitAretes(s.getCle(), s.ouAjouterAretes(this), false)) ;
		}
	}
	
	/**
	 * Dans le cadre du projet Ayutthaya, on peut être amené à devoir reset le temps de chaque sommet depuis la source
	 * Cette méthode exécute cette tâche
	 */
	public void resetTempsDepuisSource() {
		for(Sommet s : this.listeSommets) {
			s.setTempsDepuisSource(Double.POSITIVE_INFINITY);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder() ;
		sb.append("Graph de "+numSommets+" sommets :\n") ;
		for(Sommet v : listeSommets) {
			sb.append("["+v.toString()+"]\n") ;
		}
		return sb.toString(); 
	}

	/**
	 * Méthode pour ajouter une nouvelle couleur de sol et associer une pondération valeur à cette couleur
	 * @param temps pondération associée à la couleur
	 * @param couleur couleur du sommet a afficher
	 */
	public void ajouterCouleurDeSol(double temps, String couleur) {
		if(!this.couleursDeSol.containsKey(temps)) this.couleursDeSol.put(temps, couleur) ;
	}
	
	/**
	 * Méthode pour ajouter un nouveau type de sol et lui associer une pondération
	 * @param temps pondération associée à la couleur
	 * @param nom du type de sol
	 */
	public void ajouterTypeDeSol(String nom, double temps) {
		if(!this.typesDeSol.containsKey(nom)) this.typesDeSol.put(nom, temps) ;
	}
	
	// Getters et Setters de la classe
	
	public int getNbColonnes() {
		return this.nbColonnes ;
	}

	public void setNbColonnes(int nbColonnes) {
		if(this.nbColonnes == -1) {
			this.nbColonnes = nbColonnes ;
		} else System.err.println("Le nombre de colonnes a déjà été défini.") ;
	}

	public int getNbLignes() {
		return this.nbLignes ;
	}

	public void setNbLignes(int nbLignes) {
		if(this.nbLignes == -1) {
			this.nbLignes = nbLignes ;
		} else System.err.println("Le nombre de lignes a déjà été défini.") ;
	}

	public int getNumSource() {
		return this.numSource ;
	}

	public void setNumSource(int numSource) {
		if(this.numSource == -1) {
			this.numSource = numSource ;
		} else System.err.println("La source a déjà été définie.") ;
	}

	public int getNumArrivee() {
		return this.numArrivee ;
	}

	public void setNumArrivee(int numArrivee) {
		if(this.numArrivee == -1) {
			this.numArrivee = numArrivee ;
		} else System.err.println("L'arrivée a déjà été définie.") ;
	}
	
	public int getNumFeu() {
		return this.numSourceFeu ;
	}

	public void setNumFeu(int numFeu) {
		if(this.numSourceFeu == -1) {
			this.numSourceFeu = numFeu ;
		} else System.err.println("La case du feu a déjà été définie.") ;
	}

	public ArrayList<Sommet> getListeSommets() {
		return listeSommets;
	}

	public int getNumSommets() {
		return this.numSommets;
	}

	public HashMap<String, Double> getTypesDeSol() {
		return this.typesDeSol ;
	}

	public HashMap<Double, String> getCouleursDeSol() {
		return this.couleursDeSol ;
	}
}
