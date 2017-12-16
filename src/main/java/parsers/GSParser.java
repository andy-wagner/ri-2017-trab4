package parsers;

import documents.Document;
import documents.GSDocument;
import utils.Values;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

// Class that parses files into Gold Standard
public class GSParser implements Strategy<Document>{
    private int documentId = 0;
    private static Scanner sc;

    /**
     * Parse a txt file and transform into a Map of relevants
     * @param file
     * @return documents 
     */
    @Override
    public Document parseFile(File file) {
        Map<Integer, Values> relevances = new HashMap<>();
        try {
            sc = new Scanner(file);
            // Id of last query read
            int actualId = 1;
            Map<Integer, Double> values = new HashMap<>();
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
                    relevances.put(actualId, new Values(values));
                    values = new HashMap<>();
                    actualId++;
                }
                values.put(docId, (double)relevance);
            }
            relevances.put(actualId, new Values(values));
            return new GSDocument(++documentId, relevances);
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: File of Golden Standard not found!");
            System.exit(1);
        }
        return null;
    }

    /**
     * Parse a directory with several txt files and transform into relevances
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
