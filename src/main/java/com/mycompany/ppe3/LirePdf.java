/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ppe3;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KRONOTEK
 */
public class LirePdf {
    private static final String FILE__NAME = "/tmp/itext.pdf";

    public static void main(String[]args) {

        PdfReader reader;

        try {
            String sql = "select facture from bon_commande where idBonCommande = 1";
            ResultSet test = ConnexionBDD.getInstance().requeteSelection(sql);
            String pdf = null;
            try {
                test.next();
                pdf = test.getString(1);
            } catch (SQLException ex) {
                Logger.getLogger(LirePdf.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            reader = new PdfReader(pdf);

            String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);

            System.out.println(textFromPage);

            reader.close();

        } catch (IOException e) {
        }

    }
}
