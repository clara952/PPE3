/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ppe3;

import com.mycompany.ppe3.Tests.BDD;
import static com.mycompany.ppe3.FenetreClient.idClient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author c.bigeard
 */
public class FenetrePersonnel extends javax.swing.JFrame {
    public static Integer fenetre;
    public static Integer idPersonnel;
    /**
     * Creates new form FenetrePersonnel
     */
    public FenetrePersonnel() {
        initComponents();
    }
    /**
     * Méthode Get, retourne "fenetre"
     * @return 
     */
    public static Integer getFenetre() {
        return fenetre;
    }
    /**
     * Méthode Set, pour insérer la valeur dans "fenetre"
     * @param fenetre 
     */
    public static void setFenetre(Integer fenetre) {
        FenetrePersonnel.fenetre = fenetre;
    }
    /**
     * Méthode Get, retourne "idPersonnel"
     * @return 
     */
    public static Integer getIdPersonnel() {
        return idPersonnel;
    }
    /**
     * Méthode Set, pour insérer la valeur dans "fenetre"
     * @param idClient 
     */
    public static void setIdPersonnel(Integer idPersonnel) {
        FenetrePersonnel.idPersonnel = idPersonnel;
    }
    /**
     * Méthode pour afficher liste personnel dans la jList
     */
    public void afficherPersonnel(){
        try {
            String sql = "select * from personnel where idPersonnel = " + idPersonnel;
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            if (lesTuples.next()) {
                jTextFieldNom.setText(lesTuples.getString(2));
                jTextFieldPrenom.setText(lesTuples.getString(3));
                jTextFieldEmail.setText(lesTuples.getString(4));
                jTextFieldTel.setText(lesTuples.getString(5));    
                jTextFieldPseudo.setText(lesTuples.getString(6));    
                jTextFieldMDP.setText(lesTuples.getString(7));  
                jComboBoxProfil.getModel().setSelectedItem(BoxLibelleProfil(lesTuples.getString(8)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Méthode pour ajouter un personnel
     * @return 
     */
    public String ajouterPersonnel(){
        String erreur = "rien";
        //Vérifier que le client en question n'existe pas déjà
        String verification = "select count(*) from personnel where nomPersonnel = '" + jTextFieldNom.getText()+ "' and prenomPersonnel = '" + jTextFieldPrenom.getText() + "' and pseudo = '" + jTextFieldPseudo.getText() + "'";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(verification);
        try {
            lesTuples.next();
            switch (lesTuples.getInt("count(*)")) {
                case 1:     //Il existe un peronnel avec les même informations
                    erreur = "Ce personnel existe déjà";
                    break;
                case 0:     //Il n'y a pas de client avec les mêmes utilisateurs
                    String sql = "insert into personnel values (NULL, '" + jTextFieldNom.getText()+ "','" + jTextFieldPrenom.getText() + "','" + jTextFieldEmail.getText() + "','" + jTextFieldTel.getText() +  "','" + jTextFieldPseudo.getText() +  "','" + jTextFieldMDP.getText() +  "','" + BoxIdProfil() + "')";
                    ConnexionBDD.getInstance().requeteAction(sql);
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FenetreClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return erreur;
    }
    /**
     * Méthode pour modifier un personnel
     */
    public void modificationPersonnel(){
        
        String sql = "update personnel set nomPersonnel = '" + jTextFieldNom.getText()+ "', prenomPersonnel = '" + jTextFieldPrenom.getText() + "', emailPersonnel = '" + jTextFieldEmail.getText() + "', telPersonnel = '" + jTextFieldTel.getText() + "', pseudo = '" + jTextFieldPseudo.getText() + "', mdp = '" + jTextFieldMDP.getText() + "', idProfil = '" + BoxIdProfil() + "' where idPersonnel = " + getIdPersonnel();
        ConnexionBDD.getInstance().requeteAction(sql);
    }
    /**
     * Méthode qui complète la combo box avec les profils existants
     */
    public void comboBoxProfil(){
        String tuple;
        DefaultComboBoxModel leModel = (DefaultComboBoxModel)jComboBoxProfil.getModel();
        try {
            String sql = "select * from profil";
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
     * Méthode qui retourn l'id du profil choisi dans la combo box
     * @return 
     */
    public String BoxIdProfil(){
        String idProfil = null;
        String profil = jComboBoxProfil.getSelectedItem().toString();
        String sql = "select idProfil from profil where libelleProfil = '" + profil + "'";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            lesTuples.next();
            idProfil = lesTuples.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(FenetrePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idProfil;
    }
    /**
     * Méthode qui retourne le libelle du profil selon l'idProfil donné
     * @param idProfil
     * @return 
     */
    public String BoxLibelleProfil(String idProfil){
        String libelleProfil = null;
        String sql = "select libelleProfil from profil where idProfil = '" + idProfil + "'";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            lesTuples.next();
            libelleProfil = lesTuples.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(FenetrePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return libelleProfil;
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
        jLabelTitre = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNom = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPrenom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTel = new javax.swing.JTextField();
        jLabelErreur = new javax.swing.JLabel();
        jButtonConfirmer = new javax.swing.JButton();
        jButtonAnnuler = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldMDP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldPseudo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxProfil = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jLabelTitre.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelTitre.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitre.setText("Données personnel");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitre)
                .addContainerGap(329, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitre)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("nom :");

        jTextFieldNom.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldNom.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("prénom :");

        jTextFieldPrenom.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("email :");

        jTextFieldEmail.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("téléphone :");

        jTextFieldTel.setBackground(new java.awt.Color(204, 204, 204));

        jButtonConfirmer.setText("Confirmer");
        jButtonConfirmer.setBorder(null);
        jButtonConfirmer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonConfirmerMouseClicked(evt);
            }
        });

        jButtonAnnuler.setText("Annuler");
        jButtonAnnuler.setBorder(null);
        jButtonAnnuler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAnnulerMouseClicked(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("mot de passe :");

        jTextFieldMDP.setBackground(new java.awt.Color(204, 204, 204));

        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("pseudo :");

        jTextFieldPseudo.setBackground(new java.awt.Color(204, 204, 204));

        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("profil:");

        jComboBoxProfil.setModel(new DefaultComboBoxModel());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldNom)
                            .addComponent(jTextFieldPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldEmail)
                            .addComponent(jTextFieldTel, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelErreur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonConfirmer, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldPseudo, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMDP, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxProfil, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextFieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextFieldPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextFieldTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextFieldPseudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextFieldMDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBoxProfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelErreur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAnnuler, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jButtonConfirmer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 277, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 65, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Bouton pour confirmer la modification/l'ajout et fermer la fenêtre
     * @param evt 
     */
    private void jButtonConfirmerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonConfirmerMouseClicked
        if (fenetre == 2) {     //Fenetre de modification du client
            modificationPersonnel();
            this.dispose();
        }else{                  //Fenetre d'ajout de client
            if (ajouterPersonnel() == "rien") {
                ajouterPersonnel();
                this.dispose();
            }else{
                jLabelErreur.setText(ajouterPersonnel());
            }
        }
    }//GEN-LAST:event_jButtonConfirmerMouseClicked
    /**
     * Bouton pour fermer la fenêtre
     * @param evt 
     */
    private void jButtonAnnulerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAnnulerMouseClicked
        this.dispose();
    }//GEN-LAST:event_jButtonAnnulerMouseClicked
    /**
     * Actions à faire quand la fenêtre est ouverte
     * @param evt 
     */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (fenetre == 1) {         //Fênetre d'affichage des informations du client
            jLabelTitre.setText("Données du personnel");
            jTextFieldNom.setEditable(false);
            jTextFieldPrenom.setEditable(false);
            jTextFieldEmail.setEditable(false);
            jTextFieldTel.setEditable(false);
            jTextFieldPseudo.setEditable(false);
            jTextFieldMDP.setEditable(false);
            jComboBoxProfil.setEditable(false);
            jButtonConfirmer.setVisible(false);
            
            afficherPersonnel();
            comboBoxProfil();            
        }else if (fenetre == 2) {   //Fênetre de modification d'affichage des informations du client
            jLabelTitre.setText("Modifier personnel");
            
            afficherPersonnel();
            comboBoxProfil();
        }else{                      //Fênetre de création d'un client
            jLabelTitre.setText("Ajouter personnel");  
            
            comboBoxProfil();
        }
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(FenetrePersonnel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FenetrePersonnel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FenetrePersonnel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FenetrePersonnel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FenetrePersonnel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnnuler;
    private javax.swing.JButton jButtonConfirmer;
    private javax.swing.JComboBox<String> jComboBoxProfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelErreur;
    private javax.swing.JLabel jLabelTitre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldMDP;
    private javax.swing.JTextField jTextFieldNom;
    private javax.swing.JTextField jTextFieldPrenom;
    private javax.swing.JTextField jTextFieldPseudo;
    private javax.swing.JTextField jTextFieldTel;
    // End of variables declaration//GEN-END:variables
}
