package feedback;

import utils.Values;
import java.util.Map;

public interface RelevanceFeedback {
   public void setTenFirstDocuments(Map<Integer,Values> queries);
   public Map<Integer, Values> getDocuments();
   public Map<Integer, Double> getRelevantDocuments(int queryId);
   public Map<Integer, Double> getNonRelevantDocuments(int queryId);
}
