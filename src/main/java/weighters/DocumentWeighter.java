package weighters;

import utils.Values;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */

// Class that scores the documents
public class DocumentWeighter {
    // Indexer with the scores of documents
    private Map<String, Values> indexer;
    private final Map<Integer, List<String>> documents;
    // Lengths of documents. Values used in normalization
    private final Map<Integer, Double> documentLength;
    
    /**
     * Constructor
     * @param terms 
     */
    public DocumentWeighter(Map<Integer, List<String>> terms) {
        this.documents = terms;
        indexer = new HashMap<>();
        documentLength = new HashMap<>();
    }
    
    /**
     * Calculate tf
     * All the frequency documents are converted to tf
     * @param indexer 
    */
    public void calculateTermFreq(Map<String, Values> indexer) {
        for (Map.Entry<String, Values> term : indexer.entrySet()) {
            Map<Integer, Double> termDocuments = term.getValue().getValues();
            for (Map.Entry<Integer, Double> docId_freq : termDocuments.entrySet()) {
                int docId = docId_freq.getKey();
                double frequency = 1 + Math.log10(docId_freq.getValue());
                termDocuments.put(docId, frequency);
            }
            this.indexer.put(term.getKey(), new Values(termDocuments));
        }
        calculateDocsLength();
        normalizeDocuments();
    }
    
    /**
    * Write Indexer to file
    * @param file
    * @throws FileNotFoundException 
    */
    public void writeToFile(File file) throws FileNotFoundException {
        indexer = sortIndexerByTerm();
        try (PrintWriter pw = new PrintWriter(file)) {
            // For each entry of map, writes an entry like: term, docId:weight
            for (Map.Entry<String, Values> entry : indexer.entrySet()) {
                pw.print(entry.getKey() + ",");
                Values listFrequencies = entry.getValue();
                int i = 0;
                for (Map.Entry<Integer, Double> values : listFrequencies.getValues().entrySet()) {
                    int docId = values.getKey();
                    double freq = values.getValue();
                    if (i++ == listFrequencies.getValues().size() - 1)
                        // Locale.US to represent decimal number with point
                        pw.printf(Locale.US, "%d:%.5f\n", docId, freq);
                    else
                        pw.printf(Locale.US, "%d:%.5f,", docId, freq);
                }
            }
            pw.close();
        }
    }
    
    public Map<String, Values> getIndexer() {
        return indexer;
    }
    
    /**
     * Calculate the document length
     * Sum all the squared tf's documents and apply them the square root 
    */
    private void calculateDocsLength() {
        for (Map.Entry<Integer, List<String>> document : documents.entrySet()) {
            int docId = document.getKey();
            double sum = 0;
            Set<String> words = new HashSet<>(document.getValue());
            for (String word : words) {
                Values values = indexer.get(word);
                if(values !=  null)
                    sum += Math.pow(values.getValues().get(docId), 2);
            }
            documentLength.put(docId, Math.sqrt(sum));
        }
    }
    
    /**
     * Normalize the tf's documents
     * For eache document, divide tf value with the document's length
     */
    private void normalizeDocuments() {
       for (Map.Entry<String, Values> term : indexer.entrySet()) {
            Map<Integer, Double> termDocuments = term.getValue().getValues();
            for (Map.Entry<Integer, Double> doc_tf : termDocuments.entrySet()) {
                int docId = doc_tf.getKey();
                double length = documentLength.get(docId);
                double normalization = doc_tf.getValue() / length;
                termDocuments.put(docId, normalization);
            }
            indexer.put(term.getKey(), new Values(termDocuments));
        } 
    }
    
    /**
     * Sort indexer alphabetically
     * @return sorted indexer 
     */
    private Map<String, Values> sortIndexerByTerm() {
        List<Map.Entry<String, Values>> words = new ArrayList<>(indexer.entrySet());
        // Comparator to sort by query_id
        Collections.sort(words, 
                (Map.Entry<String, Values> o1, Map.Entry<String, Values> o2) -> o1.getKey().compareTo(o2.getKey()));
        // Create new map with sorted results
        Map<String, Values> result = new LinkedHashMap<>();
        for (Map.Entry<String, Values> entry: words) {
            Values values = entry.getValue();
            Map<Integer, Double> entyDocuments = sortDocuments(values.getValues());
            values.setValues(entyDocuments);
            result.put(entry.getKey(), values);
        }
        return result;
    }
    
    /**
     * Sort Indexer by id of documents
     * @param values
     * @return 
     */
    private Map<Integer, Double> sortDocuments(Map<Integer, Double> values) {
        List<Map.Entry<Integer, Double>> valuesDocuments = new ArrayList<>(values.entrySet());
        // Comparator to sort documents by their id
        Collections.sort(valuesDocuments, 
                (Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) -> o1.getKey().compareTo(o2.getKey()));
        // Create new map with sorted results
        Map<Integer, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry: valuesDocuments)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }
}
