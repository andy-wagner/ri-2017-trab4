package Feedback;

import Utils.Values;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExplicitRelevance implements RelevanceFeedback{
    private final Map<Integer, Values> tenFirsDocuments;
    private final Map<Integer, Values> gsRelevants;
    
    public ExplicitRelevance(Map<Integer, Values> gsRelevants) {
        tenFirsDocuments = new LinkedHashMap<>();
        this.gsRelevants = gsRelevants;
    }

    @Override
    public void setTenFirstDocuments(Map<Integer, Values> queries) {
        for (Map.Entry<Integer, Values> query : queries.entrySet()) {
            Values documents = query.getValue();
            int nDocuments = 0;
            Map<Integer, Double> scores = new LinkedHashMap<>();
            for (Map.Entry<Integer, Double> docScore : documents.getValues().entrySet()) {
                scores.put(docScore.getKey(), docScore.getValue());
                if (++nDocuments == 10)
                    break;
            }
            tenFirsDocuments.put(query.getKey(), new Values(scores));
        }
    }

    @Override
    public Map<Integer, Values> getDocuments() {
        return tenFirsDocuments;
    }

    @Override
    public Map<Integer, Double> getRelevantDocuments(int queryId) {
        return tenFirsDocuments.get(queryId).getValues().entrySet().stream()
                .filter(map -> gsRelevants.get(queryId).getValues().containsKey(map.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    @Override
    public Map<Integer, Double> getNonRelevantDocuments(int queryId) {
        return tenFirsDocuments.get(queryId).getValues().entrySet().stream()
                .filter(map -> !gsRelevants.get(queryId).getValues().containsKey(map.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
}
