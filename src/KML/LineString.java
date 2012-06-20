
package KML;

import Utils.DomUtils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

/**
 *
 * @author al
 */
public class LineString implements PlacemarkItem {
    String theCoords = "";
    List<PointData> theData = null;
    
    public LineString(Node theNode) {
      theCoords = DomUtils.getTagValue("coordinates", theNode).trim();
      theData = new ArrayList<PointData>();
      String[] splitCoords = theCoords.split("\n");
      
      for(int i = 0; i < splitCoords.length; ++i){
          PointData thePoint = new PointData(splitCoords[i]);
          theData.add(thePoint);
      }     
    }
    
    @Override
    public int noOfPoints() {
        return theData.size();
    }

    @Override
    public PointData getPointAt(int index) throws Exception {
        if(index < 0 || index > noOfPoints()){
        throw new Exception("Out of bounds.");
        } else {
            return theData.get(index);
        }
    }   
}
