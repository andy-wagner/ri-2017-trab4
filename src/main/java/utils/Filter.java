package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    
   public List<String> getStopWords() {
       return stopWords;
   }

    public String stemmingTerm(String term) {
        englishStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(term);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return term;
    } 
}
