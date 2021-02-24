package main.io;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.core.algorithmie.complexite.Rapport;

/**
 * Classe statique permettant l'écriture dans des fichiers des différents résultats pouvant être extraits du programme
 */
public class EcritureResultats {
	
	/**
	 * Méthode permettant de générer un fichier TXT avec le plus court chemin trouvé lors de l'étude d'un fichier G avec un algorithme de la classe Algos
	 * @param r Le rapport contenant le chemin trouvé
	 * @param dossier Le dossier dans lequel sera enregristré le fichier
	 */
	public static void ecriturePCCVersFichier(Rapport r, String dossier) {
		String fichier = dossier+"/meilleurChemin.txt" ; 
		try {
			File file = new File(fichier);
			if (!file.exists()) {
				file.createNewFile();
			} 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
	
			for (int i: r.getChemin()) {
				bw.write(String.valueOf(i));
				bw.write('\n');
			}
			bw.close();	
			System.out.println("Votre fichier "+fichier+" a bien été enregistré.") ;
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Méthode écrivant dans un fichier CSV les résultats passés en arguments
	 * @param rapports une liste de rapports quelconque
	 * @param chemin Le chemin passé en argument doit être valide sinon la fonctiokltsn ne s'exécutera pas jusqu'au bout
	 */
	public static void ecrireRapportsDansCSV(ArrayList<Rapport> rapports, String chemin) {
        try (FileWriter fileWriter = new FileWriter(chemin)) {
        	fileWriter.append(Rapport.enteteCSV()+"\n") ; // écriture du nom des colonnes
    		for(Rapport t : rapports) fileWriter.append(t.formatCSV()+"\n") ; // écriture des données
        } catch (FileNotFoundException e) {
            System.err.println("Le chemin suivant est invalide : " + chemin);
            return ;
        } catch (IOException e) {
            System.err.println("Le chemin suivant est invalide : " + chemin);
            return ;
        }
        System.out.println("Votre fichier CSV a été généré à l'adresse suivante : "+chemin) ;
	}
	
	/**
	 * Méthode permettant de générer un fichier TXT écrivant pour chaque instance d'un fichier G2 les résultats 
	 * du problème soulevé avec le projet Ayutthaya. 
	 * Une ligne contient 'Y' si l'instance laisse une chance au prisonnier de s'évader et 'N' sinon.
	 * @param rapports les différents rapports relatifs aux instances du fichier étudié
	 * @param chemin Le chemin du fichier qui contiendra les résultats
	 */
	public static void enregistrerResultatsYN(ArrayList<Rapport> rapports, String chemin) {
        try (FileWriter fileWriter = new FileWriter(chemin)) {
    		for(Rapport t : rapports) {
    			if(t.getTempsChemin() != Double.POSITIVE_INFINITY) {
    				fileWriter.append("Y\n") ;
    			} else fileWriter.append("N\n") ;
    		}
        } catch (FileNotFoundException e) {
            System.err.println("Le chemin suivant est invalide : " + chemin);
            return ;
        } catch (IOException e) {
            System.err.println("Le chemin suivant est invalide : " + chemin);
            return ;
        }
	}
}
