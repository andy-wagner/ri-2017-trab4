package Feedback;

import Utils.Values;
import java.util.Map;

public interface RelevanceFeedback {
   public void setTenFirstDocuments(Map<Integer,Values> queries);
   public Map<Integer, Values> getDocuments();
   public Map<Integer, Double> getRelevantDocumentsOfQueries(int queryId);
}
