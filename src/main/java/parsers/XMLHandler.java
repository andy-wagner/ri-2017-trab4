package parsers;

import documents.Document;
import documents.XMLDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
* Constructor.
* XML Handler is a class that handles XML files, using the SAX parser.
*/
public class XMLHandler extends DefaultHandler {
   private Document document;
   private StringBuffer sb;
   boolean id = false;
   boolean title = false;
   boolean author = false;
   boolean text = false;

    /**
     * Check attributes in XML file
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
   public void startElement(String uri, 
   String localName, String qName, Attributes attributes) throws SAXException {
       sb = new StringBuffer();
       if (qName.equalsIgnoreCase("DOC")) {
           document = new XMLDocument();
       } else if (qName.equalsIgnoreCase("DOCNO")) {
         id = true;
      } else if (qName.equalsIgnoreCase("TITLE")) {
         title = true;
      } else if (qName.equalsIgnoreCase("AUTHOR")) {
         author = true;
      } else if (qName.equalsIgnoreCase("TEXT")) {
          text = true;
      }
   }

    /**
     * Set the attributes in Document's Object
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
   public void characters(char ch[], int start, int length) throws SAXException {
       sb.append(ch, start, length);
       if (id) {
           // Save if Document has DOCNO
           if (new String(ch, start, length).trim().length() > 0)
            document.setId(Integer.parseInt(new String(ch, start, length).trim()));
           id = false;
      } else if (title) {
          ((XMLDocument) document).setTitle(new String(ch, start, length).trim());
          title = false;
      } else if (author) {
          ((XMLDocument) document).setAuthor(new String(ch, start, length).trim());
          author = false;
      } else if (text) {
          text = false;
      }
   }
   
    /**
     * Finish the read of the element in XML file
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
   public void endElement(String uri, 
   String localName, String qName) throws SAXException {
      document.setText(sb.toString().trim());
   }
   
    /**
     * Return the Document read.
     * @return Document
     */
    public Document getDocument() {
       return document;
   }
}