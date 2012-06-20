/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author al
 */
public class PeopleFile {

    public static void writeData(String fileName,
            Collection<String> theData) {
        FileWriter theWriter = null;
        try {
            theWriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(theWriter);

            for (String theItem : theData) {
                out.write(theItem);
                out.newLine();
            }

            out.flush();
        } catch (IOException e) {
            // ...
        } finally {
            if (null != theWriter) {
                try {
                    theWriter.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }
    }
}
