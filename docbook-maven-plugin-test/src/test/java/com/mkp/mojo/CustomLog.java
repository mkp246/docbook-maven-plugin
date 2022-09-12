package com.mkp.mojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

public class CustomLog implements Log {
    private List<String> logs = new ArrayList<>();

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(CharSequence content) {
        logs.add("DEBUG: " + content);
    }

    @Override
    public void debug(CharSequence content, Throwable error) {
        logs.add("DEBUG: " + content + error.getMessage());
    }

    @Override
    public void debug(Throwable error) {
        logs.add("DEBUG: " + error.getMessage());
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(CharSequence content) {
        logs.add("INFO: " + content);
    }

    @Override
    public void info(CharSequence content, Throwable error) {
        logs.add("INFO: " + content + error.getMessage());
    }

    @Override
    public void info(Throwable error) {
        logs.add("INFO: " + error.getMessage());
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(CharSequence content) {
        logs.add("WARN: " + content);
    }

    @Override
    public void warn(CharSequence content, Throwable error) {
        logs.add("WARN: " + content + error.getMessage());
    }

    @Override
    public void warn(Throwable error) {
        logs.add("WARN: " + error.getMessage());
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(CharSequence content) {
        logs.add("ERROR: " + content);
    }

    @Override
    public void error(CharSequence content, Throwable error) {
        logs.add("ERROR: " + content + error.getMessage());
    }

    @Override
    public void error(Throwable error) {
        logs.add("ERROR: " + error.getMessage());
    }

    public Iterator<String> getLogs() {
        return logs.iterator();
    }
}
