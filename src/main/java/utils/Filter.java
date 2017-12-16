package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.tartarus.snowball.ext.englishStemmer;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */

/**
 * Class that applys stopWording and stemming
 */
public class Filter {
    private final List<String> stopWords;
    
    /**
     * Constructor
     * @throws java.io.FileNotFoundException
     */
    public Filter() throws FileNotFoundException {
        stopWords = new ArrayList<>();
    }
    
    /**
     * Read the file of stopwords and load them to a list
     * @param file
     * @throws FileNotFoundException 
     */
    public void loadStopwords(File file) throws FileNotFoundException {
        if (!file.exists()) {
            System.err.println("ERROR: File of stopwords not found!");
            System.exit(1);
        }
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String word = sc.nextLine();
            // String.trim() to remove white spaces after text
            if (word.trim().length() > 0)
                stopWords.add(word.trim());
        }
    }
    
    /**
     * Apply the stopwording filtering
     * If there's any term in the stopwording list, it'll be removed from terms list
     * @param terms
     * @return filtered words
     */
    public Map<Integer, List<String>> stopwordsFiltering(Map<Integer, List<String>> terms) {
        Map<Integer, List<String>> result = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : terms.entrySet()) {
            List<String> words = entry.getValue();
            List<String> filter = words.stream()
                    .filter(line -> !stopWords.contains(line))
                    .collect(Collectors.toList());
            result.put(entry.getKey(), filter);
        }
        return result;
    }
    
    /**
     * Stemming terms
     * @param terms
     * @return terms
     */
    public Map<Integer, List<String>> stemmingWords(Map<Integer, List<String>> terms) {
        Map<Integer, List<String>> result = new HashMap<>();
        englishStemmer stemmer = new englishStemmer();
        for (Map.Entry<Integer, List<String>> entry : terms.entrySet()) {
            List<String> words = entry.getValue();
            for (int i = 0; i < words.size(); i++) {
                String x = words.get(i);
                stemmer.setCurrent(words.get(i));
                if (stemmer.stem()) {
                    words.set(i, stemmer.getCurrent());
                }
            }
            result.put(entry.getKey(), words);
        }
        return result;
    } 
}
