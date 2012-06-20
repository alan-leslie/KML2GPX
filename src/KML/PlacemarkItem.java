
package KML;

/**
 *
 * @author al
 */
public interface PlacemarkItem {
    int noOfPoints();
    PointData getPointAt(int index) throws Exception; 
}
