package documents;

import utils.Values;
import java.util.HashMap;
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
public class GSDocument extends Document{
    private Map<Integer, Values> relevants;
    
    /**
     * Class that extends Document abstract class
     * Class that represents Gold Standard Files
     * Constructor.
     */
    public GSDocument() {
        relevants = new HashMap<>();
    }
    
    /**
     * Constructor
     * @param id
     * @param relevances 
     */
    public GSDocument(int id, Map<Integer, Values> relevances) {
        super(id);
        this.relevants = relevances;
    }
    
    /**
     * Get relevant documents
     * @return relevants 
     */
    public Map<Integer, Values> getRelevants() {
        return relevants;
    }
    
    /**
     * Set the relavant documents
     * @param relevants 
     */
    public void setRelevants(Map<Integer, Values> relevants) {
        this.relevants = relevants;
    }
    
    /**
     * Print object
     * @return string 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Values> query : relevants.entrySet()) {
            Values documents = query.getValue();
            for (Map.Entry<Integer, Double> docId_relevance : documents.getValues().entrySet())
                sb.append(String.format("{%d, %d, %d}", 
                        query.getKey(), docId_relevance.getKey(), docId_relevance.getValue().intValue()));
        }
        return sb.toString();
    }
}
