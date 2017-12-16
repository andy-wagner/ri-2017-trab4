package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */

// Class that represent the value of a Map
public class Values {
    private Map<Integer, Double> values;

    // Constructor
    public Values() {
        values = new HashMap<>();
    }
    
    /**
     * Constructor
     * @param values 
     */
    public Values(Map<Integer, Double> values)  {
        this.values = values;
    }

    /**
     * Get all the values
     * @return values 
     */
    public Map<Integer, Double> getValues() {
        return values;
    }

    /**
     * Add a new value
     * @param id
     * @param score 
     */
    public void addValue(int id, double score) {
        values.put(id, score);
    }
    
    /**
     * Set the new values
     * @param values 
     */
    public void setValues(Map<Integer, Double> values) {
        this.values = values;
    }
    
    /**
     * Sort the values
     * The values are sorted by score and by id
     * @return sorted values
     */
    public Map<Integer, Double> sortValues() {
        List<Map.Entry<Integer,Double>> entries = new ArrayList<>(values.entrySet());
        Collections.sort(entries, (Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) 
                -> o1.getValue().compareTo(o2.getValue()) * -1);
        Collections.sort(entries, (Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) -> {
            int res = 0;
            if (Objects.equals(o1.getValue(), o2.getValue()))
                res = o1.getKey().compareTo(o2.getKey());
            return res;
        });
        // Create new map with sorted results
        Map<Integer, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry: entries)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }
}
