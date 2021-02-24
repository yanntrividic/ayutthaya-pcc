package main.menu.utils;

import java.util.ArrayList;
import java.util.Scanner;

import main.core.algorithmie.complexite.GenerateurFichiersGraphes;
import main.core.algorithmie.complexite.Rapport;
import main.io.EcritureResultats;
import main.io.EntreeClavier;

/**
 * Classe statique implémentant quelques fonctionnalités communes aux deux projets
 */
public class FonctionnalitesCommunes {
	
	/**
	 * Ferme proprement le Scanner et termine le programme
	 * @param sc Scanner a fermer
	 */
	public static void finProgramme(Scanner sc) {
		sc.close();
		System.out.println("\n\nFin du programme.");
		System.exit(1);
	}
	
	/**
	 * Méthode permettant l'enregistrements des divers rapports obtenus au format CSV
	 * @param rapports Liste de rapports
	 * @param sc Scanner permettant de récuperer les choix de l'utilisateur
	 */
	public static void enregistrementRapportsCSV(ArrayList<Rapport> rapports, Scanner sc) {
		AffichageConsole.choixEnregistremenRapportCSV();
		int choix = EntreeClavier.getEntierDansIntervalleInclu(0, 1, sc) ;
		if(choix == 1) {
			System.out.print("Veuillez entrer le chemin absolu du dossier où vous souhaitez sauvegarder votre fichier : ");
			String csv = EntreeClavier.getNomFichierEcritureValideAvecExtension(sc, "csv") ;
			EcritureResultats.ecrireRapportsDansCSV(rapports, csv) ;
		}
	}
	
	/**
	 * Menu permettant à l'utilisateur de générer des fichiers G ou G2 (tous les graphes seront "carrés")
	 * @param sc Scanner permettant de récuperer les choix de l'utilisateur
	 * @param typeFichier Doit être G ou G2
	 * @return La liste des fichiers générés
	 */
	public static ArrayList<String> menuGenerationFichiersAleatoires(Scanner sc, String typeFichier) {
		System.out.print("Veuillez entrer le chemin absolu du dossier où vous souhaitez générer vos fichiers "+typeFichier.toUpperCase()+" : ");
		String dossier = EntreeClavier.getDossierValide(sc) ; // récupère le dossier où on souhaite enregistrer les fichiers
		System.out.print("Veuillez entrer le nom de base que vous voulez attribuer à vos fichiers "+typeFichier.toUpperCase()+" : ");
		String nomFichier = sc.nextLine() ; // et le préfixe du fichier

		System.out.print("Quel est le nombre de lignes/colonnes minimal du graphe à générer ? ");
		int min = EntreeClavier.getEntierDansIntervalleInclu(10, 1000, sc) ; // nombre de lignes/colonnes minimal
		System.out.print("Quel est le nombre de lignes/colonnes maximal du graphe à générer ? ");
		int max = EntreeClavier.getEntierDansIntervalleInclu(min, 1000, sc) ; // maximal
				
		if(typeFichier.toLowerCase() == "g2") {
			System.out.print("Combien d'instances doivent comporter vos fichiers ? ");
			int nbInstances = EntreeClavier.getEntierDansIntervalleInclu(1, 100, sc) ; // récupération du nombre d'instances
			return GenerateurFichiersGraphes.genererSerieGraphesAleatoiresCarresDansFichier(min, max, nomFichier, dossier, typeFichier, nbInstances) ;
		} else {
			return GenerateurFichiersGraphes.genererSerieGraphesAleatoiresCarresDansFichier(min, max, nomFichier, dossier, typeFichier, 1);
		}
	}
}
