package Feedback;

import Utils.Values;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<Integer, Double> getRelevantDocumentsOfQueries(int queryId) {
        Map<Integer, Double> relevantDocuments = new HashMap<>();
        Values queryDocuments = tenFirsDocuments.get(queryId);
        Values gsDocuments = gsRelevants.get(queryId);
        if (queryDocuments != null && gsDocuments != null) {
            for (Map.Entry<Integer, Double> document : queryDocuments.getValues().entrySet()) {
                if (gsDocuments.getValues().containsKey(document.getKey()))
                    relevantDocuments.put(document.getKey(), document.getValue());
            }
        }
        return relevantDocuments;
    }
}
