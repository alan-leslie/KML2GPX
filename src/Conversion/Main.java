package Conversion;

/*
 * 
 *
 */
import Files.GPSFile;
import KML.KMLDocument;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import net.divbyzero.gpx.GPX;

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

        String speedStr = properties.getProperty("Speed", "1.3");
        double theSpeed = Double.parseDouble(speedStr);
        String theFile = properties.getProperty("FileName", "WalkingDirectionsFromVictoriaToBoda.kml");
        String theOutputDir = properties.getProperty("OutputDir", ".");

        Logger theLogger = Main.makeLogger();
        
        KMLDocument theKML = new KMLDocument(theFile, theLogger);
        theKML.printData();
        Converter theConv = new Converter(theKML, theLogger);
        GPX theGPX = theConv.convert(theSpeed);
        GPSFile.writeData("Output.gpx", "Swedish Pub Crawl", theGPX);
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
