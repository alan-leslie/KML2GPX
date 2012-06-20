package Conversion;

/*
 * 
 *
 */
import KML.KMLDocument;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author al
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    // TODO -
    public static void main(String[] args) {
        Properties properties = new Properties();
        FileInputStream is = null;

        try {
            is = new FileInputStream("KML2GPS.properties");
            properties.load(is);
        } catch (IOException e) {
            // ...
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }

        String speedStr = properties.getProperty("Speed", "3");
        int theSpeed = Integer.parseInt(speedStr);
        String theFile = properties.getProperty("Filename", "WalkingDirectionsFromVictoriaToBoda.kml");
        String theOutputDir = properties.getProperty("OutputDir", ".");

        Logger theLogger = Main.makeLogger();
        
        KMLDocument theKML = new KMLDocument(theFile, theLogger);
        theKML.printData();
    }

    /**
     *
     * @return - valid logger (single file).
     */
    static Logger makeLogger() {
        Logger lgr = Logger.getLogger("KML2GPS");
        lgr.setUseParentHandlers(false);
        lgr.addHandler(simpleFileHandler());
        return lgr;
    }

    /**
     *
     * @return - valid file handler for logger.
     */
    private static FileHandler simpleFileHandler() {
        try {
            FileHandler hdlr = new FileHandler("KML2GPS.log");
            hdlr.setFormatter(new SimpleFormatter());
            return hdlr;
        } catch (Exception e) {
            System.out.println("Failed to create log file");
            return null;
        }
    }
}
