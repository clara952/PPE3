/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ppe3;

import com.mycompany.ppe3.Tests.BDD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author c.bigeard
 */
public class DoubleListes extends javax.swing.JPanel {

    private static String nomList1;
    private static String nomList2;
    private static Integer fenetre;
    ImageIcon iconPlus;

    /**
     * Méthode qui retourne la fenêtre choisie (administrateur ou Agent)
     *
     * @return
     */
    public static Integer getFenetre() {
        return DoubleListes.fenetre;
    }

    /**
     * Méthode pour récupérer la fenêtre choisi selon numéro) et affiche les
     * bonnes informations dans les listes
     *
     * @param fenetre
     */
    public static void setFenetre(Integer fenetre) {
        DoubleListes.fenetre = fenetre;
        if (fenetre == 1) {
            setNomList1("categorie");
            setNomList2("produit");
        } else if (fenetre == 2) {
            setNomList1("profil");
            setNomList2("personnel");
        } else {
            setNomList1("personnel");
            setNomList2("vente");
        }
    }

    /**
     * Méthode retourne nom List1
     *
     * @return
     */
    public static String getNomList1() {
        return nomList1;
    }

    /**
     * Méthode récupère nom List1
     *
     * @param nomList1
     */
    public static void setNomList1(String nomList1) {
        DoubleListes.nomList1 = nomList1;
    }

    /**
     * Méthode retourne nom List2
     *
     * @return
     */
    public static String getNomList2() {
        return nomList2;
    }

    /**
     * Méthode qui récupère le nom List2
     *
     * @param nomList2
     */
    public static void setNomList2(String nomList2) {
        DoubleListes.nomList2 = nomList2;
    }

    /**
     * Returns an ImageIcon, or null if the path(chemin d'accès au fichier) was invalid.
     */
//    protected ImageIcon createImageIcon(String path,
//            String description) {
//        java.net.URL imgURL = getClass().getResource(path);
//        if (imgURL != null) {
//            return new ImageIcon(imgURL, description);
//        } else {
//            System.err.println("Couldn't find file: " + path);
//            return null;
//        }
//    }

    /**
     * Creates new form DoubleListes
     */
    public DoubleListes() {
//        iconPlus = createImageIcon("/images/plus.png", "Ajouter");
        initComponents();

        //jButtonAjouterList1.setIcon(iconPlus);


    }

    /**
     * Méthode afficher liste des catégories
     */
    public void list1() {
        String tuple;
        DefaultListModel leModel = (DefaultListModel) jList1.getModel();
        leModel.clear();
        //Afficher le choix "tous" pour séléctionner tous les produits
        if (getFenetre() != 3) {
            leModel.addElement("Tous");
        }
        try {
            String sql = "select * from " + getNomList1();
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            while (lesTuples.next()) {
                tuple = lesTuples.getString(2);
                if (getFenetre() == 3) {
                    tuple = tuple + " " + lesTuples.getString(3);
                }
                leModel.addElement(tuple);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Méthode qui retourne le nom et le prénom du personnel selon son id
     *
     * @param idPerso
     * @return
     */
    public String nomPerso(String idPerso) {
        String sql = "select nomPersonnel, prenomPersonnel from personnel where idPersonnel = " + idPerso;
        ResultSet recup = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            recup.next();
            idPerso = recup.getString(1) + " " + recup.getString(2);
        } catch (SQLException ex) {
            Logger.getLogger(DoubleListes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idPerso;
    }

    /**
     * Méthode qui retourne le nom et le prénom du client selon son id
     *
     * @param idClient
     * @return
     */
    public String nomClient(String idClient) {
        String sql = "select nomClient,prenomClient from client where idClient = " + idClient;
        ResultSet recup = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            recup.next();
            idClient = recup.getString(1) + " " + recup.getString(2);
        } catch (SQLException ex) {
            Logger.getLogger(DoubleListes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idClient;
    }

    /**
     * Méthode afficher liste des produits
     */
    public void list2() {
        String tuple = null;
        DefaultListModel leModel = (DefaultListModel) jList2.getModel();
        leModel.clear();
        try {
            String sql = "select * from " + getNomList2();
//            System.out.println(sql);
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            while (lesTuples.next()) {
                tuple = lesTuples.getString(2);
                if (getFenetre() == 2) {        //Pour le personnel afficher nom ET PRENOM
                    tuple = tuple + " " + lesTuples.getString(3);
                } else if (getFenetre() == 3) {
                    tuple = "Vente " + lesTuples.getString(1) + " du " + lesTuples.getString(2) + " pour " + nomClient(lesTuples.getString(3)) + " par " + nomPerso(lesTuples.getString(4));
                }
                leModel.addElement(tuple);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Méthode qui affiche dans la liste des produits seleument ceux qui
     * appartiennent à la catégorie séléctionnée dans la liste des catégories
     */
    public void list2parList1() {
        //Tester si sélection faite ou pas
        if (jList1.getSelectedValue() == null) {
            list2();
        } else {
            DefaultListModel listCategorie = (DefaultListModel) jList2.getModel();
            //Vider la liste pour pouvoir la remplir avec les bonnes informations
            listCategorie.clear();
            String tuple = null;
            String idList1;
            String libelleList1;
            try {
                //Récupérer le nom des colonnes 1 et 2 de la première table
                String recup = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + getNomList1() + "'";
                ResultSet recuperation = ConnexionBDD.getInstance().requeteSelection(recup);
                recuperation.next();
                idList1 = recuperation.getString(1);
                recuperation.next();
                libelleList1 = recuperation.getString(1);

                String sql = "select * from " + getNomList1() + ", " + getNomList2() + " where " + getNomList1() + "." + idList1 + " = " + getNomList2() + "." + idList1 + " and " + getNomList1() + "." + libelleList1 + " = '" + jList1.getSelectedValue() + "'";
                ResultSet nomCategorie = ConnexionBDD.getInstance().requeteSelection(sql);
                if (jList1.getSelectedIndex() == 0) {
                    list2();
                } else {
                    while (nomCategorie.next()) {
                        tuple = nomCategorie.getString(4);
                        if (getFenetre() == 2) {        //Pour le personnel afficher nom ET PRENOM
                            tuple = tuple + " " + nomCategorie.getString(5);
                        }
                        //Ajouter les éléments de la base de données un par un
                        listCategorie.addElement(tuple);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BDD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Méthode pour récupérer l'id du client sélectionné dans la liste client
     *
     * @return
     */
    public Integer recupererIdPersonnel() {
        Integer idPersonnel = 0;

        String personnel = getList2Selectionnee();
        String infosPersonnel[] = personnel.split(" ");

        String nomPersonnel = infosPersonnel[0];
        String prenomPersonnel = infosPersonnel[1];

        String sql = "select idPersonnel from personnel where  nomPersonnel = '" + nomPersonnel + "' and prenomPersonnel = '" + prenomPersonnel + "'";
        ResultSet tuple = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            tuple.next();
            idPersonnel = tuple.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return idPersonnel;
    }

    /**
     * Méthode pour récupérer l'id du client sélectionné dans la liste client
     *
     * @return
     */
    public Integer recupererIdProduit() {
        Integer idProduit = 0;
        String sql = "select idProduit from produit where  libelleProduit = '" + jList2.getSelectedValue() + "'";
        ResultSet tuple = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            tuple.next();
            idProduit = tuple.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return idProduit;
    }

    /**
     * Méthode qui retourn la valeur de la selection dans liste 1
     *
     * @return
     */
    public String getList1Selectionnee() {
        return jList1.getSelectedValue();
    }

    /**
     * Méthode qui retourn la valeur de la selection dans liste 2
     *
     * @return
     */
    public String getList2Selectionnee() {
        return jList2.getSelectedValue();
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
        jButtonAfficherList2 = new javax.swing.JButton();
        jButtonAjouterList2 = new javax.swing.JButton();
        jButtonModifierList2 = new javax.swing.JButton();
        jButtonAjouterList1 = new javax.swing.JButton();
        jButtonModifierList1 = new javax.swing.JButton();
        jButtonSupprimerList1 = new javax.swing.JButton();
        jButtonSupprimerList2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButtonAfficherList2.setBackground(new java.awt.Color(255, 255, 255));
        jButtonAfficherList2.setForeground(new java.awt.Color(51, 51, 51));
        jButtonAfficherList2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/oeil.png"))); // NOI18N
        jButtonAfficherList2.setBorder(null);
        jButtonAfficherList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAfficherList2MouseClicked(evt);
            }
        });

        jButtonAjouterList2.setBackground(new java.awt.Color(255, 255, 255));
        jButtonAjouterList2.setForeground(new java.awt.Color(51, 51, 51));
        jButtonAjouterList2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/plus.png"))); // NOI18N
        jButtonAjouterList2.setToolTipText("");
        jButtonAjouterList2.setBorder(null);
        jButtonAjouterList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAjouterList2MouseClicked(evt);
            }
        });
        jButtonAjouterList2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterList2ActionPerformed(evt);
            }
        });

        jButtonModifierList2.setBackground(new java.awt.Color(255, 255, 255));
        jButtonModifierList2.setForeground(new java.awt.Color(51, 51, 51));
        jButtonModifierList2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modifier.jpg"))); // NOI18N
        jButtonModifierList2.setBorder(null);
        jButtonModifierList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonModifierList2MouseClicked(evt);
            }
        });

        jButtonAjouterList1.setBackground(new java.awt.Color(255, 255, 255));
        jButtonAjouterList1.setForeground(new java.awt.Color(51, 51, 51));
        jButtonAjouterList1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/plus.png"))); // NOI18N
        jButtonAjouterList1.setBorder(null);
        jButtonAjouterList1.setMaximumSize(new java.awt.Dimension(239, 227));
        jButtonAjouterList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAjouterList1MouseClicked(evt);
            }
        });

        jButtonModifierList1.setBackground(new java.awt.Color(255, 255, 255));
        jButtonModifierList1.setForeground(new java.awt.Color(51, 51, 51));
        jButtonModifierList1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modifier.jpg"))); // NOI18N
        jButtonModifierList1.setBorder(null);
        jButtonModifierList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonModifierList1MouseClicked(evt);
            }
        });

        jButtonSupprimerList1.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSupprimerList1.setForeground(new java.awt.Color(51, 51, 51));
        jButtonSupprimerList1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/supprimer.png"))); // NOI18N
        jButtonSupprimerList1.setBorder(null);

        jButtonSupprimerList2.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSupprimerList2.setForeground(new java.awt.Color(51, 51, 51));
        jButtonSupprimerList2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/supprimer.png"))); // NOI18N
        jButtonSupprimerList2.setBorder(null);
        jButtonSupprimerList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSupprimerList2MouseClicked(evt);
            }
        });

        jList1.setModel(new DefaultListModel());
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(jList2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonAjouterList1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonModifierList1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSupprimerList1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonAjouterList2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonModifierList2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSupprimerList2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAfficherList2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonSupprimerList2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAjouterList2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSupprimerList1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonModifierList1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAjouterList1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonModifierList2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAfficherList2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Bouton ajouter au niveau de la liste 2 (produit ou personnel)
     *
     * @param evt
     */
    private void jButtonAjouterList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAjouterList2MouseClicked
        if (getFenetre() == 1) {        //Listes Categorie et Produit
            FenetreProduit fenetreProduit = new FenetreProduit();
            fenetreProduit.setFenetre(3);
            fenetreProduit.show();
        } else {                          //Listes Profil et Personnel
            FenetrePersonnel fenetrePersonnel = new FenetrePersonnel();
            fenetrePersonnel.setFenetre(3);
            fenetrePersonnel.show();
        }
    }//GEN-LAST:event_jButtonAjouterList2MouseClicked

    private void jButtonAjouterList2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterList2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAjouterList2ActionPerformed
    /**
     * Bouton ajouter au niveau de la liste 1 (categorie ou profil)
     *
     * @param evt
     */
    private void jButtonAjouterList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAjouterList1MouseClicked
        if (getFenetre() == 1) {        //Listes Categorie et Produit
            String nomCategorie = JOptionPane.showInputDialog("Ajouter une catégorie");

            if (nomCategorie.length() != 0) {
                String sql = "insert into categorie values (null, '" + nomCategorie + "')";
                ConnexionBDD.getInstance().requeteAction(sql);
            }
        } else {                          //Listes Profil et Personnel
            String nomProfil = JOptionPane.showInputDialog("Ajouter un profil");

            if (nomProfil.length() != 0) {
                String sql = "insert into profil values (null, '" + nomProfil + "')";
                ConnexionBDD.getInstance().requeteAction(sql);
            }
        }
    }//GEN-LAST:event_jButtonAjouterList1MouseClicked
    /**
     * Bouton modifier, dans liste 1, me nom sélectionné
     *
     * @param evt
     */
    private void jButtonModifierList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonModifierList1MouseClicked
        if (jList1.getSelectedValue() != null) {
            if (getFenetre() == 1) {        //Listes Categorie et Produit
                String nomNewCategorie = JOptionPane.showInputDialog("Changer le nom de la catégorie '" + getList1Selectionnee() + "'");

                if (nomNewCategorie.length() != 0) {
                    String sql = "update categorie set libelleCategorie = '" + nomNewCategorie + "' where libelleCategorie = '" + getList1Selectionnee() + "'";
                    ConnexionBDD.getInstance().requeteAction(sql);
                }
            } else {                          //Listes Profil et Personnel
                String nomProfil = JOptionPane.showInputDialog("Changer le nom du profil '" + getList1Selectionnee() + "'");

                if (nomProfil.length() != 0) {
                    String sql = "update profil set libelleProfil = '" + nomProfil + "' where libelleProfil = '" + getList1Selectionnee() + "'";
                    ConnexionBDD.getInstance().requeteAction(sql);
                }
            }
        }
    }//GEN-LAST:event_jButtonModifierList1MouseClicked
    /**
     * Action : quand une catégorie est sélectionnée (dans la liste catégorie)
     * alors seuls les produits étant de cette catégorie seront afficher dans
     * liste produit
     *
     * @param evt
     */
    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        list2parList1();
    }//GEN-LAST:event_jList1MouseClicked
    /**
     * Bouton pour modifier les informations de la sélection dans la liste 2
     * (produit ou personnel)
     *
     * @param evt
     */
    private void jButtonModifierList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonModifierList2MouseClicked
        if (jList2.getSelectedValue() != null) {
            if (getFenetre() == 1) {        //Listes Categorie et Produit
                FenetreProduit fenetreProduit = new FenetreProduit();
                fenetreProduit.setFenetre(2);
                fenetreProduit.setIdProduit(recupererIdProduit());
                fenetreProduit.show();
            } else {                          //Listes Profil et Personnel
                FenetrePersonnel fenetrePersonnel = new FenetrePersonnel();
                fenetrePersonnel.setFenetre(2);
                fenetrePersonnel.setIdPersonnel(recupererIdPersonnel());
                fenetrePersonnel.show();
            }
        }
    }//GEN-LAST:event_jButtonModifierList2MouseClicked
    /**
     * Bouton qui supprimer le produit/personnel selectionné dans la liste 2
     *
     * @param evt
     */
    private void jButtonSupprimerList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSupprimerList2MouseClicked
        if (jList2.getSelectedValue() != null) {

            int confirmerSupp = JOptionPane.showConfirmDialog(this, "Voulez-vous continuer ?", "Et maintenant …", JOptionPane.YES_NO_OPTION);

            if (confirmerSupp == 0) {
                if (getFenetre() == 1) {        //Listes Categorie et Produit
                    String sql = "delete from produit where idProduit = '" + recupererIdProduit() + "'";
                    ConnexionBDD.getInstance().requeteAction(sql);
                } else {                          //Listes Profil et Personnel
                    String sql = "delete from personnel where idPersonnel = '" + recupererIdPersonnel() + "'";
                    ConnexionBDD.getInstance().requeteAction(sql);
                }
            }
        }
    }//GEN-LAST:event_jButtonSupprimerList2MouseClicked
    /**
     * Bouton pour afficher les informations du produit/personnel sélectionné
     * dans la liste 2
     *
     * @param evt
     */
    private void jButtonAfficherList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAfficherList2MouseClicked
        if (jList2.getSelectedValue() != null) {
            if (getFenetre() == 1) {        //Listes Categorie et Produit
                FenetreProduit fenetreProduit = new FenetreProduit();
                fenetreProduit.setFenetre(1);
                fenetreProduit.setIdProduit(recupererIdProduit());
                fenetreProduit.show();
            } else {                          //Listes Profil et Personnel
                FenetrePersonnel fenetrePersonnel = new FenetrePersonnel();
                fenetrePersonnel.setFenetre(1);
                fenetrePersonnel.setIdPersonnel(recupererIdPersonnel());
                fenetrePersonnel.show();
            }
        }
    }//GEN-LAST:event_jButtonAfficherList2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAfficherList2;
    private javax.swing.JButton jButtonAjouterList1;
    private javax.swing.JButton jButtonAjouterList2;
    private javax.swing.JButton jButtonModifierList1;
    private javax.swing.JButton jButtonModifierList2;
    private javax.swing.JButton jButtonSupprimerList1;
    private javax.swing.JButton jButtonSupprimerList2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
