/*
 * Copyright (C) 2023 mande
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mimo.cloud.rssprocessor_sax.handlers;

import com.mimo.cloud.rssprocessor_sax.model.Canal;
import com.mimo.cloud.rssprocessor_sax.model.Noticia;
import com.mimo.cloud.rssprocessor_sax.utils.Constants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mande
 */
public class CanalHandler extends DefaultHandler {

    private Canal canal = null;
    private Noticia currentNoticia = null;
    private final StringBuilder currentValue = new StringBuilder();

    public Canal getResult() {
        return canal;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentValue.setLength(0);

        if (qName.equalsIgnoreCase(Constants.CHANNEL)) {
            canal = new Canal();
        }

        if (qName.equalsIgnoreCase(Constants.ITEM)) {
            currentNoticia = new Noticia();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(Constants.TITLE)) {

            if (currentNoticia == null) {
                canal.setTitulo(currentValue.toString());
            } else {
                currentNoticia.setTitulo(currentValue.toString());
            }

        }

        if (qName.equalsIgnoreCase(Constants.LINK)) {

            if (currentNoticia == null) {
                canal.setUrl(currentValue.toString());
            } else {
                currentNoticia.setUrl(currentValue.toString());
            }
        }

        if (qName.equalsIgnoreCase(Constants.DESCRIPTION)) {
            if (currentNoticia == null) {
                canal.setDescripcion(currentValue.toString());
            } else {
                currentNoticia.setDescripcion(currentValue.toString());
            }
        }

        if (qName.equalsIgnoreCase(Constants.PUBDATE) && currentNoticia != null) {
            currentNoticia.setFecha_publicacion(currentValue.toString());
        }

        if (qName.equalsIgnoreCase(Constants.CATEGORY) && currentNoticia != null) {
            currentNoticia.setCategoria(currentValue.toString());
        }

        if (qName.equalsIgnoreCase(Constants.ITEM)) {
            canal.addNoticia(currentNoticia);
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(ch, start, length);
    }

}
