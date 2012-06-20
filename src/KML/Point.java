package KML;

import Utils.DomUtils;
import org.w3c.dom.Node;

/**
 *
 * @author al
 */
public class Point implements PlacemarkItem {
    String theCoords = "";
    PointData theData = null;

    public Point(Node theNode) {
        theCoords = DomUtils.getTagValue("coordinates", theNode);
        theData = new PointData(theCoords);
    }

    @Override
    public int noOfPoints() {
        return 1;
    }

    @Override
    public PointData getPointAt(int index) throws Exception {
        if (index == 0) {
            return theData;
        } else {
            throw new Exception("Out of bounds.");
        }
    }
}
