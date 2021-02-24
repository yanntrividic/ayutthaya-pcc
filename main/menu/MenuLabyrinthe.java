package main.menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import main.core.Graphe;
import main.core.algorithmie.GestionAlgos;
import main.core.algorithmie.TypeAlgo;
import main.core.algorithmie.TypeHeuristique;
import main.core.algorithmie.complexite.Rapport;
import main.io.EcritureResultats;
import main.io.EntreeClavier;
import main.io.Lecture;
import main.menu.utils.AffichageConsole;
import main.menu.utils.FonctionnalitesCommunes;
import main.vue.Carte;

/**
 * Classe statique contenant permettant d'accéder aux différentes fonctionnalités liées au projet Ayutthaya
 */
public class MenuLabyrinthe {
	
	/**
	 * Menu principal de la classe, permet de choisir parmi les différentes actions à effectuer puis de fermer proprement le programme
	 * @param sc Scanner permettant de récuperer les différents choix de l'utilisateur
	 * @param rapports Liste qui contiendra les rapports des différents algorithmes utilisés
	 * @param cartes Contient les fenêtres qui seront ouvertes pour la visualisation de l'exécution des algorithmes
	 */
	protected static void menuLabyrinthe(Scanner sc, ArrayList<Rapport> rapports, LinkedList<Carte> cartes) {
		String dossier = "" ;
		ArrayList<String> fichiersG2 = new ArrayList<String>() ;
				
		AffichageConsole.menuChoixFichiersLabyrinthe() ;
		int choix = EntreeClavier.getEntierDansIntervalleInclu(0, 3, sc) ; // choix de la fonctionnalité
		
		boolean afficheCarte = true ; // choix de la visualisation ou non
		if(choix != 0) {
			AffichageConsole.choixAffichageCarte();
			afficheCarte = (EntreeClavier.getEntierDansIntervalleInclu(0, 1, sc) == 1)?true:false ;
		}
		
		if(afficheCarte) main.core.algorithmie.Algos.attente = EntreeClavier.getAttente(afficheCarte, sc) ; // choix du temps de temporisation si visualisation
		TypeHeuristique heuristique ;
		
		switch(choix) {
		case 1:
			lireUnSeulFichierEtEnregistrer(sc, afficheCarte, cartes, rapports) ;
			break ;
		case 2:
			System.out.print("Veuillez entrer le chemin absolu vers un dossier contenant des fichiers G2 : ") ;
			dossier = EntreeClavier.getDossierValide(sc) ;
			fichiersG2 = Lecture.getFichiersExtension(dossier, "g2"); // récupère tous les fichiers G2 du dossier
			heuristique = EntreeClavier.getChoixHeuristique(sc, true) ; // choix de l'heuristique
			for(String f : fichiersG2)  lireGrapheDepuisFichierEtExecuterAlgo(dossier+"/"+f, heuristique, afficheCarte, cartes, rapports) ; // exécute A* avec l'heuristique choisie sur tous les fichiers
			break ;
		case 3:
			fichiersG2 = FonctionnalitesCommunes.menuGenerationFichiersAleatoires(sc, "g2") ; // génère puis récupère tous les G2 générés
			heuristique = EntreeClavier.getChoixHeuristique(sc, true) ; // choix de l'heuristique
			for(String f : fichiersG2)  lireGrapheDepuisFichierEtExecuterAlgo(f, heuristique, afficheCarte, cartes, rapports) ; // exécute A* avec l'heuristique choisie sur tous les fichiers
			break ;
		case 0:
			FonctionnalitesCommunes.finProgramme(sc);
			Carte.fermerFenetre(cartes) ; // puis on ferme les différentes fenêtres 
			break ;
		default:
			System.err.println("Erreur lors du choix de la fonctionnalité.") ;
			break ;
		}
		
		FonctionnalitesCommunes.enregistrementRapportsCSV(rapports, sc) ; // Après avoir exécuté la fonctionnalité demandée, on propose d'enregistrer en CSV
		Carte.fermerFenetre(cartes) ; // puis on ferme les différentes fenêtres 
		FonctionnalitesCommunes.finProgramme(sc) ; // et le scanner
	}
	
	/**
	 * Applique l'algorithme A* avec une heuristique choisie sur les instances contenues dans le fichier, 
	 * affiche ou non la visualisation et enregistre les résultats dans une liste de rapports
	 * @param fichier le fichier G2 contenant les instances à étudier
	 * @param heuristique L'heuristique a utiliser
	 * @param afficheCarte Si true, alors on affiche la visualisation
	 * @param cartes Les différentes visualisations
	 * @param rapports La liste de rapports qui contiendra les rapports d'exécution des différentes instances
	 */
	private static void lireGrapheDepuisFichierEtExecuterAlgo(String fichier, TypeHeuristique heuristique, boolean afficheCarte, LinkedList<Carte> cartes, ArrayList<Rapport> rapports) {
		ArrayList<Graphe> instances = Lecture.lireInstancesDepuisFichier(fichier) ;
		int compteurInstances = 1 ;
		TypeAlgo algo = TypeAlgo.ASTAR ;
		Carte carte = null ;

		for(Graphe graphe : instances) {
			Rapport r = null ;
	
			if(heuristique == null) {
				for(TypeHeuristique heuristiqueEnum : TypeHeuristique.values()) {
					if(afficheCarte) carte = Carte.initCarte(carte, graphe, fichier, compteurInstances, algo, heuristiqueEnum, cartes) ; // si on essaie d'appliquer plusieurs fois les algorithmes ici, le feu n'aura pas reset.
					r = GestionAlgos.getRapportAlgo(algo, heuristiqueEnum, fichier, compteurInstances, graphe, carte, true) ; // Exécute l'algorithme et retourne un rapport
					rapports.add(r) ; // ajout du rapport dans la liste
				}
			}
			else {
				if(afficheCarte) carte = Carte.initCarte(carte, graphe, fichier, compteurInstances, algo, heuristique, cartes) ; // si on essaie d'appliquer plusieurs fois les algorithmes ici, le feu n'aura pas reset.
				r = GestionAlgos.getRapportAlgo(algo, heuristique, fichier, compteurInstances, graphe, carte, true) ; // Exécute l'algorithme et retourne un rapport
				rapports.add(r) ; // ajout du rapport dans la liste
			}

			if(afficheCarte) { // on marque une pause entre deux instances
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					System.out.println("stop");
				}
			}
	
			while(cartes.size() > Carte.nbCartesMaxSimultanee) Carte.fermerFenetre(cartes.poll()) ; // tant que le nombre de fenêtres est supérieur au max voulu, on défile et on ferme
			compteurInstances++ ;
		}
	}
	
	/**
	 * Méthode correspondant au choix 1, permet d'exécuter l'algo sur un fichier en particulier et 
	 * d'enregistrer les résultats du problème posé dans un fichier choisi TXT dont on a choisi le dossier.
	 * @param sc Scanner permettant de récuperer les différents choix de l'utilisateur
	 * @param afficheCarte Si true, alors on affiche la visualisation
	 * @param cartes Les différentes visualisations
	 * @param rapports La liste de rapports qui contiendra les rapports d'exécution des différentes instances
	 */
	private static void lireUnSeulFichierEtEnregistrer(Scanner sc, boolean afficheCarte, LinkedList<Carte> cartes, ArrayList<Rapport> rapports) {
		System.out.print("Veuillez entrer le chemin absolu vers un fichier G2 : ") ;
		String fichier = EntreeClavier.getCheminValideAvecExtension(sc, "g2") ;
		
		TypeHeuristique heuristique = EntreeClavier.getChoixHeuristique(sc, true) ; // choix de l'heuristique
		
		if(heuristique == null) {
			for(TypeHeuristique heuristiqueEnum : TypeHeuristique.values()) lireGrapheDepuisFichierEtExecuterAlgo(fichier, heuristiqueEnum, afficheCarte, cartes, rapports) ;
		} else lireGrapheDepuisFichierEtExecuterAlgo(fichier, heuristique, afficheCarte, cartes, rapports) ;
		
		System.out.print("Veuillez entrer le chemin absolu du dossier dans lequel vous souhaitez "
				+ "enregistrer les résultats Y/N de l'application de l'algorithme "+TypeAlgo.ASTAR.toString()+(heuristique!=null?" avec l'heuristique "+heuristique.toString():"")+" : ");
		String dossierResultats = EntreeClavier.getDossierValide(sc) ; // choix du dossier d'enregistrement des résultats
		
		String [] decoupe = fichier.split("\\/") ; // On récupère le nom du fichier originel
		fichier = decoupe[decoupe.length-1] ;
		fichier = fichier.split("\\.")[0] ;
		
		String nomFichierResultats = "rapportYN_"+fichier+"_"+heuristique.toString()+".txt" ; // et on génère un nom à partir des String obtenus
		EcritureResultats.enregistrerResultatsYN(rapports, dossierResultats+"/"+nomFichierResultats) ;
		System.out.println("Le fichier "+nomFichierResultats+" a bien été enregistré dans le dossier "+dossierResultats) ;
	}
}
