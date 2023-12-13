/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_sax.model;

import com.mimo.cloud.rssprocessor_sax.handlers.CanalHandler;
import com.mimo.cloud.rssprocessor_sax.utils.Constants;
import com.mimo.cloud.rssprocessor_sax.utils.XmlSaxUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author mande
 */
public class Canal {

    private String titulo;
    private String url;
    private String descripcion;
    private ArrayList<Noticia> noticias;

    public Canal() {
        this.noticias = new ArrayList<>();
    }

    public Canal(String titulo, String url, String descripcion, ArrayList<Noticia> noticias) {
        this.titulo = titulo;
        this.url = url;
        this.descripcion = descripcion;
        this.noticias = noticias;
    }

    public Canal(Node node) {

        if (node.hasChildNodes()) {

            this.noticias = new ArrayList<>();

            for (int i = 0; i < node.getChildNodes().getLength(); i++) {

                Node childNode = node.getChildNodes().item(i);

                switch (childNode.getNodeName()) {

                    case "title":
                        this.titulo = childNode.getTextContent();
                        break;

                    case "link":
                        this.url = childNode.getTextContent();
                        break;

                    case "description":
                        this.descripcion = childNode.getTextContent();
                        break;

                    case "item":
                        this.noticias.add(new Noticia(childNode));
                        break;

                    default:

                }
            }
        }
    }

    public static Canal fromXml(String xml) throws ParserConfigurationException, SAXException, IOException {

        return ((CanalHandler) XmlSaxUtils.parseStringToDocument(xml, new CanalHandler())).getResult();

    }

    public TransformerHandler toXml() throws ParserConfigurationException, TransformerConfigurationException, SAXException, IOException {

        SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();

        Transformer transformer = transformerHandler.getTransformer();
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "fich.dtd"
        );
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        File newXML = new File(String.format(Constants.xmlFilePath, this.getTitulo()));
        FileWriter fileWriter = new FileWriter(newXML);
        Result result = new StreamResult(fileWriter);
        transformerHandler.setResult(result);

        transformerHandler.startDocument();

        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", "", "canal", "", this.titulo);

        transformerHandler.startElement("", "", "noticias", atts);

        atts.clear();
        String text;

        for (Noticia noticia : this.noticias) {

            transformerHandler.startElement("", "", "noticia", atts);
            text = noticia.getTitulo();
            transformerHandler.characters(text.toCharArray(), 0, text.length());
            transformerHandler.endElement("", "", "noticia");
        }

        transformerHandler.endElement("", "", "noticias");

        transformerHandler.endDocument();

        return transformerHandler;

    }

    public JSONObject toJson() {

        JSONObject json = new JSONObject();

        JSONArray jsonArray = new JSONArray(this.noticias.stream().map(noticia -> noticia.toJson()).collect(Collectors.toList()));

        json.put("noticias", jsonArray);

        return json;

    }

    public void addNoticia(Noticia noticia) {
        this.noticias.add(noticia);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Noticia> getNoticias() {
        return noticias;
    }

    public void setNoticias(ArrayList<Noticia> noticias) {
        this.noticias = noticias;
    }

}
