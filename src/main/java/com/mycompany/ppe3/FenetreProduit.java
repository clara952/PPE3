/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ppe3;

import com.mycompany.ppe3.Tests.BDD;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author c.bigeard
 */
public class FenetreProduit extends javax.swing.JFrame {
    public static Integer fenetre;
    public static Integer idProduit;
    
    /**
     * Creates new form FenetreProduit
     */
    public FenetreProduit() {
        initComponents();
    }
    /**
     * Méthode qui retourne le numéro fenêtre choisie
     * @return 
     */
    public static Integer getFenetre() {
        return fenetre;
    }
    /**
     * Méthode qui récupère le numéro fenêtre choisie
     * @param fenetre 
     */
    public static void setFenetre(Integer fenetre) {
        FenetreProduit.fenetre = fenetre;
    }
    /**
     * Méthode qui retourn l'idProduit
     * @return 
     */
    public static Integer getIdProduit() {
        return idProduit;
    }
    /**
     * Méthode qui récupère l'idProduit
     * @param idProduit 
     */
    public static void setIdProduit(Integer idProduit) {
        FenetreProduit.idProduit = idProduit;
    }
    /**
     * Méthode pour afficher liste personnel dans la jList
     */
    public void afficherProduit() throws IOException{
        try {
            String sql = "select * from produit where idProduit = " + getIdProduit();
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            if (lesTuples.next()) {                
                String http = lesTuples.getString(5);
                Image image = null;
                try {
                    URL url = new URL(http);
                    image = ImageIO.read(url);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FenetreProduit.class.getName()).log(Level.SEVERE, null, ex);
                }
                ImageIcon icon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(197, 159, Image.SCALE_DEFAULT));
                jLabelIcon.setIcon(icon);

                jTextFieldNomProduit.setText(lesTuples.getString(2));
                jComboBoxCategorie.getModel().setSelectedItem(BoxLibelleCategorie(lesTuples.getString(7)));
                jTextFieldPrixProduit.setText(lesTuples.getString(3));
                jTextFieldStockProduit.setText(lesTuples.getString(4));
                jTextFieldLienImage.setText(lesTuples.getString(5));
                jTextFieldLienImage.setPreferredSize(new Dimension(367, 25));
                jTextFieldAvisProduit.setText(lesTuples.getString(6));            
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Méthode qui ajoute un produit à la BDD
     * @return 
     */
    public String ajouterProduit(){
        String erreur = "rien";
        //Vérifier que le client en question n'existe pas déjà
        String verification = "select count(*) from produit where libelleProduit = '" + jTextFieldNomProduit.getText()+ "'";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(verification);
        try {
            lesTuples.next();
            switch (lesTuples.getInt("count(*)")) {
                case 1:     //Il existe un peronnel avec les même informations
                    erreur = "Ce produit existe déjà";
                    break;
                case 0:     //Il n'y a pas de client avec les mêmes utilisateurs
                    String sql = "insert into produit values (NULL, '" + jTextFieldNomProduit.getText()+ "','" + jTextFieldPrixProduit.getText() + "','" + jTextFieldStockProduit.getText() + "','" + jTextFieldLienImage + "','" +  jTextFieldAvisProduit.getText() +  "','" + BoxIdCategorie() + "')";
                    ConnexionBDD.getInstance().requeteAction(sql);
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FenetreProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return erreur;
    }
    /**
     * Méthode qui modifie les information du produit sélectionné
     */
    public void modificationProduit(){
        
        String sql = "update produit set libelleProduit = '" + jTextFieldNomProduit.getText()+ "', prix = '" + jTextFieldPrixProduit.getText() + "', stock = '" + jTextFieldStockProduit.getText() + "', image = '" + jTextFieldLienImage + "', popularite = '" + jTextFieldAvisProduit.getText() + "', idCategorie = '" + BoxIdCategorie() + "' where idProduit = " + getIdProduit();
        ConnexionBDD.getInstance().requeteAction(sql);
    }
    /**
     * Méthode qui complète la combo box avec les catégories existantes
     */
    public void comboBoxCategorie(){
        String tuple;
        DefaultComboBoxModel leModel = (DefaultComboBoxModel)jComboBoxCategorie.getModel();
        try {
            String sql = "select * from categorie";
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            while (lesTuples.next()) { 
                tuple = lesTuples.getString(2);
                leModel.addElement(tuple);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    /**
     * Méthode qui retourn l'idCatégorie de la catégorie sélectionnée
     * @return 
     */
    public String BoxIdCategorie(){
        String idCategorie = null;
        String categorie = jComboBoxCategorie.getSelectedItem().toString();
        String sql = "select idCategorie from categorie where libelleCategorie = '" + categorie + "'";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            lesTuples.next();
            idCategorie = lesTuples.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(FenetrePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idCategorie;
    }
    /**
     * Méthode qui retourne le libelleCategorie selon l'idCategorie donné
     * @param idCategorie
     * @return 
     */
    public String BoxLibelleCategorie(String idCategorie){
        String libelleCategorie = null;
        String sql = "select libelleCategorie from categorie where idCategorie = '" + idCategorie + "'";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            lesTuples.next();
            libelleCategorie = lesTuples.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(FenetrePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return libelleCategorie;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelTitre = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldNomProduit = new javax.swing.JTextField();
        jTextFieldPrixProduit = new javax.swing.JTextField();
        jTextFieldStockProduit = new javax.swing.JTextField();
        jTextFieldAvisProduit = new javax.swing.JTextField();
        jButtonAnuller = new javax.swing.JButton();
        jButtonConfirmer = new javax.swing.JButton();
        jLabelErreur = new javax.swing.JLabel();
        jLabelIcon = new javax.swing.JLabel();
        jLabelLienImage = new javax.swing.JLabel();
        jTextFieldLienImage = new javax.swing.JTextField();
        jComboBoxCategorie = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabelTitre.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTitre.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelTitre.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitre.setText("Produit");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitre)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitre)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Nom :");

        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Catégorie :");

        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Prix :");

        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Stock :");

        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Avis :");

        jTextFieldNomProduit.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldNomProduit.setForeground(new java.awt.Color(0, 0, 0));

        jTextFieldPrixProduit.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldPrixProduit.setForeground(new java.awt.Color(0, 0, 0));

        jTextFieldStockProduit.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldStockProduit.setForeground(new java.awt.Color(0, 0, 0));

        jTextFieldAvisProduit.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldAvisProduit.setForeground(new java.awt.Color(0, 0, 0));

        jButtonAnuller.setText("Annuler");
        jButtonAnuller.setBorder(null);
        jButtonAnuller.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAnullerMouseClicked(evt);
            }
        });

        jButtonConfirmer.setText("Confirmer");
        jButtonConfirmer.setBorder(null);
        jButtonConfirmer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonConfirmerMouseClicked(evt);
            }
        });

        jLabelLienImage.setForeground(new java.awt.Color(51, 51, 51));
        jLabelLienImage.setText("Lien image :");

        jTextFieldLienImage.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldLienImage.setForeground(new java.awt.Color(0, 0, 0));

        jComboBoxCategorie.setModel(new DefaultComboBoxModel());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonAnuller, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(jLabelErreur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonConfirmer, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(33, 33, 33)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldAvisProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(jTextFieldStockProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(jTextFieldPrixProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(jTextFieldNomProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(jComboBoxCategorie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelLienImage)
                                .addGap(33, 33, 33)
                                .addComponent(jTextFieldLienImage)))
                        .addContainerGap(31, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldNomProduit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldPrixProduit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextFieldStockProduit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextFieldAvisProduit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLienImage)
                    .addComponent(jTextFieldLienImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAnuller, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConfirmer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelErreur, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(526, 414));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        
    }//GEN-LAST:event_formWindowActivated
    /**
     * Bouton pour femer la fenêtre
     * @param evt 
     */
    private void jButtonAnullerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAnullerMouseClicked
        this.dispose();
    }//GEN-LAST:event_jButtonAnullerMouseClicked
    /**
     * Actions à faire quand la fenêtre est ouverte
     * @param evt 
     */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (fenetre == 1) {         //Fênetre d'affichage des informations du client
            jLabelTitre.setText("Produit");
            //Les données ne doivent pas pouvoir être modifié dans la fenêtre d'affichage
            jTextFieldNomProduit.setEditable(false);
            jComboBoxCategorie.setEditable(false);
            jTextFieldPrixProduit.setEditable(false);
            jTextFieldStockProduit.setEditable(false);
            jTextFieldAvisProduit.setEditable(false);
            //Le lien et le bouton confirmer n'ont pas besoin d'être affichés
            jButtonConfirmer.setVisible(false);
            jLabelLienImage.setVisible(false);
            jTextFieldLienImage.setVisible(false);
            
            comboBoxCategorie();
            try {
                afficherProduit();
            } catch (IOException ex) {
                Logger.getLogger(FenetreProduit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if (fenetre == 2) {   //Fênetre de modification d'affichage des informations du client
            jLabelTitre.setText("Modifier produit");
            comboBoxCategorie();
            try {
                afficherProduit();
            } catch (IOException ex) {
                Logger.getLogger(FenetreProduit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{                      //Fênetre de création d'un client
            jLabelTitre.setText("Ajouter produit"); 
            
            comboBoxCategorie();
        }
    }//GEN-LAST:event_formWindowOpened
    /**
     * Bouton pour confirmer l'ajout/modification et fermer la fenêtre
     * @param evt 
     */
    private void jButtonConfirmerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonConfirmerMouseClicked
        if (fenetre == 2) {     //Fenetre de modification du client
            modificationProduit();
            this.dispose();
        }else{                  //Fenetre d'ajout de client
            if (ajouterProduit()== "rien") {
                ajouterProduit();
                this.dispose();
            }else{
                jLabelErreur.setText(ajouterProduit());
            }
        }
    }//GEN-LAST:event_jButtonConfirmerMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FenetreProduit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FenetreProduit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FenetreProduit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FenetreProduit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FenetreProduit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnuller;
    private javax.swing.JButton jButtonConfirmer;
    private javax.swing.JComboBox<String> jComboBoxCategorie;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelErreur;
    private javax.swing.JLabel jLabelIcon;
    private javax.swing.JLabel jLabelLienImage;
    private javax.swing.JLabel jLabelTitre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextFieldAvisProduit;
    private javax.swing.JTextField jTextFieldLienImage;
    private javax.swing.JTextField jTextFieldNomProduit;
    private javax.swing.JTextField jTextFieldPrixProduit;
    private javax.swing.JTextField jTextFieldStockProduit;
    // End of variables declaration//GEN-END:variables
}
