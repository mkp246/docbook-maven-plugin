package com.mkp.mojo;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.apache.maven.plugin.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static com.mkp.mojo.JarURL.getResPath;

public class MojoErrorHandler implements ErrorListener, ErrorHandler {
    private static final String FORMAT = "Source %s, Line %s, Column %s, %s";
    private final Log log;
    private final boolean showErrors;

    public MojoErrorHandler(Log log, boolean showErrors) {
        this.log = log;
        this.showErrors = showErrors;
    }

    @Override
    public void warning(TransformerException exception) throws TransformerException {
        logLevel(Lvl.WARN, exception);
    }

    @Override
    public void error(TransformerException exception) throws TransformerException {
        logLevel(Lvl.ERROR, exception);
    }

    @Override
    public void fatalError(TransformerException exception) throws TransformerException {
        logLevel(Lvl.ERROR, exception);
        throw exception;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        logLevel(Lvl.WARN, exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        logLevel(Lvl.ERROR, exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        logLevel(Lvl.ERROR, exception);
        throw exception;
    }

    private void logLevel(Lvl level, TransformerException exception) {
        if (!showErrors || !level.isEnabled(log)) {
            return;
        }
        SourceLocator locator = exception.getLocator();
        String source = getSource(locator.getPublicId());
        if (source == null) {
            source = getSource(locator.getSystemId());
        }
        logFormat(level, source, locator.getLineNumber(), locator.getColumnNumber(), exception.getMessage());
    }

    private void logLevel(Lvl level, SAXParseException exception) {
        String source = getSource(exception.getPublicId());
        if (source == null) {
            source = getSource(exception.getSystemId());
        }
        logFormat(level, source, exception.getLineNumber(), exception.getColumnNumber(), exception.getMessage());

    }

    private void logFormat(Lvl level, String source, int lineNumber, int columnNumber, String message) {
        String msg = String.format(FORMAT, source, lineNumber, columnNumber, message);
        level.log(log, msg);
    }

    private String getSource(String id) {
        if (id != null) {
            return getResPath(id);
        }
        return null;
    }

    private enum Lvl {
        INFO {
            @Override
            public boolean isEnabled(Log log) {
                return log.isInfoEnabled();
            }

            @Override
            public void log(Log log, String msg) {
                log.info(msg);
            }
        }, WARN {
            @Override
            public boolean isEnabled(Log log) {
                return log.isWarnEnabled();
            }

            @Override
            public void log(Log log, String msg) {
                log.warn(msg);
            }
        }, ERROR {
            @Override
            public boolean isEnabled(Log log) {
                return log.isErrorEnabled();
            }

            @Override
            public void log(Log log, String msg) {
                log.error(msg);
            }
        };

        public abstract boolean isEnabled(Log log);

        public abstract void log(Log log, String msg);
    }
}
