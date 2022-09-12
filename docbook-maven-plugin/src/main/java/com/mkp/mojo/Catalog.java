package com.mkp.mojo;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private final Map<String, String> map;
    private final JarURL original;

    public Catalog(String catalogResource, int size) {
        map = new HashMap<>(size);
        original = new JarURL(Catalog.class.getResource(catalogResource).toString());
    }

    public void addMapping(String key, String uri) {
        map.put(key, uri);
    }

    public String resolve(String publicId, String systemId) {
        String uri = map.get(publicId);
        if (uri == null) {
            uri = map.get(systemId);
        }
        if (uri == null) {
            return null;
        }
        return original.resolve(uri);
    }
}
