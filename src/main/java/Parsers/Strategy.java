package Parsers;

import Documents.Document;
import java.io.File;
import java.util.List;

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
* Strategy's pattern.
* The Strategy pattern creates objects. The strategy object changes the executing algorithm of the context object.
*/
public interface Strategy<T> {

    /**
     * Parse a certain file.
     * @param file
     * @return Document
     */
    T parseFile(File file);

    /**
     * Parse a certain directory.
     * @param file
     * @return list of Documents.
     */
    List<Document> parseDir(File file);
}
