package expansion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import utils.Values;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class QueryExpansion {
    private Map<Integer, Values> relevantDocuments;
    private Map<Integer, Map<String, Double>> termsWeight;
            
    public QueryExpansion(Map<Integer, Values> relevantDocuments, Map<Integer, Map<String, Double>> termsWeight) {
        this.relevantDocuments = relevantDocuments;
        this.termsWeight = termsWeight;
        addSimilarTermsToQuery();
        //addHigherTermsToQuery();
    }
    
    private void addSimilarTermsToQuery() {
        try {
            String filename = "cranfield_sentences.txt";
            // Strip white space before and after for each line
            SentenceIterator iter = new BasicLineIterator(filename);
            // Split on white spaces in the line to get words
            TokenizerFactory t = new DefaultTokenizerFactory();
            /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
            */
            t.setTokenPreProcessor(new CommonPreprocessor());
            Word2Vec vec = new Word2Vec.Builder()
                    .minWordFrequency(5)
                    .iterations(1)
                    .layerSize(100)
                    .seed(42)
                    .windowSize(5)
                    .iterate(iter)
                    .tokenizerFactory(t)
                    .build();
            vec.fit();
            for (Map.Entry<Integer, Map<String, Double>> query : termsWeight.entrySet()) {
                Map<String, Double> newTerms = new HashMap<>();
                Map<String, Double> terms = query.getValue();
                for (Map.Entry<String, Double> term : terms.entrySet()) {
                    newTerms.put(term.getKey(), term.getValue());
                    Collection<String> lst = vec.wordsNearest(term.getKey(), 3);
                    for (String word : lst)
                        newTerms.put(word, -1.0);
                }
                termsWeight.put(query.getKey(), newTerms);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: File of sentences not found!");
            System.exit(1);
        }
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
