package main.menu.utils;

public class AffichageConsole {
	
	//COMMUNS
	
	private static void enteteMenu() {
		System.out.print("\n\n-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n\n") ;
	}
	
	private static void politesse() {
		System.out.print("Veuillez entrer votre choix : ");
	}
	
	public static void afficherIntro() {
		System.out.println("Bienvenue !\n\nCette application a été développée dans le but d'étudier le fonctionnement de l'algorithme A* et de l'algorithme de Dijkstra.") ;
		System.out.println("Projet réalisé par Yann Trividic dans le cadre du cours Algorithmie Avancée de l'Université de Paris.") ;
	}
	
	public static void choixAffichageCarte() {
		enteteMenu() ;
		System.out.println("\nSouhaitez-vous visualiser l'exécution de vos algorithmes ?") ;
		System.out.println("1 - Oui") ;
		System.out.println("0 - Non") ;
		politesse() ;
	}
	
	public static void choixAttenteAffichage() {
		enteteMenu() ;
		System.out.println("\nQuelle durée en millisecondes voulez-vous temporiser entre chaque itération pour observer l'évolution des algorithmes ?"
				+ "\nPour une carte de taille moyenne, 50 ms est une bonne durée.") ;
		politesse() ;
	}
	
	public static void choixGenerationGraphes() {
		enteteMenu() ;
		System.out.println("\nSouhaitez-vous générer aléatoirement des graphes avant d'appliquer des algorithmes dessus ?") ;
		System.out.println("1 - Oui") ;
		System.out.println("0 - Non") ;
		politesse() ;
	}
	
	public static void choixProjet() {
		enteteMenu() ;
		System.out.println("\n  - Menu de choix du projet - \n") ;
		System.out.println("1 - Recherche du plus court chemin avec A* et Dijsktra") ;
		System.out.println("2 - Le labyrinthe : s'échapper d'Ayutthaya") ;
		System.out.println("0 - Quitter le programme") ;
		politesse() ;
	}
	
	public static void choixEnregistremenRapportCSV() {
		enteteMenu() ;
		System.out.println("\nSouhaitez-vous enregistrer les rapports des algorithmes exécutés dans un fichier CSV ?") ;
		System.out.println("1 - Oui") ;
		System.out.println("0 - Non") ;
		politesse() ;
	}
	
	
	// PLUS COURT CHEMIN
	
	public static void menuChoixFichiersPCC() {
		enteteMenu() ;
		System.out.println("\n  - Plus court chemin - Menu de choix de fichiers - \n") ;
		System.out.println("\nSouhaitez-vous exécuter les algorithmes sur un fichier en particulier, un dossier de fichiers ou des graphes générés aléatoirement ?") ;
		System.out.println("1 - Un fichier G contenant un graphe (vous pourrez ensuite enregistrer un fichier TXT contenant le meilleur chemin à condition d'exécuter un seul algorithme)") ;
		System.out.println("2 - Un dossier contenant plusieurs fichiers G") ;
		System.out.println("3 - Une série de fichiers générés aléatoirement") ;
		System.out.println("0 - Quitter le programme") ;
		politesse() ;
	}
	
	public static void choixAlgo() {
		enteteMenu() ;
		System.out.println("\n  - Plus court chemin - Menu de choix de l'algorithme - \n") ;
		System.out.println("1 - Exécuter l'algorithme de Dijkstra") ;
		System.out.println("2 - Exécuter l'algorithme A*") ;
		System.out.println("3 - Exécuter les deux algorithmes") ;
		System.out.println("0 - Quitter le programme") ;
		politesse() ;
	}
	
	public static void choixHeuristiquePCC() {
		enteteMenu() ;
		System.out.println("\n  - Plus court chemin - Menu de choix de l'heuristique pour l'algorithme A* - \n") ;
		System.out.println("1 - Utiliser la distance Manhattan") ;
		System.out.println("2 - Utiliser la distance euclidienne") ;
		System.out.println("3 - Utiliser toutes les heuristiques") ;
		System.out.println("0 - Quitter le programme") ;
		politesse() ;
	}
	
	
	// AYUTTHAYA
	
	public static void choixHeuristiqueAyutthaya() {
		enteteMenu() ;
		System.out.println("\n  - Ayutthaya - Menu de choix de l'heuristique pour l'algorithme A* - \n") ;
		System.out.println("1 - Utiliser la distance Manhattan") ;
		System.out.println("2 - Utiliser la distance euclidienne") ;
		System.out.println("3 - Utiliser notre heuristique sur mesure") ;
		System.out.println("4 - Utiliser toutes les heuristiques") ;
		System.out.println("0 - Quitter le programme") ;
		politesse() ;
	}
		
	public static void menuChoixFichiersLabyrinthe() {
		enteteMenu() ;
		System.out.println("\n  - Ayutthaya - Menu de choix de fichiers - \n") ;
		System.out.println("\nSouhaitez-vous exécuter les algorithmes sur un fichier en particulier, un dossier de fichiers ou des graphes générés aléatoirement ?") ;
		System.out.println("1 - Un fichier contenant plusieurs instances (on pourra ensuite enregistrer sous la forme d'un fichier TXT les résultats obtenus)") ;
		System.out.println("2 - Un dossier contenant plusieurs fichiers G2") ;
		System.out.println("3 - Une série de fichiers générés aléatoirement") ;
		System.out.println("0 - Quitter le programme") ;
		politesse() ;
	}
}
