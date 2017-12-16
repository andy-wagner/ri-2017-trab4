package feedback;

import utils.Values;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Rocchio {
    private double alpha = 1.0;
    private double beta = 0.75;
    private double sigma = 0.25;
    private Map<Integer, List<String>> queries;
    private Map<Integer, Values> tenFirstDocuments;
    private Map<Integer, Double> queryVector;
    private Map<String, Values> termIndexer;
    private Map<Integer, Values> relevant;
    private Map<Integer, Values> nonRelevant;
    
    public Rocchio(Map<Integer, List<String>> queries, Map<Integer, Values> tenFirstDocuments,
            Map<String, Values> termIndexer, Map<Integer, Values> relevant, Map<Integer, Values> nonRelevant) {
        this.queries = queries;
        this.tenFirstDocuments = tenFirstDocuments;
        this.termIndexer = termIndexer;
        this.relevant = relevant;
        this.nonRelevant = nonRelevant;
        queryVector = new HashMap<>();
    }
    
    public void calculateRocchio() {
        for(Map.Entry<Integer, List<String>> query : queries.entrySet()) {
            int queryId = query.getKey();
            List<String> terms = query.getValue();
            double total = 0;
            // Calculate q0 for query
            double q0 = calculateWeightsOfQuery(queryId, terms, tenFirstDocuments);
            Map<Integer, Double> relevantDocuments = relevant.get(queryId).getValues();
            System.out.println(tenFirstDocuments.get(1).getValues());
            if (relevantDocuments.size() > 0) {
                double relevantWeight = calculateWeightsOfQuery(queryId, terms, relevant);
                total = alpha * q0 + ((beta / relevantDocuments.size()) * relevantWeight);
            }
            Map<Integer, Double> nonRelevantDocuments = nonRelevant.get(queryId).getValues();
            if (nonRelevantDocuments.size() > 0) {
                double nonRelevantWeight = calculateWeightsOfQuery(queryId, terms, nonRelevant);
                total -= ((sigma / nonRelevantDocuments.size()) * nonRelevantWeight);
            }
            if(total > 0)
                queryVector.put(queryId, total);
        }
    }
    
    private double calculateWeightsOfQuery(int queryId, List<String> terms, Map<Integer, Values> structure) {
        double sum = 0;
        for (String term : terms) {
            Map<Integer, Double> documents = filterDocumentsOfTerm(term, queryId, structure);
            sum += calculateTermWeight(documents);
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
    
    private double calculateTermWeight(Map<Integer, Double> documents) {
        double sum = 0;
        if (documents.size() > 0) {
            for (Map.Entry<Integer, Double> document : documents.entrySet())
                sum += document.getValue();
        }
        return sum;
    }
}
