/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_sax.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Manuel Delgado Sánchez
 */
public class XmlSaxUtils {

    public static DefaultHandler parseStringToDocument(String xml, DefaultHandler handler) throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        // XXE attack, see https://rules.sonarsource.com/java/RSPEC-2755
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new InputSource(new StringReader(xml)), handler);
        return handler;

    }

    public static void writeXmlToFile(TransformerHandler transformerHandler, String fileName) throws TransformerException, IOException {

        Transformer transformer = transformerHandler.getTransformer();
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "fich.dtd"
        );
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        File newXML = new File("filename");
        FileWriter fileWriter = new FileWriter(newXML);
        Result result = new StreamResult(fileWriter);
        transformerHandler.setResult(result);
    }

}
