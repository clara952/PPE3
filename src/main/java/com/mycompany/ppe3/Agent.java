/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ppe3;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author c.bigeard
 */
public class Agent extends javax.swing.JFrame {
    
    ConnexionBDD bdd = ConnexionBDD.getInstance();
    DefaultTableModel model;
    
    public static Integer idAgent;
    
    /**
     * Creates new form Agent
     */
    public Agent() {
        initComponents();
        
        model = (DefaultTableModel) jTable1.getModel();
        model.addColumn("produit");
        model.addColumn("prix unitaire");
        model.addColumn("quantité");
        model.addColumn("prix total");
    }
    /**
     * Méthode retourn idAgent
     * @return 
     */
    public Integer getIdAgent() {
        return this.idAgent;
    }
    /**
     * Méthode récupère idAgent
     * @param id 
     */
    public void setIdAgent(Integer id) {
        this.idAgent = id;
    }
    
    /**
     * Méthode pour afficher liste des clients
     */
    public void listClient(){
            String nom;
            String prenom;
            DefaultListModel leModel = (DefaultListModel)jListClient.getModel();
            leModel.clear();
            
            String sql = "select * from client";
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            try { 
                while (lesTuples.next()) {
                    nom = lesTuples.getString(2);
                    prenom = lesTuples.getString(3);
                    leModel.addElement(nom + " " + prenom);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    /**
     * Méthode afficher liste des clients
     */
    public void listStock(){
            String libelle;
            String stock;
            DefaultListModel leModel = (DefaultListModel)jListStock.getModel();
            leModel.clear();
            
            String sql = "select libelleProduit, stock from produit where stock <= 1";
            ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
            try { 
                while (lesTuples.next()) {
                    libelle = lesTuples.getString(1);
                    stock = lesTuples.getString(2);
                    leModel.addElement(libelle + " (" + stock + " en stock)");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    /**
     * Méthode pour récupérer l'id du client sélectionné dans la liste client
     * @return 
     */
    public Integer recupererIdClientList(){
        Integer idClient = 0;
        
        String client = jListClient.getSelectedValue();
        String infosClient[] = client.split(" ");
        
        String nomClient = infosClient[0];
        String prenomClient = infosClient[1];
        
        String sql = "select idClient from client where  nomClient = '" + nomClient + "' and prenomClient = '" + prenomClient + "'";
        ResultSet tuple = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            tuple.next();
            idClient = tuple.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return idClient;
    }
    /**
     * Méthode pour qui retourne le nom et le prénom du personnel actuellement connecté
     * @return 
     */
    public String nomAgent(){
        Integer idAgent = getIdAgent();
        String nomAgent = null;
        
        String sql = "select nomPersonnel, prenomPersonnel from Personnel where  idPersonnel = '" + idAgent + "'";
        ResultSet tuple = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            tuple.next();
            nomAgent = tuple.getString(1) + " " + tuple.getString(2);
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nomAgent;
    }
    /**
     * Méthode qui Complète la combo box client avec le nom de tous les clients
     */
    public void comboBoxClient(){
        String nom;
        String prenom;
        DefaultComboBoxModel leModel = (DefaultComboBoxModel)jComboBoxClient.getModel();
            
        String sql = "select * from client";
        ResultSet lesTuples = ConnexionBDD.getInstance().requeteSelection(sql);
        try { 
            while (lesTuples.next()) {
                nom = lesTuples.getString(2);
                prenom = lesTuples.getString(3);
                leModel.addElement(nom + " " + prenom);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    /**
     * Méthode qui retourne l'id du client sélectionné dans la combo box
     * @return 
     */
    public Integer recupererIdClientComboBox(){
        Integer idClient = 0;
        
        String client = jComboBoxClient.getSelectedItem().toString();
        String infosClient[] = client.split(" ");
        
        String nomClient = infosClient[0];
        String prenomClient = infosClient[1];
        
        String sql = "select idClient from client where  nomClient = '" + nomClient + "' and prenomClient = '" + prenomClient + "'";
        ResultSet tuple = ConnexionBDD.getInstance().requeteSelection(sql);
        try {
            tuple.next();
            idClient = tuple.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return idClient;
    }
    /**
     * Méthode pour écrire une facture
     * @param idFacture
     * @param nomAgent
     * @param nomClient
     * @param total 
     */
    public void ecrireFacturePdf(String idFacture, String nomAgent,String nomClient, String total){
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        FileOutputStream baos = new FileOutputStream(new File("facture.pdf"));

        try {

            PdfWriter.getInstance(document, baos);

           //open
            document.open();

            Paragraph p = new Paragraph();
            p.add("Facture n°" + idFacture);
            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            Paragraph p2 = new Paragraph();
            p2.add("Client : " + nomClient);//no alignment

            document.add(p2);

            Paragraph p3 = new Paragraph();
            p3.add("Agent : " + nomAgent);//no alignment

            document.add(p3);

            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);

            document.add(new Paragraph("Total de " + total + " €", f));

           //close
            document.close();
            
            byte[] pdf = baos.toByteArray();
            
            String sql = "update bon_commande set facture = '" + pdf + "' where idBonCommande = " + idFacture;
            ConnexionBDD.getInstance().requeteAction(sql);

        } catch (DocumentException e) {
            System.out.println("Erreur");
        }
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
        jPanel3 = new javax.swing.JPanel();
        jLabelTitre = new javax.swing.JLabel();
        jTabbedPaneAgent = new javax.swing.JTabbedPane();
        jPanelProduit = new javax.swing.JPanel();
        doubleListesProduit = new com.mycompany.ppe3.DoubleListes();
        jPanelClient = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListClient = new javax.swing.JList<>();
        jButtonAfficherClient = new javax.swing.JButton();
        jButtonModifierClient = new javax.swing.JButton();
        jButtonCréerClient = new javax.swing.JButton();
        jButtonSupprimerClient = new javax.swing.JButton();
        jPanelVente = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelPersonnel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        categorieEtProduit1 = new com.mycompany.ppe3.CategorieEtProduit();
        jButtonSupprimerPanier = new javax.swing.JButton();
        jButtonAjouterPanier = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxClient = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabelPrixPanier = new javax.swing.JLabel();
        jPanelAdministrateur = new javax.swing.JPanel();
        doubleListesVente = new com.mycompany.ppe3.DoubleListes();
        jPanelAgents = new javax.swing.JPanel();
        doubleListesPersonnel = new com.mycompany.ppe3.DoubleListes();
        jPanelStatistiques = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListStock = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jButtonProduits = new javax.swing.JButton();
        jButtonClients = new javax.swing.JButton();
        jButtonVente = new javax.swing.JButton();
        jButtonAgents = new javax.swing.JButton();
        jButtonStatistiques = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestionnaire pour agent");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabelTitre.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelTitre.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitre.setText("Produits");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(542, Short.MAX_VALUE)
                .addComponent(jLabelTitre)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabelTitre)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 720, 130));

        jTabbedPaneAgent.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPaneAgent.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPaneAgent.setEnabled(false);
        jTabbedPaneAgent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPaneAgentMouseClicked(evt);
            }
        });

        jPanelProduit.setBackground(new java.awt.Color(255, 255, 255));
        jPanelProduit.setEnabled(false);

        javax.swing.GroupLayout jPanelProduitLayout = new javax.swing.GroupLayout(jPanelProduit);
        jPanelProduit.setLayout(jPanelProduitLayout);
        jPanelProduitLayout.setHorizontalGroup(
            jPanelProduitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProduitLayout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addComponent(doubleListesProduit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        jPanelProduitLayout.setVerticalGroup(
            jPanelProduitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProduitLayout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(doubleListesProduit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        jTabbedPaneAgent.addTab("Produit", jPanelProduit);

        jPanelClient.setBackground(new java.awt.Color(255, 255, 255));

        jListClient.setBackground(new java.awt.Color(255, 255, 255));
        jListClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jListClient.setModel(new DefaultListModel());
        jScrollPane4.setViewportView(jListClient);

        jButtonAfficherClient.setBackground(new java.awt.Color(255, 255, 255));
        jButtonAfficherClient.setForeground(new java.awt.Color(51, 51, 51));
        jButtonAfficherClient.setIcon(new javax.swing.ImageIcon("F:\\SIO2\\Programmation\\PPE3\\src\\main\\java\\images\\oeil.png")); // NOI18N
        jButtonAfficherClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButtonAfficherClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAfficherClientMouseClicked(evt);
            }
        });

        jButtonModifierClient.setBackground(new java.awt.Color(255, 255, 255));
        jButtonModifierClient.setForeground(new java.awt.Color(51, 51, 51));
        jButtonModifierClient.setIcon(new javax.swing.ImageIcon("F:\\SIO2\\Programmation\\PPE3\\src\\main\\java\\images\\modifier.jpg")); // NOI18N
        jButtonModifierClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButtonModifierClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonModifierClientMouseClicked(evt);
            }
        });

        jButtonCréerClient.setBackground(new java.awt.Color(255, 255, 255));
        jButtonCréerClient.setForeground(new java.awt.Color(51, 51, 51));
        jButtonCréerClient.setIcon(new javax.swing.ImageIcon("F:\\SIO2\\Programmation\\PPE3\\src\\main\\java\\images\\plus.png")); // NOI18N
        jButtonCréerClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButtonCréerClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCréerClientMouseClicked(evt);
            }
        });

        jButtonSupprimerClient.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSupprimerClient.setForeground(new java.awt.Color(51, 51, 51));
        jButtonSupprimerClient.setIcon(new javax.swing.ImageIcon("F:\\SIO2\\Programmation\\PPE3\\src\\main\\java\\images\\supprimer.png")); // NOI18N
        jButtonSupprimerClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButtonSupprimerClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSupprimerClientMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelClientLayout = new javax.swing.GroupLayout(jPanelClient);
        jPanelClient.setLayout(jPanelClientLayout);
        jPanelClientLayout.setHorizontalGroup(
            jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanelClientLayout.createSequentialGroup()
                        .addComponent(jButtonCréerClient, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonModifierClient, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSupprimerClient, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                        .addComponent(jButtonAfficherClient, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelClientLayout.setVerticalGroup(
            jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonCréerClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAfficherClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSupprimerClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonModifierClient))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPaneAgent.addTab("Client", jPanelClient);

        jPanelVente.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setEnabled(false);

        jPanelPersonnel.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPersonnel.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Confirmer");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new DefaultTableModel()
        );
        jScrollPane3.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jButtonSupprimerPanier.setText("Supprimer");
        jButtonSupprimerPanier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSupprimerPanierMouseClicked(evt);
            }
        });

        jButtonAjouterPanier.setText("Ajouter");
        jButtonAjouterPanier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAjouterPanierMouseClicked(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Client");

        jComboBoxClient.setModel(new DefaultComboBoxModel());

        jLabel10.setText("Total :");

        jLabelPrixPanier.setText("0.0");

        javax.swing.GroupLayout jPanelPersonnelLayout = new javax.swing.GroupLayout(jPanelPersonnel);
        jPanelPersonnel.setLayout(jPanelPersonnelLayout);
        jPanelPersonnelLayout.setHorizontalGroup(
            jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPersonnelLayout.createSequentialGroup()
                .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelPersonnelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxClient, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(categorieEtProduit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPersonnelLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelPrixPanier, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPersonnelLayout.createSequentialGroup()
                        .addComponent(jButtonAjouterPanier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSupprimerPanier))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanelPersonnelLayout.setVerticalGroup(
            jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPersonnelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(categorieEtProduit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelPersonnelLayout.createSequentialGroup()
                        .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonSupprimerPanier)
                            .addComponent(jButtonAjouterPanier))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPrixPanier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelPersonnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jLabel10)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Agent", jPanelPersonnel);

        jPanelAdministrateur.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAdministrateur.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanelAdministrateurLayout = new javax.swing.GroupLayout(jPanelAdministrateur);
        jPanelAdministrateur.setLayout(jPanelAdministrateurLayout);
        jPanelAdministrateurLayout.setHorizontalGroup(
            jPanelAdministrateurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAdministrateurLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(doubleListesVente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanelAdministrateurLayout.setVerticalGroup(
            jPanelAdministrateurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAdministrateurLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(doubleListesVente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jTabbedPane1.addTab("Admin", jPanelAdministrateur);

        javax.swing.GroupLayout jPanelVenteLayout = new javax.swing.GroupLayout(jPanelVente);
        jPanelVente.setLayout(jPanelVenteLayout);
        jPanelVenteLayout.setHorizontalGroup(
            jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVenteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanelVenteLayout.setVerticalGroup(
            jPanelVenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVenteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addGap(236, 236, 236))
        );

        jTabbedPaneAgent.addTab("Vente", jPanelVente);

        jPanelAgents.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanelAgentsLayout = new javax.swing.GroupLayout(jPanelAgents);
        jPanelAgents.setLayout(jPanelAgentsLayout);
        jPanelAgentsLayout.setHorizontalGroup(
            jPanelAgentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAgentsLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(doubleListesPersonnel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanelAgentsLayout.setVerticalGroup(
            jPanelAgentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAgentsLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(doubleListesPersonnel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jTabbedPaneAgent.addTab("Produit", jPanelAgents);

        jPanelStatistiques.setBackground(new java.awt.Color(255, 255, 255));
        jPanelStatistiques.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jListStock.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(jListStock);

        jPanelStatistiques.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 610, 100));

        jTabbedPaneAgent.addTab("Produit", jPanelStatistiques);

        jPanel1.add(jTabbedPaneAgent, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 650, 380));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jButtonProduits.setBackground(new java.awt.Color(102, 102, 102));
        jButtonProduits.setText("Produits");
        jButtonProduits.setBorder(null);
        jButtonProduits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonProduitsMouseClicked(evt);
            }
        });

        jButtonClients.setBackground(new java.awt.Color(102, 102, 102));
        jButtonClients.setText("Clients");
        jButtonClients.setBorder(null);
        jButtonClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonClientsMouseClicked(evt);
            }
        });

        jButtonVente.setBackground(new java.awt.Color(102, 102, 102));
        jButtonVente.setText("Vente");
        jButtonVente.setBorder(null);
        jButtonVente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonVenteMouseClicked(evt);
            }
        });

        jButtonAgents.setBackground(new java.awt.Color(102, 102, 102));
        jButtonAgents.setText("Agents");
        jButtonAgents.setBorder(null);
        jButtonAgents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAgentsMouseClicked(evt);
            }
        });

        jButtonStatistiques.setBackground(new java.awt.Color(102, 102, 102));
        jButtonStatistiques.setText("Statistiques");
        jButtonStatistiques.setBorder(null);
        jButtonStatistiques.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonStatistiquesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButtonProduits, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonClients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonVente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonAgents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonStatistiques, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jButtonProduits, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonClients, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonVente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAgents, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonStatistiques, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 450));

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

        setSize(new java.awt.Dimension(873, 489));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Button pour ouvrire la fênetre pour créer un client
     * @param evt 
     */
    private void jButtonCréerClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCréerClientMouseClicked
        FenetreClient pageFenetreClient = new FenetreClient();
        pageFenetreClient.setFenetre(3);
        pageFenetreClient.show();
    }//GEN-LAST:event_jButtonCréerClientMouseClicked

    private void jTabbedPaneAgentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPaneAgentMouseClicked
        
    }//GEN-LAST:event_jTabbedPaneAgentMouseClicked
    
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
//        doubleListesPersonnel.list1();
//        doubleListesProduit.list1();
//        
//        doubleListesPersonnel.list2();
//        doubleListesProduit.list2();        
    }//GEN-LAST:event_formWindowActivated
    /**
     * Bouton pour afficher la fênetre d'affichage des informations du client
     * @param evt 
     */
    private void jButtonAfficherClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAfficherClientMouseClicked
        if (jListClient.getSelectedValue() != null) {
            FenetreClient pageFenetreClient = new FenetreClient();
                pageFenetreClient.setFenetre(1);
                pageFenetreClient.setIdClient(recupererIdClientList());
                pageFenetreClient.show();
        }
        
    }//GEN-LAST:event_jButtonAfficherClientMouseClicked
    /**
     * Bouton pour afficher la fênetre de modification du client sélectionné dans la liste client
     * @param evt 
     */
    private void jButtonModifierClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonModifierClientMouseClicked
        if (jListClient.getSelectedValue() != null) {
            FenetreClient pageFenetreClient = new FenetreClient();
                pageFenetreClient.setFenetre(2);
                pageFenetreClient.setIdClient(recupererIdClientList());
                pageFenetreClient.show();
        }    
    }//GEN-LAST:event_jButtonModifierClientMouseClicked
    /**
     * Bouton pour afficher panel Produit du TabbedPanel
     * @param evt 
     */
    private void jButtonProduitsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonProduitsMouseClicked
        jTabbedPaneAgent.setSelectedIndex(0);
        jLabelTitre.setText("Produits");
        
        doubleListesProduit.setFenetre(1);
    }//GEN-LAST:event_jButtonProduitsMouseClicked
    /**
     * Bouton pour afficher panel Client du TabbedPanel
     * @param evt 
     */
    private void jButtonClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonClientsMouseClicked
        jTabbedPaneAgent.setSelectedIndex(1);
        jLabelTitre.setText("Clients");
    }//GEN-LAST:event_jButtonClientsMouseClicked
    /**
     * Bouton pour afficher panel Vente du TabbedPanel
     * @param evt 
     */
    private void jButtonVenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonVenteMouseClicked
        jTabbedPaneAgent.setSelectedIndex(2);
        jLabelTitre.setText("Vente");
        doubleListesProduit.setFenetre(3);
        categorieEtProduit1.listCategorie();
        categorieEtProduit1.listProduit();
        comboBoxClient();
    }//GEN-LAST:event_jButtonVenteMouseClicked
    /**
     * Bouton pour afficher panel Agent du TabbedPanel
     * @param evt 
     */
    private void jButtonAgentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAgentsMouseClicked
        jTabbedPaneAgent.setSelectedIndex(3);
        jLabelTitre.setText("Agents");
        
        doubleListesProduit.setFenetre(2);
    }//GEN-LAST:event_jButtonAgentsMouseClicked
    /**
     * Bouton pour afficher panel Satistique du TabbedPanel
     * @param evt 
     */
    private void jButtonStatistiquesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonStatistiquesMouseClicked
        jTabbedPaneAgent.setSelectedIndex(4);
        jLabelTitre.setText("Statistiques");
        
    }//GEN-LAST:event_jButtonStatistiquesMouseClicked
    
   /**
     * Bouton pour supprimer le client selectionné, affichage d'une boite de dialogue demandant confirmation de la suppression du client
     * @param evt 
     */
    private void jButtonSupprimerClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSupprimerClientMouseClicked
        if (jListClient.getSelectedValue() != null) {

            int confirmerSupp = JOptionPane.showConfirmDialog(this, "Voulez-vous continuer ?", "Et maintenant …", JOptionPane.YES_NO_OPTION);
        
            if (confirmerSupp == 0) {
                String sql = "delete from client where idClient = '" + recupererIdClientList() + "'";
                ConnexionBDD.getInstance().requeteAction(sql);
            }
        }
        //Sinon la fênetre se ferme et la suppression du client n'est pas fait
    }//GEN-LAST:event_jButtonSupprimerClientMouseClicked
    /**
     * Action à faire lors de l'ouverture de la fenêtre
     * @param evt 
     */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        listClient();
        listStock();
        
        doubleListesPersonnel.setFenetre(2);
        doubleListesPersonnel.list1();
        doubleListesPersonnel.list2();
        
        doubleListesProduit.setFenetre(1);
        doubleListesProduit.list1();
        doubleListesProduit.list2();
        
        doubleListesVente.setFenetre(3);
        doubleListesVente.list1();
        doubleListesVente.list2();
        
        if(getIdAgent() == 2){            
            jButtonSupprimerClient.setVisible(false);
            
            jButtonAgents.setVisible(false);
            jPanelAgents.setVisible(false);
            jButtonStatistiques.setVisible(false);
            jPanelStatistiques.setVisible(false);
        }else{
            jTabbedPane1.setSelectedIndex(1);
        }        
    }//GEN-LAST:event_formWindowOpened
    /**
     * Bouton pour ajouter le produit selectionné dans la liste, vers le panier du client
     * @param evt 
     */
    private void jButtonAjouterPanierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAjouterPanierMouseClicked
        if (categorieEtProduit1.getProduitSelectionne() != null) {
            try {
                String produit = categorieEtProduit1.getProduitSelectionne();
                String prixTotal;
                Float prixpanier = Float.parseFloat(jLabelPrixPanier.getText());
                
                String sql = "select prix from produit where libelleProduit = '" + produit + "'";
                ResultSet tuple = ConnexionBDD.getInstance().requeteSelection(sql);
                tuple.next();
                String prix = tuple.getString(1);
                
                String quantite = JOptionPane.showInputDialog(this, "Quelle quantité ?", "1"); 
                
                if (Integer.parseInt(quantite) > 1) {
                    prixTotal = String.valueOf(Integer.parseInt(quantite) * Float.parseFloat(prix));
                }else{
                    prixTotal = prix;
                }
                String[] rowData = {produit,prix, quantite, prixTotal};
                model.addRow(rowData);
                //Ajouter l'affichage du prix total de tous les articles
                prixpanier = prixpanier + Float.parseFloat(prixTotal);
                jLabelPrixPanier.setText(String.valueOf(prixpanier));
            } catch (SQLException ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonAjouterPanierMouseClicked
    /**
     * Bouton pour suprimer le produit selectionné dans le panier
     * @param evt 
     */
    private void jButtonSupprimerPanierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSupprimerPanierMouseClicked
        if (jTable1.getSelectedRow() != -1) {
            Float prixpanier = Float.parseFloat(jLabelPrixPanier.getText());
            Integer numeroLigne = jTable1.getSelectedRow();
            Float prixRetire = Float.parseFloat((String) jTable1.getModel().getValueAt(numeroLigne, 3));
            prixpanier = prixpanier - prixRetire;
            jLabelPrixPanier.setText(String.valueOf(prixpanier));
            
            
            model.removeRow(jTable1.getSelectedRow());
        }
    }//GEN-LAST:event_jButtonSupprimerPanierMouseClicked
    /**
     * Bouton pour confirmer l'achat des produit actuellement dans le panier, par le client afficher dans la conbo box
     * @param evt 
     */
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        Integer idClient = recupererIdClientComboBox();
        Integer idPersonnel = getIdAgent();
        String idVente = null;
        
        String sql = "insert into vente values (null, DATE(NOW())," + idClient + "," + idPersonnel + ")";
        ConnexionBDD.getInstance().requeteAction(sql);
        //Récupérer idVente créer pour créer la suite
        String sql2 = "select idVente from vente where dateVente = DATE(NOW()) and idClient = " + idClient + " and idPersonnel = " + idPersonnel;
        ResultSet recupIdVente = ConnexionBDD.getInstance().requeteSelection(sql2);
        try {
            recupIdVente.next();
            idVente = recupIdVente.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Créer un bon de commande automatiquement
        if (idVente != null) {
            String sql3 = "insert into bon_commande values (null,null, '" + idVente + "')";
            ConnexionBDD.getInstance().requeteAction(sql3);     
            
            ecrireFacturePdf(idVente, nomAgent(), jComboBoxClient.getSelectedItem().toString(), jLabelPrixPanier.getText());
        }
        //Maintenant ils faut compléter la commande avec les produit et leurs quantité
        for (int i  = 0; i  < jTable1.getRowCount(); i ++) {
            //récupérer le nom du produit
            String produit = (String) jTable1.getModel().getValueAt(i, 0);
            String sql4 = "select idProduit from produit where libelleProduit = '" + produit + "'";
            ResultSet recupProduit = ConnexionBDD.getInstance().requeteSelection(sql4);
            try {
                recupProduit.next();
                produit = recupProduit.getString(1);
            } catch (SQLException ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //récupérer la quantité
            String quantite = (String) jTable1.getModel().getValueAt(i, 2);
            
            String sql5 = "insert into comprendre values ('" + produit + "','" + idVente + "','" + quantite + "')";
            ConnexionBDD.getInstance().requeteAction(sql5);
        }
    }//GEN-LAST:event_jButton1MouseClicked

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
            java.util.logging.Logger.getLogger(Agent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Agent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Agent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Agent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Agent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mycompany.ppe3.CategorieEtProduit categorieEtProduit1;
    private com.mycompany.ppe3.DoubleListes doubleListesPersonnel;
    private com.mycompany.ppe3.DoubleListes doubleListesProduit;
    private com.mycompany.ppe3.DoubleListes doubleListesVente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAfficherClient;
    private javax.swing.JButton jButtonAgents;
    private javax.swing.JButton jButtonAjouterPanier;
    private javax.swing.JButton jButtonClients;
    private javax.swing.JButton jButtonCréerClient;
    private javax.swing.JButton jButtonModifierClient;
    private javax.swing.JButton jButtonProduits;
    private javax.swing.JButton jButtonStatistiques;
    private javax.swing.JButton jButtonSupprimerClient;
    private javax.swing.JButton jButtonSupprimerPanier;
    private javax.swing.JButton jButtonVente;
    private javax.swing.JComboBox<String> jComboBoxClient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabelPrixPanier;
    private javax.swing.JLabel jLabelTitre;
    private javax.swing.JList<String> jListClient;
    private javax.swing.JList<String> jListStock;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelAdministrateur;
    private javax.swing.JPanel jPanelAgents;
    private javax.swing.JPanel jPanelClient;
    private javax.swing.JPanel jPanelPersonnel;
    private javax.swing.JPanel jPanelProduit;
    private javax.swing.JPanel jPanelStatistiques;
    private javax.swing.JPanel jPanelVente;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPaneAgent;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    
}
