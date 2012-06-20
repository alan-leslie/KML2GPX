package KML;

import Files.KMLFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author al
 */
public class KMLDocument {

    private List<Placemark> thePlacemarks = null;
    private Logger theLogger = null;

    public KMLDocument(String fileName, Logger theLogger) {
        this.theLogger = theLogger;
        thePlacemarks = new ArrayList<Placemark>();
        Document theDoc = KMLFile.getFileData(fileName);

        XPath staffXpath = XPathFactory.newInstance().newXPath();
        NodeList theData = null;

        try {
            theData = (NodeList) staffXpath.evaluate("kml/Document/Placemark", theDoc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            theLogger.log(Level.SEVERE, null, ex);
        }

        if (theData != null) {
            int dataLength = theData.getLength();

            for (int i = 0; i < dataLength; ++i) {
                Placemark thePlacemark = new Placemark(theData.item(i));
                thePlacemarks.add(thePlacemark);
            }
        }
    }

    public void printData() {
        int dataLength = thePlacemarks.size();
        int i = 0;

        for (Placemark thePlacemark : thePlacemarks) {
            System.out.println("Placemark no:" + Integer.toString(i));
            System.out.println("Name: " + thePlacemark.getName());
            System.out.println("Description: " + thePlacemark.getDescription());

            PlacemarkItem theItem = thePlacemark.getItem();
            try {
                for (int j = 0; j < theItem.noOfPoints(); ++j) {
                    System.out.println("Coords at : " + Integer.toString(j));
                    System.out.println("Lat: " + Double.toString(theItem.getPointAt(j).getLatitude()));
                    System.out.println("Lon: " + Double.toString(theItem.getPointAt(j).getLongitude()));
                }
            } catch (Exception ex) {
                theLogger.log(Level.SEVERE, null, ex);
            }

            ++i;
        }
    }

    public List<Placemark> getThePlacemarks() {
        return thePlacemarks;
    }
}
