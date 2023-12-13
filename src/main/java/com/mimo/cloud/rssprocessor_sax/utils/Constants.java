/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_sax.utils;

/**
 *
 * @author mande
 */
public class Constants {

    public static String url = "https://www.europapress.es/rss/rss.aspx";

    public static String xmlFilePath = "noticias_%s.xml";

    public static String jsonFilePath = "noticias_%s.json";

    //Variables from request body
    public static final String CHANNEL = "channel";

    public static final String ITEM = "item";

    public static final String TITLE = "title";

    public static final String LINK = "link";

    public static final String DESCRIPTION = "description";

    public static final String PUBDATE = "pubDate";

    public static final String CATEGORY = "category";

}
