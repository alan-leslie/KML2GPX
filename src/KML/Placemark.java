package KML;

import Utils.DomUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author al
 */
public class Placemark {

    private String name = "";
    private String id = "";
    private String description = "";
    private PlacemarkItem item = null;

    public Placemark(Node theNode) {
        name = DomUtils.getTagValue("name", theNode);
        description = DomUtils.getTagValue("description", theNode);
        
        NodeList theData = DomUtils.getXPathNodes(theNode, "./Point");

        if (theData != null && theData.getLength() > 0) {
            Point thePoint = new Point(theData.item(0));
            item = thePoint;
        } else {
            theData = DomUtils.getXPathNodes(theNode, "./LineString");

            if (theData != null && theData.getLength() > 0) {
                LineString theLineString = new LineString(theData.item(0));
                item = theLineString;
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlacemarkItem getItem() {
        return item;
    }
}
