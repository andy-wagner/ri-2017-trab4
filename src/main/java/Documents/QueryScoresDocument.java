package Documents;

import Utils.Values;
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
public class QueryScoresDocument extends Document{
    private Map<Integer, Values> scores;
    
    /**
     * Class that extends Document abstract class
     * Class that represents Scores of Queries Files
     * Constructor.
     */
    public QueryScoresDocument() {
        scores = new HashMap<>();
    }
    
    /**
     * Constructor
     * @param id
     * @param scores 
     */
    public QueryScoresDocument(int id, Map<Integer, Values> scores) {
        super(id);
        this.scores = scores;
    }
    
    /**
     * Get relevant documents
     * @return scores 
     */
    public Map<Integer, Values> getScores() {
        return scores;
    }
    
    /**
     * Set the relavant documents
     * @param scores 
     */
    public void setScores(Map<Integer, Values> scores) {
        this.scores = scores;
    }
    
    /**
     * Print object
     * @return string 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Values> query : scores.entrySet()) {
            Values documents = query.getValue();
            for (Map.Entry<Integer, Double> docId_relevance : documents.getValues().entrySet())
                sb.append(String.format("{%d, %d, %d}", 
                        query.getKey(), docId_relevance.getKey(), docId_relevance.getValue().intValue()));
        }
        return sb.toString();
    }
}
