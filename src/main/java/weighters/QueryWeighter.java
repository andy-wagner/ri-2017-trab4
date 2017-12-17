package weighters;

import utils.Values;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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

// Class that scores the queries
public class QueryWeighter {
    // Indexer with queries
    private final Map<String, Values> indexer;
    // Number of Documents. Used in idf
    private final int numberDocuments;
    private final Map<Integer, List<String>> queries;
    // Lengths of queries. Values used in normalization
    private final Map<Integer, Double> queryLength;
    // Indexer with the scores
    private Map<Integer, Values> scorer;
    
    /**
     * Constructor
     * @param indexer
     * @param queries
     * @param numberDocuments 
     */
    public QueryWeighter(Map<String, Values> indexer, Map<Integer, List<String>> queries, int numberDocuments) {
        this.indexer = indexer;
        this.queries = queries;
        this.numberDocuments = numberDocuments;
        queryLength = new HashMap<>();
        scorer = new HashMap<>();
    }   
       
    /**
     * Get the indexer with scores
     * @return scorer 
     */
    public Map<Integer, Values> getQueryScorer() {
       return scorer;
    }
    
    /**
     * Calculate idf
     * All the documents of the indexer of queries are updated do the idf documents
     * @param queryIndexer
     * @param documentIndexer 
     */
    public void calculateInverseDocFreq(Map<String, Values> queryIndexer, Map<String, Values> documentIndexer) {
        for (Map.Entry<String, Values> term : queryIndexer.entrySet()) {
            String termId = term.getKey();
            Map<Integer, Double> termQueries = term.getValue().getValues();
            for (Map.Entry<Integer, Double> doc_freq : termQueries.entrySet()) {
                int docId = doc_freq.getKey();
                double idf = 0;
                // If the query indexer has the term, get its value and update it with idf
                if (documentIndexer.get(termId) != null)
                    idf = doc_freq.getValue() * Math.log10(numberDocuments / documentIndexer.get(termId).getValues().size());
                termQueries.put(docId, idf);
            }
            // Put the idf in the query indexer
            indexer.put(termId, new Values(termQueries));
        }
        calculateQueryLength();
        normalizeQueries();
    }
    
    /**
     * Calculate score of a document.
     * For each term present in a certain query, multiply the weight of query and document
     * After that, sum all multiplications and get the final score
     * @param documentIndexer 
     */
    public void calculateDocumentScore(Map<String, Values> documentIndexer) {
        // Get the term
        for (Map.Entry<String, Values> term : indexer.entrySet()) {
            String termId = term.getKey();
            Values queryValues = term.getValue();
            // Get the id and weight of the queries that the term appears
            for (Map.Entry<Integer, Double> queryId_score : queryValues.getValues().entrySet()) {
                int queryId = queryId_score.getKey();
                // Check if term appears on indexer of documents
                if (documentIndexer.get(termId) != null) {
                    Values docValues = documentIndexer.get(termId);
                    // Get the id and weight of the documents that the term appears
                    for (Map.Entry<Integer, Double> docId_score : docValues.getValues().entrySet()) {
                        int docId = docId_score.getKey();
                        // Update the score
                        double score = queryId_score.getValue() * docId_score.getValue();
                        Values inputValues = scorer.get(queryId);
                        /**
                         * If theres is already an instance of the score of the pair (query, document), update
                         * Otherwise, create a new instance
                         */
                        if (inputValues != null) {
                            if(inputValues.getValues().get(docId) != null)
                                score += inputValues.getValues().get(docId);
                            // Update the score sum the weight of another term
                            inputValues.addValue(docId, score);
                            scorer.put(queryId, inputValues);
                        } else {
                            Map<Integer, Double> newValue = new HashMap<>();
                            // Create a new score for the pair (query, document)
                            newValue.put(docId, score);
                            scorer.put(queryId, new Values(newValue));
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Write the sorted results in file
     * @param file 
     */
    public void writeToFile(File file) {
        scorer = sortScores();
        PrintWriter pw;
        try {
            pw = new PrintWriter(file);
            pw.write(String.format("%-10s %-10s %-10s\n", "query_id", "doc_id", "doc_score"));
            for (Map.Entry<Integer, Values> score : scorer.entrySet()) {
                Values values = score.getValue();
                for (Map.Entry<Integer, Double> value : values.getValues().entrySet())
                    pw.write(String.format("%-10s %-10d %-10.5f\n",score.getKey(), value.getKey(), value.getValue()));
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: File not found!");
            System.exit(1);
        }
    }
    
    /**
     * Calculate the query length
     * Sum all the squared idf's documents and apply them the square root 
     */
    private void calculateQueryLength() {
        for (Map.Entry<Integer, List<String>> query : queries.entrySet()) {
            int queryId = query.getKey();
            double sum = 0;
            // Set to don't repeat the terms and duplicate the documents
            Set<String> terms = new HashSet<>(query.getValue());
            for (String word : terms) {
                Values values = indexer.get(word);
                sum += Math.pow(values.getValues().get(queryId), 2);
            }
            queryLength.put(queryId, Math.sqrt(sum));
        }
    }
    
    /**
     * Normalize the idf's documents
     * For eache query, divide idf value with the query length
    */
    private void normalizeQueries() {
       for (Map.Entry<String, Values> term : indexer.entrySet()) {
            Map<Integer, Double> documents = term.getValue().getValues();
            for (Map.Entry<Integer, Double> query_weight : documents.entrySet()) {
                int queryId = query_weight.getKey();
                double length = queryLength.get(queryId);
                double normalization = query_weight.getValue() / length;
                documents.put(queryId, normalization);
            }
            indexer.put(term.getKey(), new Values(documents));
        }
    }
    
    /**
     * Sort the results
     * @return sorted scores
     */
    private Map<Integer, Values> sortScores() {
        Map<Integer, Values> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Values> query : scorer.entrySet())
            result.put(query.getKey(), new Values(query.getValue().sortValues()));
        return result;
    }
}
