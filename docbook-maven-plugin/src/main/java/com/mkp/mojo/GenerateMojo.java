package com.mkp.mojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * transform dockbook xml to specified format using XSLT
 *
 * @since 1.0
 */
@Mojo(name = "generate", requiresProject = false)
public class GenerateMojo extends AbstractMojo {
    /**
     * Source directory where source files are located
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.sourceDir", required = true)
    File sourceDir;

    /**
     * Destination directory where converted files has to saved
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.destDir", required = true)
    File destDir;

    /**
     * docbook xslt files version to used
     * current supported are 1.73.2, 1.79.1
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.xslVersion", defaultValue = "1.79.1")
    String xslVersion;

    /**
     * Output format of transformed file, must be one of the directory name in docbook-xsl-&lt;xslVersion&gt;.zip
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.format", required = true)
    String format;

    /**
     * Stylesheet file in docbook-xsl-&lt;xslVersion&gt;.zip/&lt;format&gt;/
     */
    @Parameter(property = "docbook.stylesheet", required = true)
    String stylesheet;

    /**
     * Files regex filter to process file from sourceDir
     * by default includes all *.xml files
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.include", defaultValue = ".*\\.xml")
    String include;

    /**
     * Outfile files extension to append.
     * source file's extension is dropped and this extension is added if set
     * e.g. a.8.txt.xml source will become a.8.txt by default.
     * if extension is set to gz then it will become a.8.txt.gz
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.extension", defaultValue = "")
    String extension = "";

    /**
     * Show xml parsing errors
     *
     * @since 1.0
     */
    @Parameter(property = "docbook.showErrors", defaultValue = "false")
    boolean showErrors;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Pattern incl = Pattern.compile(include);
            File[] files = sourceDir.listFiles((dir, name) -> incl.matcher(name).matches());
            if (files == null || files.length == 0) {
                getLog().warn("No file found");
                return;
            }
            getLog().info("Found " + files.length + " files");

            if (!extension.isEmpty()) {
                extension = "." + extension;
            }

            MojoErrorHandler errorHandler = new MojoErrorHandler(getLog(), showErrors);

            TransformerFactory factory = TransformerFactoryImpl.newInstance();
            factory.setURIResolver(new CLResourceURIResolver(getClass(), getXsltPath()));
            factory.setErrorListener(errorHandler);

            getLog().info("Converting to " + format + " format with output file extension " + extension);
            getLog().info("Using XSLT " + getXsltPath());
            Transformer transformer = factory.newTransformer(getXsltSource());
            transformer.setErrorListener(errorHandler);

            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setEntityResolver(new CustomEntResolver());
            reader.setErrorHandler(errorHandler);

            getLog().info("Processing from " + sourceDir);
            getLog().info("Writing results to " + destDir);
            Files.createDirectories(destDir.toPath());
            for (File file : files) {
                Result result = getResult(file);
                transformer.transform(getSaxSource(reader, file), result);
                transformer.reset();
                getLog().info("Processed file " + file.getName() + " to " + result.getSystemId());
            }
        } catch (TransformerException | IOException | SAXException | ParserConfigurationException e) {
            throw new MojoExecutionException(e);
        }
    }

    private String getXsltPath() {
        return "/xml/docbook-xsl-" + xslVersion + "/" + format + "/" + stylesheet;
    }

    private Source getXsltSource() {
        InputStream is = getClass().getResourceAsStream(getXsltPath());
        return new StreamSource(is);
    }

    private Result getResult(File inputFile) {
        String outName = inputFile.getName();
        int extStart = outName.lastIndexOf('.');
        outName = outName.substring(0, extStart) + extension;
        File outFile = destDir.toPath().resolve(outName).toFile();
        return new StreamResult(outFile);
    }

    private SAXSource getSaxSource(XMLReader reader, File file) throws SAXException, IOException, ParserConfigurationException {
        return new SAXSource(reader, getIs(file));
    }

    private InputSource getIs(File file) {
        return new InputSource(file.toString());
    }
}
