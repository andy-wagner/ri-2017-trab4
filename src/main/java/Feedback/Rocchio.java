package Feedback;

import Utils.Values;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Rocchio {
    private double alpha = 1.0;
    private double beta = 0.75;
    private double sigma = 0.25;
    private String relevanceType;
    private Map<Integer, List<String>> queries;
    private Map<Integer, Values> tenFirstDocuments;
    private Map<Integer, Map<String, Double>> queriesVectors;
    private Map<String, Values> termIndexer;
    
    public Rocchio(Map<Integer, List<String>> queries, Map<Integer, Values> tenFirstDocuments, String relevanceType,
            Map<String, Values> termIndexer) {
        this.queries = queries;
        this.tenFirstDocuments = tenFirstDocuments;
        this.relevanceType = relevanceType;
        this.termIndexer = termIndexer;
        queriesVectors = new HashMap<>();
    }
    
    public void calculateWeightsOfQueryVector() {
        for (Map.Entry<Integer, List<String>> query : queries.entrySet()) {
            int queryId = query.getKey();
            List<String> terms = query.getValue();
            Map<String, Double> termsWeights = new HashMap<>();
            for (String term : terms) {
                Map<Integer, Double> documents = getDocumentsFromTerm(term, queryId);
                termsWeights.put(term, calculateTermWeight(documents));
           }
            queriesVectors.put(queryId, termsWeights);
        }
    }
    
    private Map<Integer, Double> getDocumentsFromTerm(String term, int queryId) {
        Map<Integer, Double> filter = new HashMap<>();
        Map<Integer, Double> tenDocuments = tenFirstDocuments.get(queryId).getValues();
        if (termIndexer.get(term) != null) {
            filter = termIndexer.get(term).getValues().entrySet().stream()
                    .filter(map -> tenDocuments.containsKey(map.getKey()))
                    .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        }
        return filter;
    }
    
    private double calculateTermWeight(Map<Integer, Double> documents) {
        double sum = 0;
        if (documents.size() > 0) {
            for (Map.Entry<Integer, Double> document : documents.entrySet())
                sum += document.getValue();
        }
        return sum;
    }
}
