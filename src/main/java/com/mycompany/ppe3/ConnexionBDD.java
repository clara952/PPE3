/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ppe3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c.bigeard
 */
public class ConnexionBDD {
    /**
     * Membres static (de classe)
     *
     */
//    private static String nomServeur = "localhost";
//    private static String nomServeur = "10.0.10.196";
    private static String nomServeur = "10.0.10.129";
//    private static String port = "3308";
    private static String port = "3306";
//    private static String nomBdd = "tablette";
    private static String nomBdd = "tablette_bigeard";
    private static String nomUtilisateur = "root";
    private static String motDePasse = "root";

    private static String chaineConnexion;

    //propriété non statique
    private Connection connexion;

    private static ConnexionBDD monDao = null;

    /**
     * Constructeur privé ! pour construire un objet, il faut utiliser la
     * méthode publique statique getDaoSIO
     *
     */
    private ConnexionBDD(){        
        try {
            //Définition de l'emplacement de la BDD
            ConnexionBDD.chaineConnexion = "jdbc:mysql://" + ConnexionBDD.nomServeur + ":" + ConnexionBDD.port + "/" + ConnexionBDD.nomBdd;
            
            //Création de la connexion à la BDD
            this.connexion = (Connection) DriverManager.getConnection(ConnexionBDD.chaineConnexion, ConnexionBDD.nomUtilisateur, ConnexionBDD.motDePasse);

        } catch (SQLException ex) {
            Logger.getLogger(ConnexionBDD.class.getName()).log(Level.SEVERE, null, ex);
        }       
    } 
    /**
     * Permet d'obtenir l'objet instancié
     * @return un Objet DaoSIO avec connexion active ... pour une certaine durée
     */
    public static ConnexionBDD getInstance() {

        if (ConnexionBDD.monDao==null ) {
            ConnexionBDD.monDao = new ConnexionBDD();
        }else{
            if(!ConnexionBDD.monDao.connexionActive()){
            ConnexionBDD.monDao = new ConnexionBDD();    
            }
        }
        return ConnexionBDD.monDao;
    }

    public Boolean connexionActive() {
        Boolean connexionActive = true;
        try {
            if (this.connexion.isClosed()) {
                connexionActive = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnexionBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connexionActive;
    }
/**
 * 
 * @param sql, comportera un ordre selec
 * @return 
 */
    public ResultSet requeteSelection(String sql){
   
        try {
            Statement requete=new ConnexionBDD().connexion.createStatement();
            return requete.executeQuery(sql);
           
        } catch (SQLException ex) {
            Logger.getLogger(ConnexionBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       
    }
    /**
     * 
     * @param sql, comportera un ordre insert, update, select, alter, etc.
     * @return le nombre de lignes impactées par la requête action
     * 
     */
      public Integer requeteAction(String sql){
   
        try {
            Statement requete=new ConnexionBDD().connexion.createStatement();
            return requete.executeUpdate(sql);
           
        } catch (SQLException ex) {
            Logger.getLogger(ConnexionBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
       
    }  
}
