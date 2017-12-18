package feedback;

import utils.Values;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weighters.TermWeighter;


public class Rocchio {
    private double alpha = 1.0;
    private double beta = 0.75;
    private double sigma = 0.25;
    private Map<Integer, List<String>> queries;
    private Map<Integer, Values> tenFirstDocuments;
    private Map<Integer, Double> queryVector;
    private Map<Integer, Values> relevant;
    private Map<Integer, Values> nonRelevant;
    private TermWeighter termWeighter;
    
    public Rocchio(Map<Integer, List<String>> queries, Map<Integer, Values> tenFirstDocuments,
            Map<Integer, Values> relevant, Map<Integer, Values> nonRelevant, TermWeighter termWeighter) {
        this.queries = queries;
        this.tenFirstDocuments = tenFirstDocuments;
        this.relevant = relevant;
        this.nonRelevant = nonRelevant;
        this.termWeighter = termWeighter;
        queryVector = new HashMap<>();
    }
    
    public void calculateRocchio() {
        for(Map.Entry<Integer, List<String>> query : queries.entrySet()) {
            int queryId = query.getKey();
            List<String> terms = query.getValue();
            double total = 0;
            // Calculate q0 for query
            double q0 = termWeighter.calculateWeightsOfQuery(queryId, terms, tenFirstDocuments, false);
            Map<Integer, Double> relevantDocuments = relevant.get(queryId).getValues();
            if (relevantDocuments.size() > 0) {
                double relevantWeight = termWeighter.calculateWeightsOfQuery(queryId, terms, relevant, true);
                total = alpha * q0 + ((beta / relevantDocuments.size()) * relevantWeight);
            }
            Map<Integer, Double> nonRelevantDocuments = nonRelevant.get(queryId).getValues();
            if (nonRelevantDocuments.size() > 0) {
                double nonRelevantWeight = termWeighter.calculateWeightsOfQuery(queryId, terms, nonRelevant, false);
                total -= ((sigma / nonRelevantDocuments.size()) * nonRelevantWeight);
            }
            if(total > 0)
                queryVector.put(queryId, total);
        }
    }
}
