package Parsers;

import Documents.Document;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */

/*
* XML Parser.
* Class that parser files, more specifically, XML Files.
*/
public class XMLParser implements Strategy<Document> {
    /**
     * Parse a certain file
     * @param file
     * @return Document
     */
    @Override
    public Document parseFile(File file) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            saxParser.parse(file, handler);
            return handler.getDocument();
        } catch (ParserConfigurationException | SAXException | IOException  ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Parse a certain directory
     * @param file
     * @return documents
     */
    @Override
    public List<Document> parseDir(File file) {
        File []files = file.listFiles();
        Arrays.sort(files);
        List<Document> documents = new ArrayList<>();
        for (File f : files) {
            Document document = parseFile(f);
            // Auto-increment in DOCNO of all Documents
            if (document.getId() != documents.size() + 1)
                document.setId(documents.size() + 1);
            documents.add(document);
        }
        return documents;
    }
}
