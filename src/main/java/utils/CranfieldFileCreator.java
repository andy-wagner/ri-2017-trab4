package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CranfieldFileCreator {
    private final File file;
    
    public CranfieldFileCreator(String filename) {
        file = new File(filename);
    }
    
    public void appendContent(String []content) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.err.println("ERROR: Can't create new file!");
                System.exit(1);
            }
        }
        try {
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            for (String term : content)
                bw.write(term);
        } catch (IOException ex) {
            System.err.println("ERROR: File not found!");
            System.exit(1);
        } finally{
            try {
                if (bw != null)    
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                System.err.println("ERROR: Can't close files!");
                System.exit(1);
            }
        }
    }
}
