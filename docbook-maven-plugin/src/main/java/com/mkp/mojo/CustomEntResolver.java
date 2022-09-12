package com.mkp.mojo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CustomEntResolver implements EntityResolver {
    private List<Catalog> catalogs;

    public CustomEntResolver() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        catalogs = new ArrayList<>(2);
        loadCatalog(builder, "/xml/docbook-xml-4.2/catalog.xml");
        loadCatalog(builder, "/xml/docbook-xml-4.5/catalog.xml");
        loadCatalog(builder, "/xml/docbook-xml-5.0.1/catalog.xml");
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String url = null;
        for (Catalog catalog : catalogs) {
            url = catalog.resolve(publicId, systemId);
            if (url != null) {
                break;
            }
        }
        if (url == null) {
            return null;
        }
        return new InputSource(url);
    }

    private void loadCatalog(DocumentBuilder builder, String resource) throws IOException, SAXException {
        InputStream stream = CustomEntResolver.class.getResourceAsStream(resource);
        Document doc = builder.parse(stream);
        NodeList entries = doc.getDocumentElement().getElementsByTagName("*");
        if (entries.getLength() == 0) {
            return;
        }
        Catalog catalog = new Catalog(resource, entries.getLength());
        for (int idx = 0; idx < entries.getLength(); idx++) {
            Element item = (Element) entries.item(idx);
            String tag = item.getTagName();
            switch (tag) {
                case "public":
                    catalog.addMapping(item.getAttribute("publicId"), item.getAttribute("uri"));
                    break;
                case "system":
                    catalog.addMapping(item.getAttribute("systemId"), item.getAttribute("uri"));
                    break;
                case "uri":
                    catalog.addMapping(item.getAttribute("name"), item.getAttribute("uri"));
                    break;
            }
        }
        builder.reset();
        catalogs.add(catalog);
    }
}
