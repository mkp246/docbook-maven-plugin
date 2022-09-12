package com.mkp.mojo;

public class JarURL {
    private final String url;

    public JarURL(String url) {
        if (isAbsolute(url)) {
            this.url = url.substring(0, url.lastIndexOf('/'));
        } else {
            throw new IllegalArgumentException("not supported url: " + url);
        }
    }

    public String resolve(String child) {
        String result = url;
        String[] tokens = child.split("/");
        for (String token : tokens) {
            if (token.equals(".")) {
                continue;
            } else if (token.equals("..")) {
                result = result.substring(0, result.lastIndexOf('/'));
            } else {
                result = result + "/" + token;
            }
        }
        return result;
    }

    public static boolean isAbsolute(String href) {
        return href.startsWith("jar:");
    }

    public static String getResPath(String href) {
        if (isAbsolute(href)) {
            return href.substring(href.indexOf('!') + 1);
        } else {
            return href;
        }
    }
}
