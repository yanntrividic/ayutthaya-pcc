package main.core.algorithmie.complexite;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe statique permettant de générer dee manière aléatoire des fichiers G et G2. Voir le README.
 */
public class GenerateurFichiersGraphes {
		
	private static final int typeVert = 1 ; // Valeurs associées aux types de sols supportés par le générateur
	private static final int typeGris = 1000 ;
	private static final int typeBleu = 20 ;
	private static final int typeJaune = 30 ;
	
	private static final char [] typesG = { 'G', 'W', 'B', 'S' } ; // répartition des différents types de sols dans les fichiers G
	private static final double [] probasG = { 0.6, 0.8, 0.9, 1 } ;
	
	private static final char [] typesG2 = { '.', '#' } ; // répartition des types de sols non-particuliers dans les fichiers G2
	private static final double [] probasG2 = { 0.7, 1 } ;
	
	/**
	 * Méthode générant une série de fichiers G ou G2 de tailles variables dans un dossier donné avec un nom choisi
	 * @param tailleMin Nombre de lignes/colonnes minimal des fichiers à générer
	 * @param tailleMax Nombre de lignes/colonnes maximal des fichiers à générer
	 * @param nom nom qui sera attribué en préfixe du nom du fichier. Le fichier sera ensuite numéroté en fonction de son nombre de lignes/colonnes
	 * @param nomDossier Dossier déjà existant
	 * @param typeFichier Peut-être soit G soit G2
	 * @param nbInstances dans le cas des fichiers G2, on peut spécifier le nombre d'instances que l'on souhaite générer par fichier
	 * @return La liste des noms des fichiers générés triés par ordre alphabétique.
	 */
	public static final ArrayList<String> genererSerieGraphesAleatoiresCarresDansFichier(int tailleMin, int tailleMax, String nom, String nomDossier, String typeFichier, int nbInstances) {
		ArrayList<String> fichiers = new ArrayList<String>() ;
		for(int i = tailleMin ; i <= tailleMax ; i++) {
			String cheminFinalFichier = nomDossier+"/"+nom+i+"."+typeFichier.toLowerCase() ;
			genererGrapheAleatoireDansFichier(i, i, cheminFinalFichier, typeFichier, nbInstances) ; // Cette méthode est celle qui crée vraiment le fichier
			fichiers.add(cheminFinalFichier) ;
		}
		System.out.println("Fichiers générés avec succès dans le dossier "+nomDossier) ;
		Collections.sort(fichiers);
		return fichiers ;
	}
	
	/**
	 * Méthode permettant de générer un fichier G ou G2
	 * @param nbLignes nombre de lignes du ou des graphes à générer
	 * @param nbColonnes nombre de colonnes du ou des graphes à générer
	 * @param chemin Chemin du fichier à créer
	 * @param typeFichier Peut-être G ou G2
	 * @param nbInstances Dans le cas des fichiers G2, on peut avoir plusieurs instances
	 */
	public static void genererGrapheAleatoireDansFichier(int nbLignes, int nbColonnes, String chemin, String typeFichier, int nbInstances) {
		if(nbLignes < 4 || nbColonnes < 4) throw new IllegalArgumentException("Vos dimensions ne peuvent par être inférieures à 4.") ;
		try (FileWriter fw = new FileWriter(chemin)) {
			if(typeFichier == "g") { // dans le cadre du projet PCC
				ecrireDansFichierG(fw, nbLignes, nbColonnes) ;
			} else if(typeFichier == "g2") { // dans le cadre du projet Ayutthaya
				ecrireDansFichierG2(fw, nbLignes, nbColonnes, nbInstances) ; 
			} else System.err.println("Le type de fichiers que vous souhaitez générer n'est pas supporté.") ;
            fw.flush(); // clear buffer de writer
        } catch (FileNotFoundException e) { // c'est ici que se font les différents traitements d'Exception qui ont pu survenir lors de la création du fichier
            System.err.println("Le fichier " +chemin+" n'existe pas.");
        } catch (IOException e) {
            System.err.println("Problème avec la création du fichier " + chemin);
        }
        System.out.println("Votre fichier "+chemin+" a bien été généré.") ;
    }
	
	
	// METHODES RELATIVES AUX FICHIERS G
	
	private static void ecrireDansFichierG(FileWriter fw, int nbLignes, int nbColonnes) throws IOException {
		ecrireEnteteDansFichierGrapheG(fw, nbLignes, nbColonnes) ;
		ecrireDonneesAleatoiresDansFichierGrapheG(fw, nbLignes, nbColonnes) ;
		ecrireSourceArriveeDansFichierGrapheG(fw, nbLignes, nbColonnes) ;	
	}
	
	private static void ecrireEnteteDansFichierGrapheG(FileWriter fw, int nbLignes, int nbColonnes) throws IOException {
		fw.append("==Metadata==\n=Size=\nnlines=") ;
		fw.append(nbLignes+"\n") ;
		fw.append("ncol=") ;
		fw.append(nbColonnes+"\n") ;
		ecrireTypesDansFichierGrapheG(fw) ;
	}
	
	private static void ecrireTypesDansFichierGrapheG(FileWriter fw) throws IOException {
		fw.append("=Types=\n") ;
		fw.append("G="+typeVert+"\ngreen\n") ;
		fw.append("W="+typeGris+"\ngray\n") ;
		fw.append("B="+typeBleu+"\nblue\n") ;
		fw.append("S="+typeJaune+"\nyellow\n") ;
	}
	
	private static void ecrireSourceArriveeDansFichierGrapheG(FileWriter fw, int nbLignes, int nbColonnes) throws IOException {
		fw.append("==Path==\n") ;
		fw.append("Start="+"1,1"+"\n") ;
		fw.append("Finish="+(nbColonnes-1)+","+(nbLignes-1)+"\n") ;
	}
	
	// Génération des données
	private static void ecrireDonneesAleatoiresDansFichierGrapheG(FileWriter fw, int nbLignes, int nbColonnes) throws IOException {
		fw.append("==Graph==\n") ;
		for(int i = 0 ; i < nbLignes ; i++) {
			for(int j = 0 ; j < nbColonnes ; j++) {
				double valeur = Math.random() ; // on tire un numéro au hasard
				int x = 0 ;
				while(valeur > probasG[x]) x++ ; // et on regarde à quelle valeur il correspond dans notre distribution 
				fw.append(typesG[x]) ;
 			}
			fw.append("\n") ;
		}
	}
	
	
	// METHODES RELATIVES AUX FICHIERS G2
	
	private static void ecrireDansFichierG2(FileWriter fw, int nbLignes, int nbColonnes, int nbInstances) throws IOException {
		fw.append(nbInstances+"\n") ;
		ecrireDonneesAleatoiresDansFichierGrapheG2(fw, nbLignes, nbColonnes, nbInstances) ;
	}
	
	// Génération des données
	private static void ecrireDonneesAleatoiresDansFichierGrapheG2(FileWriter fw, int nbLignes, int nbColonnes, int nbInstances) throws IOException {
		for(int instance = 0 ; instance < nbInstances ; instance++) {
			int depart = (int) (Math.random()*nbLignes*nbColonnes) ; // on tire un numéro de sommet au hasard
			int sortie = depart ;
			while(sortie == depart) sortie = (int) (Math.random()*nbLignes*nbColonnes) ; // puis on tire une sortie différente de l'entrée
			int feu = depart ;
			while(feu == depart || feu == sortie) feu = (int) (Math.random()*nbLignes*nbColonnes) ; // puis une source de feu différente de l'entrée et de la sortie
			
			int compteur = 0 ; // pour savoir sur quel sommet on se situe
			
			fw.append(nbLignes+" "+nbColonnes+"\n") ;
			
			for(int i = 0 ; i < nbLignes ; i++) {
				for(int j = 0 ; j < nbColonnes ; j++) {
					if(depart == compteur) { // si on est sur la case départ
						fw.append('D') ; // on met le caractère associé au départ, etc.
					} else if(sortie == compteur) {
						fw.append('S') ;
					} else if(feu == compteur) {
						fw.append('F') ;
					} else {					
						double valeur = Math.random() ;
						int x = 0 ;
						while(valeur > probasG2[x]) x++ ;
						fw.append(typesG2[x]) ;
					}
					compteur++ ;
	 			}
				fw.append("\n") ;
			}
		}
	}
}