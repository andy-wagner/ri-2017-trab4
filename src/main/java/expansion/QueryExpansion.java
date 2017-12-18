package expansion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import utils.Values;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExpansion {
    private Map<Integer, Values> relevantDocuments;
    private Map<Integer, Map<String, Double>> termsWeight;
    private static Logger log = LoggerFactory.getLogger(QueryExpansion.class);
            
    public QueryExpansion(Map<Integer, Values> relevantDocuments, Map<Integer, Map<String, Double>> termsWeight) {
        this.relevantDocuments = relevantDocuments;
        this.termsWeight = termsWeight;
        addHigherTermsToQuery();
        //test();
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
    
    private void test() {
        try {
            // Gets Path to Text file
            String filePath = "raw_sentences.txt";
            
            log.info("Load & Vectorize Sentences....");
            // Strip white space before and after for each line
            SentenceIterator iter = new BasicLineIterator(filePath);
            // Split on white spaces in the line to get words
            TokenizerFactory t = new DefaultTokenizerFactory();
            
            /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
            */
            t.setTokenPreProcessor(new CommonPreprocessor());
            
            log.info("Building model....");
            Word2Vec vec = new Word2Vec.Builder()
                    .minWordFrequency(5)
                    .iterations(1)
                    .layerSize(100)
                    .seed(42)
                    .windowSize(5)
                    .iterate(iter)
                    .tokenizerFactory(t)
                    .build();
            
            log.info("Fitting Word2Vec model....");
            vec.fit();
            
            log.info("Writing word vectors to text file....");
            
            // Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
            log.info("Closest Words:");
            Collection<String> lst = vec.wordsNearest("day", 10);
            System.out.println("10 Words closest to 'day': " + lst);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(QueryExpansion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
