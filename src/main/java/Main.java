import corpus.CorpusReader;
import documents.Document;
import documents.GSDocument;
import expansion.QueryExpansion;
import feedback.ExplicitRelevance;
import feedback.Rocchio;
import indexer.IndexerCreator;
import parsers.GSParser;
import weighters.DocumentWeighter;
import parsers.Parser;
import parsers.QueryParser;
import parsers.XMLParser;
import tokenizers.CompleteTokenizer;
import utils.Filter;
import utils.Values;
import weighters.QueryWeighter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IR, December 2017
 *
 * Assignment 4 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * 
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Arguments: documents; stopwords; queries; queries with relevance; indexer with weights; ranked query's results
        if (args.length == 6) {
            File file = new File(args[0]);
            if (!file.exists()) {
                System.err.println("ERROR: Files to read not found!");
                System.exit(1);
            }
            // The documents, the indexer, the ranked results and the queries must have unique names
            if (args[2].equals(args[0]) || args[4].equals(args[0]) || args[5].equals(args[0])) {
                System.err.println("ERROR: The file you want to create was read before!");
                System.exit(1);
            }
            Parser parser = new Parser(new XMLParser());
            CorpusReader documentReader = new CorpusReader();
            // In case of filename is a directory
            if (file.isDirectory())
                documentReader.setDocuments(parser.parseDir(file));
            // In case of a filename is an only file
            else
                documentReader.addDocument((Document) parser.parseFile(file));
            List<Document> documents = documentReader.getDocuments();
            CompleteTokenizer documentTokenizer = new CompleteTokenizer();
            // Tokenize all documents
            documentTokenizer.tokenize(documents);
            Map<Integer, List<String>> documentTerms = documentTokenizer.getTerms();
            Filter filter = new Filter();
            // Load stopwords
            filter.loadStopwords(new File(args[1]));
            // Apply stopwording filtering
            documentTerms = filter.stopwordsFiltering(documentTerms);
            // Apply stemming
            documentTerms = filter.stemmingWords(documentTerms);
            IndexerCreator documentIndexCreator = new IndexerCreator(documentTerms);
            // Create an indexer
            documentIndexCreator.createIndexer();
            Map<String, Values> documentIndexer = documentIndexCreator.getIndexer();
            // Calculate weights of documents
            DocumentWeighter documentWeighter = new DocumentWeighter(documentTerms);
            // Calculate tf
            documentWeighter.calculateTermFreq(documentIndexer);
            // Write results to file
            documentWeighter.writeToFile(new File(args[4]));

            File queriesFile = new File(args[2]);
            if (!queriesFile.exists()) {
                System.err.println("ERROR: Files of queries not found!");
                System.exit(1);
            }
            parser = new Parser(new QueryParser());
            CorpusReader queryReader = new CorpusReader();
            queryReader.setDocuments((List<Document>)parser.parseFile(queriesFile));
            List<Document> queryDocuments = queryReader.getDocuments();
            CompleteTokenizer queryTokenizer = new CompleteTokenizer();
            // Tokenize terms of queries
            queryTokenizer.tokenize(queryDocuments);
            Map<Integer, List<String>> queries = queryTokenizer.getTerms();
            // Apply stopwording filtering
            queries = filter.stopwordsFiltering(queries);
            // Apply stemming
            queries = filter.stemmingWords(queries);
            // Create an indexer for queries to calculate weights later
            IndexerCreator queryCreator = new IndexerCreator(queries);
            queryCreator.createIndexer();
            Map<String, Values> queryIndexer = queryCreator.getIndexer();
            // Calculate weights of queries
            QueryWeighter queryWeighter = new QueryWeighter(queryIndexer, queries, documents.size());
            // Calculate idf
            queryWeighter.calculateInverseDocFreq(queryIndexer, documentIndexer);
            // Calculate score of document
            queryWeighter.calculateDocumentScore(documentIndexer);
            // Write results to file
            queryWeighter.writeToFile(new File(args[5]));
            // EndTime of processing queries
            Map<Integer, Values> queryScorer = queryWeighter.getQueryScorer();
            // Gold Standard file
            File gsFile = new File(args[3]);
            if (!gsFile.exists()) {
                System.err.println("ERROR: File of Gold Standard not found!");
                System.exit(1);
            }
            parser = new Parser(new GSParser());
            // Parse file of queries relevances
            GSDocument gsDocument = (GSDocument) parser.parseFile(gsFile);
            Map<Integer, Values> gsRelevants = gsDocument.getRelevants();
            ExplicitRelevance explicit = new ExplicitRelevance(gsRelevants);
            explicit.setTenFirstDocuments(queryScorer);
            Map<Integer, Values> feedback = explicit.getDocuments();
            Map<Integer, Values> relevantFeedback = new HashMap<>();
            Map<Integer, Values> nonRelevantFeedback = new HashMap<>();
            for (Map.Entry<Integer, Values> query : feedback.entrySet()) {
                int queryId = query.getKey();
                Values relevant = new Values(explicit.getRelevantDocuments(queryId));
                relevantFeedback.put(queryId, relevant);
                Values nonRelevant = new Values(explicit.getNonRelevantDocuments(queryId));
                nonRelevantFeedback.put(queryId, nonRelevant);
            }
            Rocchio rocchio = new Rocchio(queries, feedback, documentWeighter.getIndexer(), relevantFeedback, nonRelevantFeedback);
            rocchio.calculateRocchio();
            Map<Integer, Map<String, Double>> termsWeight = rocchio.getTermsWeights();
            QueryExpansion expansion = new QueryExpansion(relevantFeedback, termsWeight);
        } else {
            System.err.println("ERROR: Invalid number of arguments!");
            System.out.println("USAGE: <file/dir> <stopwords> <queries> <gold standard> <indexer weights> <ranked queries>");
        }
    }
}
