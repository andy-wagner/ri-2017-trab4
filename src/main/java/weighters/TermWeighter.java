package weighters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import utils.Values;

public class TermWeighter {
    private Map<Integer, Map<String, Double>> termsWeights;
    private final Map<String, Values> termIndexer;
    
    public TermWeighter(Map<String, Values> termIndexer) {
        termsWeights = new HashMap<>();
        this.termIndexer = termIndexer;
    }
   
    public Map<Integer, Map<String, Double>> getTermsWeights() {
        return termsWeights;
    }
    
    public double calculateWeightsOfQuery(int queryId, List<String> terms, Map<Integer, Values> structure, 
            boolean relevant) {
        double sum = 0;
        Map<String, Double> weights = new HashMap<>();
        for (String term : terms) {
            Map<Integer, Double> documents = filterDocumentsOfTerm(term, queryId, structure);
            double weight = calculateTermWeight(documents);
            sum += weight;
            if (relevant)
                weights.put(term, weight);
        }
        if (relevant) 
            termsWeights.put(queryId, weights);
        return sum;
    }
        
    private double calculateTermWeight(Map<Integer, Double> documents) {
        double sum = 0;
        if (documents.size() > 0) {
            for (Map.Entry<Integer, Double> document : documents.entrySet())
                sum += document.getValue();
        }
        return sum;
    }  
        
    private Map<Integer, Double> filterDocumentsOfTerm(String term, int queryId, Map<Integer, Values> structure) {
        Map<Integer, Double> filter = new HashMap<>();
        Map<Integer, Double> documents = structure.get(queryId).getValues();
        if (termIndexer.get(term) != null) {
            filter = termIndexer.get(term).getValues().entrySet().stream()
                    .filter(map -> documents.containsKey(map.getKey()))
                    .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        }
        return filter;
    }
}
