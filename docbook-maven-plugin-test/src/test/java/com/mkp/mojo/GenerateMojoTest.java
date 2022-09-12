package com.mkp.mojo;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.io.Files;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class GenerateMojoTest extends AbstractMojoTestCase {
    public void testExecute() throws Exception {
        File pom = getTestFile("src/test/resources/pom.xml");
        PlexusConfiguration config = extractPluginConfiguration("docbook-maven-plugin", pom);
        Mojo mojo = new GenerateMojo();
        mojo = configureMojo(mojo, config);
        CustomLog log = new CustomLog();
        mojo.setLog(log);
        setVariableValueToObject(mojo, "sourceDir", getTestFile("/src/test/resources/xml/"));
        File dest = getTestFile("/target/man/");
        setVariableValueToObject(mojo, "destDir", dest);
        mojo.execute();

        for (File file : Objects.requireNonNull(getTestFile("/src/test/resources/man/").listFiles())) {
            assertEquals(Files.toString(file, UTF_8), Files.toString(new File(dest, file.getName()), UTF_8));
        }

        Iterator<String> logs = log.getLogs();
        assertEquals("INFO: Found 2 files", logs.next());
        assertEquals("INFO: Converting to manpages format with output file extension ", logs.next());
        assertEquals("INFO: Using XSLT /xml/docbook-xsl-1.73.2/manpages/docbook.xsl", logs.next());
        assertTrue(logs.next().startsWith("INFO: Processing from"));
        assertTrue(logs.next().startsWith("INFO: Writing results"));
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : no *info/productname or alternative            server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : see http://docbook.sf.net/el/productname       server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : no refentry/refmeta/refmiscinfo@class=source   server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : see http://docbook.sf.net/el/refmiscinfo       server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: no *info/productnumber or alternative          server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: see http://docbook.sf.net/el/productnumber     server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: no refentry/refmeta/refmiscinfo@class=version  server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: see http://docbook.sf.net/el/refmiscinfo       server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Warn: meta source : no valid fallback for source; leaving empty    server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta manual : no titled ancestor of refentry                 server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta manual : no refentry/refmeta/refmiscinfo@class=manual   server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta manual : see http://docbook.sf.net/el/refmiscinfo       server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Warn: meta manual : no valid fallback for manual; leaving empty    server.ports", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/html/chunker.xsl, Line 98, Column 18, Note: Writing server.ports.4", logs.next());
        assertTrue(logs.next().startsWith("INFO: Processed file server.ports.4.xml to"));
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : no *info/productname or alternative            server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : see http://docbook.sf.net/el/productname       server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : no refentry/refmeta/refmiscinfo@class=source   server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta source : see http://docbook.sf.net/el/refmiscinfo       server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: no *info/productnumber or alternative          server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: see http://docbook.sf.net/el/productnumber     server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: no refentry/refmeta/refmiscinfo@class=version  server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta version: see http://docbook.sf.net/el/refmiscinfo       server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Warn: meta source : no valid fallback for source; leaving empty    server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta manual : no titled ancestor of refentry                 server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta manual : no refentry/refmeta/refmiscinfo@class=manual   server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Note: meta manual : see http://docbook.sf.net/el/refmiscinfo       server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/common/utility.xsl, Line 189, Column 16, Warn: meta manual : no valid fallback for manual; leaving empty    server.properties", logs.next());
        assertEquals("WARN: Source /xml/docbook-xsl-1.73.2/html/chunker.xsl, Line 98, Column 18, Note: Writing server.properties.4", logs.next());
        assertTrue(logs.next().startsWith("INFO: Processed file server.properties.4.xml to"));
        assertFalse(logs.hasNext());
    }
}
