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
public class BDD {
    String serveur;
    String port;
    String nomBDD;
    String id;
    String mdp;
    Connection connexion;

    public void toto(){
        nomBDD = "tablette";
        port = "3306";
        serveur = "jdbc:mysql://10.0.10.136:" + port + "/" + nomBDD;
        id = "root";
        mdp = "root";
        
        try {
            this.connexion = DriverManager.getConnection(serveur, id, mdp);
            System.out.println("Connecté");
        } catch (Exception e) {
            System.out.println("Erreur de connexion");
        }       
    }
    public void genererTuples(Integer n){
        try {
            Statement requete = connexion.createStatement();
            requete.executeUpdate("delete from categorie");
            int i;
            for (i = 0; i < n; i++) {
                String sql = "insert into categorie values (null,'categ" + i + "')";
                requete.executeUpdate(sql);
            }
            System.out.println("Insertion de : " + i + " tuples");

        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void afficherTuples(){
        Statement requete;
        try {
            requete = connexion.createStatement();
            ResultSet lesTuples = requete.executeQuery("select * from categorie");
            while (lesTuples.next()) {                
                System.out.println(lesTuples.getString(1) + " - " + lesTuples.getString(2)); 

            }
        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        BDD bdd = new BDD();
        bdd.toto();
        bdd.genererTuples(10);
        bdd.afficherTuples();
    }
}
