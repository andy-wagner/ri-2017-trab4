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
public class Parser {
    private final Strategy strategy;
    
    /**
     * Class that uses the Strategy's pattern
     * Constructor
     * @param strategy
     */
    public Parser(Strategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Parse a certain file
     * @param file
     * @return Object
     */
    public Object parseFile(File file) {
        return strategy.parseFile(file);
    }
    
    /**
     * Parse a certain directory
     * @param file
     * @return List of Documents
     */
    public List<Document> parseDir(File file) {
        return strategy.parseDir(file);
    }
}
