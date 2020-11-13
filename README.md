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

[Lien vers le manuel d'utilisation de l'application](https://docs.google.com/document/d/1fQPri8qwvkwAs7LTUer4oeqf-xMoe0WIM1jeE22sogk/edit?usp=sharing)

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
			
