package Indexers;

import Utils.Values;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */

// Class Indexer that create an Indexer
public class IndexerCreator {
    // List of terms (words)
    private final Map<Integer, List<String>> terms;
    // Indexer. The Indexer has a list of documents like [term, docId: score] 
    private final Map<String, Values> indexer;
    
    /**
     * Constructor.
     * @param terms
     * @throws FileNotFoundException
     */
    public IndexerCreator(Map<Integer, List<String>> terms) throws FileNotFoundException {
        this.terms = terms;
        indexer = new HashMap<>();
    }
    
    /**
     * Get the indexer
     * @return indexer 
     */
    public Map<String, Values> getIndexer() {
        return indexer;
    }
    
    /**
     * Create a new indexer
     */
    public void createIndexer() {
        for (Map.Entry<Integer, List<String>> doc_terms : terms.entrySet()) {
            List<String> words = doc_terms.getValue();
            int docId = doc_terms.getKey();
            /*
            * If the indexer hasn't the document's id yet, it's created a new instance.
            * If the indexer has the document's id and not the term, the pair <term, frequency> is added.
            * If the indexer has the document's and the term, it's incrementd frequency.
            */
            for (String word : words) {
                if (indexer.keySet().contains(word)) {
                    Values doc_freq = indexer.get(word);
                    boolean containsPair = false;
                    for (Map.Entry<Integer, Double> entry : doc_freq.getValues().entrySet()) {
                        if (entry.getKey() == docId) {
                            double value = entry.getValue();
                            doc_freq.addValue(entry.getKey(), value + 1);
                            containsPair = true;
                            break;
                        }
                    }
                    if (!containsPair) {
                        doc_freq.addValue(docId, 1);
                    }
                    indexer.put(word, doc_freq);
                } else {
                    Values doc_freq = new Values();
                    doc_freq.addValue(docId, 1);
                    indexer.put(word, doc_freq);
                }
            }
        }
    }
}