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
    private Map<Integer, Map<String, Double>> q0;
    private Map<Integer, Map<String, Double>> queryVectors;
    private Map<String, Values> termIndexer;
    
    public Rocchio(Map<Integer, List<String>> queries, Map<Integer, Values> tenFirstDocuments, String relevanceType,
            Map<String, Values> termIndexer) {
        this.queries = queries;
        this.tenFirstDocuments = tenFirstDocuments;
        this.relevanceType = relevanceType;
        this.termIndexer = termIndexer;
        q0 = new HashMap<>();
        queryVectors = new HashMap<>();
    }
    
    public void calculateRocchio() {
        for(Map.Entry<Integer, List<String>> query : queries.entrySet()) {
            int queryId = query.getKey();
            // Calculate q0
            calculateWeightsOfQuery(queryId, query.getValue(), q0);
        }
    }
    
    private void calculateWeightsOfQuery(int queryId, List<String> terms, Map<Integer, Map<String, Double>> map) {
        Map<String, Double> termsWeights = new HashMap<>();
        for (String term : terms) {
            Map<Integer, Double> documents = getDocumentsFromTerm(term, queryId);
            termsWeights.put(term, calculateTermWeight(documents));
       }
        map.put(queryId, termsWeights);
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
