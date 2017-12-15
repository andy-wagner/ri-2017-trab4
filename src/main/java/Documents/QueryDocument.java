package Documents;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */
public class QueryDocument extends Document{
    
    /**
     * Class that extends Document abstract class
     * Class that represents Query Files
     * Constructor.
     */
    public QueryDocument() { }
    
    /**
     * Constructor with Document's id and text
     * @param id
     * @param text 
     */
    public QueryDocument(int id, String text) {
        super(id, text);
    }
    
    /**
     * Print object
     * @return string 
     */
    @Override
    public String toString() {
        return "Query: {id: " + super.getId() + "; text: " + super.getText() + "}";
    }
}
