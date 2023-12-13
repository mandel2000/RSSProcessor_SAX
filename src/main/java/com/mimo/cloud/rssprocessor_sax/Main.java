/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_sax;

import com.mimo.cloud.rssprocessor_sax.model.Canal;
import com.mimo.cloud.rssprocessor_sax.model.Noticia;
import com.mimo.cloud.rssprocessor_sax.utils.Constants;
import com.mimo.cloud.rssprocessor_sax.utils.XmlSaxUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Manuel Delgado Sánchez
 */
public class Main {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        System.out.println("Comienza el programa");

        Canal canal = Canal.fromXml(getFromUrl(Constants.url));

        if (canal != null) {

            showChannelInfo(canal);

            //writeXmlFile(canal, Constants.xmlFilePath);
            canal.toXml();

            writeJsonFile(canal, Constants.jsonFilePath);

        } else {
            System.out.println("La respuesta recibida no contiene un nodo 'noticias'.");
        }
    }

    private static void showChannelInfo(Canal canal) {

        System.out.println("\n  Información del Canal ");
        System.out.println(" ----------------------- ");
        System.out.println("  Título: " + canal.getTitulo());
        System.out.println("  URL: " + canal.getUrl());
        System.out.println("  Descripción: " + canal.getDescripcion());
        System.out.println("  Noticias: ");

        int i = 0;

        for (Noticia noticia : canal.getNoticias()) {
            System.out.println("\n\t Noticia " + ++i);
            System.out.println("\t| Titulo: " + noticia.getTitulo());
            System.out.println("\t| URL: " + noticia.getUrl());
            System.out.println("\t| Descripción: " + noticia.getDescripcion());
            System.out.println("\t| Fecha de publicación: " + noticia.getFecha_publicacion());
            System.out.println("\t| Categoría: " + noticia.getCategoría());
        }

    }

    private static void writeXmlFile(Canal canal, String xmlFilePath) throws SAXException, IOException, TransformerException, ParserConfigurationException {

        XmlSaxUtils.writeXmlToFile(canal.toXml(), String.format(Constants.xmlFilePath, canal.getTitulo()));

    }

    private static void writeJsonFile(Canal canal, String jsonFilePath) {

        try {
            String content = canal.toJson().toString(4);
            String fileName = String.format(jsonFilePath, canal.getTitulo());

            FileUtils.writeStringToFile(new File(fileName), content, "UTF-8");

        } catch (IOException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, "Error creating json file", ex);
        }

    }

    private static String getFromUrl(String urlAsString) throws IOException {

        StringBuilder body = new StringBuilder();

        URL url = new URL(urlAsString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int status = conn.getResponseCode();

        try ( BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")))) {

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }

            in.close();

            conn.disconnect();

            return body.toString();
        }

    }

}
