package expansion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import utils.Values;

public class QueryExpansion {
    private Map<Integer, Values> relevantDocuments;
    private Map<Integer, Map<String, Double>> termsWeight;
            
    public QueryExpansion(Map<Integer, Values> relevantDocuments, Map<Integer, Map<String, Double>> termsWeight) {
        this.relevantDocuments = relevantDocuments;
        this.termsWeight = termsWeight;
        addHigherTermsToQuery();
    }
    
    private void addHigherTermsToQuery() {
        sortTermsOnQuery();
        for (Map.Entry<Integer, Map<String, Double>> query : termsWeight.entrySet()) {
            Map<String, Double> higherTerms = query.getValue();
            int i = 0;
            for (Map.Entry<String,Double> term : higherTerms.entrySet()) {
                higherTerms.put(term.getKey(), term.getValue() * 2);
                if (++i == 3)
                    break;
            }
            termsWeight.put(query.getKey(), higherTerms);
        }
    }
    
    private void sortTermsOnQuery() {
        Map<Integer, Map<String, Double>> sortedResults = new LinkedHashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> query : termsWeight.entrySet()) {
            Map<String, Double> terms = sortTermsWeigth(query.getValue());
            sortedResults.put(query.getKey(), terms);
        }
        termsWeight = sortedResults;
    }
    
    private Map<String, Double> sortTermsWeigth(Map<String, Double> values) {
        List<Map.Entry<String,Double>> entries = new ArrayList<>(values.entrySet());
        Collections.sort(entries, (Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) 
                -> o1.getValue().compareTo(o2.getValue()) * -1);
        Collections.sort(entries, (Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) -> {
            int res = 0;
            if (Objects.equals(o1.getValue(), o2.getValue()))
                res = o1.getKey().compareTo(o2.getKey());
            return res;
        });
        // Create new map with sorted results
        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry: entries)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }
}
