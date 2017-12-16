package expansion;

import java.util.Map;
import utils.Values;

public class QueryExpansion {
    private Map<Integer, Values> relevantDocuments;
            
    public QueryExpansion(Map<Integer, Values> relevantDocuments) {
        this.relevantDocuments = relevantDocuments;
    }
}
