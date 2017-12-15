package Parsers;

import Documents.Document;
import Documents.GSDocument;
import Utils.Values;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

// Class that parses files of results of Assignment 2
public class QueryScoresParser implements Strategy<Document>{
    private int documentId = 0;
    private static Scanner sc;

    /**
     * Parse a txt file and transform into a Map of scores
     * @param file
     * @return documents 
     */
    @Override
    public Document parseFile(File file) {
        Map<Integer, Values> scores = new LinkedHashMap<>();
        try {
            sc = new Scanner(file);
            // Id of last query read
            int actualId = 1;
            Map<Integer, Double> values = new LinkedHashMap<>();
            sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // Line: queryId docId relevance
                String []data = line.split("\\s+");
                int queryId = Integer.parseInt(data[0]);
                int docId = Integer.parseInt(data[1]);
                int relevance = Integer.parseInt(data[2]);
                /** 
                 * When a new query id is found, create a new map of values
                 * Add the last values to the relevants map
                 */
                if (queryId != actualId) {
                    scores.put(actualId, new Values(values));
                    values = new HashMap<>();
                    actualId++;
                }
                values.put(docId, (double)relevance);
            }
            scores.put(actualId, new Values(values));
            return new GSDocument(++documentId, scores);
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: File of Golden Standard not found!");
            System.exit(1);
        }
        return null;
    }

    /**
     * Parse a directory with several txt files and transform into scores
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
