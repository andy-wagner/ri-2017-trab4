package CorpusReader;

import Documents.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */
public class CorpusReader {
    private List<Document> documents;

    /**
     * Constructor. The classe manages a list of documents.
     */
    public CorpusReader() {
        this.documents = new ArrayList<>();
    }

    /**
     * Add document to list.
     * @param document
     */
    public void addDocument(Document document) {
        // Auto-increment in document's id
        if (document.getId() != documents.size() + 1)
            document.setId(documents.size() + 1);
        this.documents.add(document);
    }

    /**
     * Get list of documents.
     * @return list of documents
     */
    public List<Document> getDocuments() {
        return documents;
    }
    
    /**
     * Set a new document's list
     * @param documents
     */
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
