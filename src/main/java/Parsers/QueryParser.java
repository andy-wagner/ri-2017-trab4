package Parsers;

import Documents.Document;
import Documents.QueryDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */

// Class that parses files into queries
public class QueryParser implements Strategy<List<Document>>{
    // Id of query
    private int queryId = 0;
    private static Scanner sc;
    
    /**
     * Parse a txt file and transform into queries
     * Each phrase of file is a query
     * @param file
     * @return documents 
     */
    @Override
    public List<Document> parseFile(File file) {
        List<Document> queries = new ArrayList<>();
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // Auto-increment in query's id
                queries.add(new QueryDocument(++queryId, line));
            }
            return queries;
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: File not found!");
            System.exit(1);
        }
        return null;
    }

    /**
     * Parse a directory with several txt files and transform into queries
     * @param file
     * @return documents
     */
    @Override
    public List<Document> parseDir(File file) {
        File []files = file.listFiles();
        Arrays.sort(files);
        List<Document> documents = new ArrayList<>();
        for (File f : files)
             documents.addAll(parseFile(f));
        return documents;
    }  
}
