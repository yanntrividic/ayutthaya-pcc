import matplotlib.pyplot as plt
import pandas
import matplotlib.pyplot as plt
import sys
import avg

plt.close('all')

if len(sys.argv) == 2 :
	data = pandas.read_csv(sys.argv[1]) #lecture des données
else :
	data = pandas.read_csv('results_gAlea_10_150.csv')

data = data.sort_values(by ='nbSommetsGraphe') #tri des données
data = data.drop(['nomFichier', 'numInstance', 'score', 'tempsChemin', 'tempsCalcul'], axis=1) #on enlève les colonnes superflues

dijsktra = data.loc[data['nomAlgo'] == 'Dijsktra'].drop(['heuristique', 'nomAlgo'], axis=1)

data.drop(['nomAlgo'], axis=1)

manhattan = data.loc[data['heuristique'] == 'Manhattan'].drop(['heuristique'], axis=1) #extraction par catégories
euclidienne = data.loc[data['heuristique'] == 'euclidienne'].drop(['heuristique'], axis=1) 

x = manhattan['nbSommetsGraphe'].tolist() # les valeurs en x

yManhattan = manhattan['nbSommetsParcourus'].tolist() # les valeurs en y
yEuclidienne = euclidienne['nbSommetsParcourus'].tolist()
yDijsktra = dijsktra['nbSommetsParcourus'].tolist()

plt.xlabel("Nombre de sommets du graphe")
plt.ylabel("Nombre de sommets visités avant de trouver le plus court chemin")
plt.title("Plus court chemin et nombre de sommets visités")

plt.yscale("log") # échelle logarithmique
plt.plot(x, yManhattan, label='A* - Manhattan')
plt.plot(x, yEuclidienne, label='A* - Euclidienne')
plt.plot(x, yDijsktra, label='Dijsktra')

print("Nombre de sommets visités moyen - Manhattan : 	",avg.Average(yManhattan))
print("Nombre de sommets visités moyen - euclidienne :	",avg.Average(yEuclidienne))
print("Nombre de sommets visités moyen - Dijsktra : 	",avg.Average(yDijsktra))

plt.legend()
plt.show()