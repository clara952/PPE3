# PPE3 
*Développement d’un logiciel écrit en JAVA*

## Usage
Application de type "client lourd" pour les employés d'une entreprise de vente. Permet aux agents de l'entreprise d'accéder à toutes les informations indispensables pour une vente chez un client, à partir de son ordinateur avec l'application.

## Visuel
![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_1.PNG)
![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_2.PNG)

## Installation
A partir du fichier PPE3-1.0-SNAPSHOT.jar, le logiciel peut être lancé (en théorie, en pratique cela ne marche pas).

Pour le moment, pour accéder à l'application il faut le faire à partir de Netbeans, en copiant tous les dossiers; Une fois récupéré et ouvert dans Netbeans il d'abords aller dans le fichier **ConnexionBDD.java** et modifier le nomServeur, le port, le nomBDD, le nomUtilisateur et le motDePasse avec ceux de votre base de données (à partir d'un docker, de wamp où autre)

```java
	private static String nomServeur = "10.0.10.196";
	private static String port = "8083";
    	private static String nomBdd = "tablette";
    	private static String nomUtilisateur = "root";
    	private static String motDePasse = "";
```

Dans votre base de donnée vous devrez créer une base de données, ici appelée **tablette**, et une fois fait vous pourrez, dans cette base de données copiez le **script sql** qui se trouve dans le Wiki. Avec ceci vous aurez une base de données avec toutes les tables dont vous aurez besoin pour l'application. Pour le tester vous pouvez aussi inseré des tuples dans les tables avec le fichier **SQL BDD entrer données pour test** dans le Wiki.

# Manuel d'utilisation

## Connexion
Au lancement de l'application, la première fenêtre qui apparaît est celle de connexion. Vous devrez le compléter pour afficher la prochaine fenêtre avec un pseudo et un mot de passe déjà inscrit dans la base de donnée.
![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_1.PNG)
Pour ajouter de nouveaux utilisateurs, il faudra le faire à partir d'un compte **administrateur**.


## Produits
Une fois connecté, en tant qu'**agent** ou **administrateur** on arrive sur la page *produit*
La différence entre les deux profils se trouve au niveau de la barre de navigation de gauche, les **agents** ne verront que les trois premiers boutons : *Produits*, *Clients*, *Vente*. Les **administrateurs** auront en plus les pages *Agents* et *Statistiques*.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_2.PNG)

Pour ce qui est de la gestion des produits : Les **agents** ne pourront seulement qu'afficher les produits selon leur catégorie et afficher leur informations.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_8.PNG)

Pour les **administrateurs**, ils peuvent en plus ajouter des catégories.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_3.PNG)

Modifier une catégorie sélectionnée.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_4.PNG)

Ajouter un produit.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_5.PNG)

Modifier un produit.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_6.PNG)

Supprimer le produit sélectionné avec une fenêtre de confirmation.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_7.PNG)

Ou simplement afficher ses informations.

## Clients
La page client ne diffère pas énormément entre les deux profils, l'**administrateur** à seulement en plus la possibilité de supprimer un client (l'**agent** ne peut pas le faire).

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_15.PNG)

Sinon, de même que le produit, on peut ajouter un client en entrant ses informations.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_16.PNG)

Le modifier ou simplement afficher ses informations.

## Vente
Pour les **agents**, seule une vente sera possible, il ne pourra pas afficher les autres ventes effectuées.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_9.PNG)

Ainsi, il peut choisir le client, et voir avec lui les différents produits souhaité, ainsi que leurs quantités.

![](https://github.com/clara952/PPE3/blob/master/images_PPE3/PPE3_10.PNG)

Le prix total du panier est affiché en bas de celui-ci et une fois la commande terminé il suffit de confirmer pour enregistrer la vente dans la base de données. Une facture sera écrité automatiquement et intégré elle aussi à la base de données.


## Etat du projet

#### Connexion 
Possible pour les personnes déjà inscrite dans la base de données, soit agent, soit administrateur

#### Agent
* Gestion des produits et catégorie (suppression des catégories non codée)
* Gestion d'une vente pour un client choisie (problème pour les factures)
* Gestion des clients

#### Administrateur
* Gestion des personnels et des profils (suppression des profils non codée)
* Gestion des produits et catégorie (suppression des catégories non codée)
* Gestion des ventes (en cours et problème pour les factures)
* Gestion des clients
* Affichage des statistiques (affichage du chiffre d'affaire annuel non codé)
* Statistiques (chiffre d'affaire annuel non codé)

#### Documentation
* Script SQL
* MCD BDD
* Document installation (à faire)
* Document utilisation (à faire)
* Diagramme de classe (à faire)
* Javadoc (à faire)
			
