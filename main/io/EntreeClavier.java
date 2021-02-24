package main.io;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

import main.core.algorithmie.TypeAlgo;
import main.core.algorithmie.TypeHeuristique;
import main.menu.utils.AffichageConsole;
import main.menu.utils.FonctionnalitesCommunes;

public class EntreeClavier {
	public static int nbEssaisMax = 10 ; 
	
	public static TypeAlgo getChoixAlgorithme(Scanner sc) {
		// Choix de l'algorithme 
		AffichageConsole.choixAlgo();
		int choix = getEntierDansIntervalleInclu(0, 3, sc) ;
		
		switch(choix) {
		case 1 : return TypeAlgo.DIJSKTRA ;
		case 2 : return TypeAlgo.ASTAR ;
		case 3 : return null ;
		case 0 : 
			FonctionnalitesCommunes.finProgramme(sc);
			break ;
		default :
			System.err.println("Erreur lors du choix de l'algorithme.") ;
			FonctionnalitesCommunes.finProgramme(sc);
			break ;
		}
		return null ;
	}
	
	public static TypeHeuristique getChoixHeuristique(Scanner sc, boolean ayutthaya) {
		// Choix de l'algorithme 
		if(ayutthaya) {
			AffichageConsole.choixHeuristiqueAyutthaya();
		} else AffichageConsole.choixHeuristiquePCC();
		
		int choix = getEntierDansIntervalleInclu(0, ayutthaya?4:3, sc) ;
		if(!ayutthaya && choix == 3) choix = 4 ;
		
		switch(choix) {
		case 1 : return TypeHeuristique.MANHATTAN ;
		case 2 : return TypeHeuristique.EUCLIDIENNE ;
		case 3 : return TypeHeuristique.FEU ;
		case 4 : return null ;
		case 0 : 
			FonctionnalitesCommunes.finProgramme(sc);
			break ;
		default :
			System.err.println("Erreur lors du choix de l'heuristique.") ;
			FonctionnalitesCommunes.finProgramme(sc);
			break ;
		}
		return null ;
	}
	
	public static int getAttente(boolean affichageCarte, Scanner sc) {
		if (affichageCarte) {
			AffichageConsole.choixAttenteAffichage();
			return getEntierDansIntervalleInclu(0, 3000, sc) ;
		}
		return 0 ;
	}
	
	public static int getEntierDansIntervalleInclu(int min, int max, Scanner sc) {
		int valeur = min-1 ;
		int compteur = 0 ;
		//System.out.println("min = "+min+" max = "+max) ;
		do {
			if(compteur != 0) System.err.print("Votre nombre doit être compris entre "+min+" et "+max+". Réessayez : ") ;
			try {
				valeur = sc.nextInt() ;
			} catch(InputMismatchException e) {
				System.err.print("Il faut rentrer un entier. ") ;
				sc.next();
				valeur = max+1 ;
			}
			compteur ++ ;
			if(compteur == nbEssaisMax) {
				System.err.println("Nombre d'essais maximum autorisés dépassé. Sortie du programme");
				FonctionnalitesCommunes.finProgramme(sc);
			}
		} while(valeur < min || valeur > max) ;
		System.out.print("Votre choix : "+valeur+"\n");
		return valeur ;
	}
	

    public static String getDossierValide(Scanner sc) {
    	sc.nextLine();
    	String chemin ;
    	int compteur = 0 ;
    	do {
    		if (compteur != 0) System.err.print("Dossier invalide. Réessayez : ") ;
    		chemin = sc.nextLine() ;
    		compteur ++ ;
			if(compteur == nbEssaisMax) {
				System.err.println("Nombre d'essais maximum autorisés dépassé. Sortie du programme");
				FonctionnalitesCommunes.finProgramme(sc);
			}
    	} while(!Lecture.dossierExiste(chemin)) ;
    	if(chemin.endsWith("/")) chemin = chemin.substring(0,chemin.length()-1) ;
    	return chemin ;
    }
    
    public static String getNomFichierEcritureValide(Scanner sc) {
    	String dossier = getDossierValide(sc) ;
    	System.out.print("Veuillez entrer le nom de votre fichier : ") ;
    	return dossier+"/"+sc.nextLine().replace(" ", ""); //prend en compte les espaces, mais ne fait pas grand chose d'autre... à améliorer
    }
    
    public static String ajouterExtension(String nomFichier, String extension) {
    	String [] decoupe = nomFichier.split("\\.") ;
    	if(!decoupe[decoupe.length-1].toLowerCase().equals(extension)) nomFichier += "."+extension.toLowerCase() ;
    	return nomFichier ;
    }
    
    
    public static String getNomFichierEcritureValideAvecExtension(Scanner sc, String extension) {
    	return ajouterExtension(getNomFichierEcritureValide(sc), extension) ;
    }
    
    public static String getCheminValideAvecExtension(Scanner sc, String extension) {
    	sc.nextLine();
    	String chemin ;
    	int compteur = 0 ;
    	do {
    		chemin = sc.nextLine() ;
	    	if(!new File(chemin).exists()) {
	    		chemin = "" ;
	    		System.err.print("Le fichier que vous avez rentré n'existe pas. Recommencez : ") ;
	    	} else {
		    	String [] decoupe = chemin.split("\\.") ;
		    	if(!decoupe[decoupe.length-1].toLowerCase().equals(extension)) {
		    		chemin = "" ;
		    		System.err.print("Le fichier que vous avez indiqué n'est pas un fichier G. Recommencez : ") ;
		    	}
	    	}
			compteur ++ ;
			if(compteur == nbEssaisMax) {
				System.err.println("Nombre d'essais maximum autorisés dépassé. Sortie du programme");
				FonctionnalitesCommunes.finProgramme(sc);
			}
    	} while(chemin == "") ;
   	 return chemin ;
    }
}
