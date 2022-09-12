package com.mkp.mojo;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class CLResourceURIResolver implements URIResolver {
    private final JarURL original;

    public CLResourceURIResolver(Class<?> cl, String baseRes) {
        this.original = new JarURL(cl.getResource(baseRes).toString());
    }

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        String url = href;
        if (!JarURL.isAbsolute(url)) {
            JarURL home = has(base) ? new JarURL(base) : original;
            url = home.resolve(url);
        }
        return new StreamSource(url);
    }

    private static boolean has(String value) {
        return value != null && !value.isEmpty();
    }
}
