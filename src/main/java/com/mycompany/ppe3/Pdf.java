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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 *
 * @author KRONOTEK
 */
public class Pdf {

    public static void main(String[]args) throws FileNotFoundException {
        writeUsingIText();
    }

    private static void writeUsingIText() throws FileNotFoundException {

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        FileOutputStream baos = new FileOutputStream(new File("facture.pdf"));

        try {

            PdfWriter.getInstance(document, baos);

           //open
            document.open();

            Paragraph p = new Paragraph();
            p.add("Facture");
            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            Paragraph p2 = new Paragraph();
            p2.add("Monsieur A");//no alignment

            document.add(p2);

            Paragraph p3 = new Paragraph();
            p3.add("Agent B");//no alignment

            document.add(p3);

            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);

            document.add(new Paragraph("Total de 0.0 €", f));

           //close
            document.close();
            
            byte[] pdf = baos.toByteArray();
            
            String sql = "insert into bon_commande values ('1','" + pdf + "',13)";
            ConnexionBDD.getInstance().requeteAction(sql);

        } catch (DocumentException e) {
            System.out.println("Erreur");
        }

    }
}


