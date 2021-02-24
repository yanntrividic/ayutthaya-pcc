import matplotlib.pyplot as plt
import pandas
import matplotlib.pyplot as plt
import sys
import avg

plt.close('all')

if len(sys.argv) == 2 :
	data = pandas.read_csv(sys.argv[1]) #lecture des données du fichier passé en ligne de command
else :
	data = pandas.read_csv('results_g2Alea_10_100.csv')

data = data.sort_values(by ='nbSommetsGraphe') #tri des données
data = data.drop(['nomAlgo', 'nomFichier', 'numInstance', 'score', 'tempsChemin', 'nbSommetsParcourus'], axis=1) #on enlève les colonnes superflues

manhattan = data.loc[data['heuristique'] == 'Manhattan'].drop(['heuristique'], axis=1) #extraction par catégories
euclidienne = data.loc[data['heuristique'] == 'euclidienne'].drop(['heuristique'], axis=1) 
ayutthaya = data.loc[data['heuristique'] == 'Ayutthaya'].drop(['heuristique'], axis=1) 

x = manhattan['nbSommetsGraphe'].tolist() # les valeurs en x

yManhattan = manhattan['tempsCalcul'].tolist() # les valeurs en y
yEuclidienne = euclidienne['tempsCalcul'].tolist()
yAyutthaya = ayutthaya['tempsCalcul'].tolist()

plt.xlabel("Nombre de sommets du graphe")
plt.ylabel("Temps de calcul de l'algorithme (en ms - échelle logarithmique)")
plt.title("Complexité de l'algorithme A* en fonction des différentes heuristiques utilisées")

plt.yscale("log") # échelle logarithmique

plt.plot(x, yManhattan, label='Manhattan')
plt.plot(x, yEuclidienne, label='Euclidienne')
plt.plot(x, yAyutthaya, label='Ayutthaya')

print("Complexité moyenne en ms - Manhattan : 		",avg.Average(yManhattan))
print("Complexité moyenne en ms - euclidienne :	",avg.Average(yEuclidienne))
print("Complexité moyenne en ms - Ayutthaya : 		",avg.Average(yAyutthaya))

plt.legend()
plt.show()