package documents;

/**
 * IR, November 2017
 *
 * Assignment 3 
 *
 * @author Tiago Faria, 73714, tiagohpf@ua.pt
 * @author David dos Santos Ferreira, 72219, davidsantosferreira@ua.pt
 * 
 */
public abstract class Document {
    private int id;
    private String text;
    
    /**
     * Constructor. Abstract Class that represents a Document
     */
    public Document() { }
    
    /**
     * Constructor. A Document's object uses an id
     * @param id
     */
    public Document(int id) {
        this.id = id;
    }
    
    /**
     * Constructor. A Document's object uses an id and a text
     * @param id
     * @param text
     */
    public Document(int id, String text) {
        this.id = id;
        this.text = text;
    }

    /**
     * Get Document's id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set a new id for the Document
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get Document's text
     * @return text
     */
    public String getText() {
       
        return text;
    }

    /**
     * Set a new text for the Document
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    } 
}