
package Files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.divbyzero.gpx.Coordinate;
import net.divbyzero.gpx.GPX;
import net.divbyzero.gpx.TrackSegment;
import net.divbyzero.gpx.Waypoint;

/**
 *
 * @author al
 */
public class GPSFile {
    public static void writeData(String fileName,
            String theTitle,
            GPX theData) {
        FileWriter theWriter = null;
        try {
            theWriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(theWriter);
            
            out.write("<?xml version=\"1.0\"?>");
            out.newLine();
            out.write("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" creator=\"garmin2gpx.py\" version=\"1.1\" ns0:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/0/gpx.xsd\">");
            out.newLine();
            out.write("<metadata>");
            out.newLine();
            out.write("<name>" + theTitle + "</name>");
            out.newLine();
            
            Coordinate topRight = getTopRight(theData);
            Coordinate bottomLeft = getBottomLeft(theData);
            out.write("<bounds maxlat=\"" + Double.toString(topRight.getLatitude()));
            out.write(" maxlon=\"" + Double.toString(topRight.getLongitude()));
            out.write(" minlat=\"" + Double.toString(bottomLeft.getLatitude()));
            out.write(" minlon=\"" + Double.toString(bottomLeft.getLongitude()));
            out.newLine();
            out.write("</metadata>");
            
            List<Waypoint> theWayPoints = getWayPoints(theData);
            List<TrackSegment> theSegments = getSegments(theData);
  
            
            for (Waypoint theWayPoint : theWayPoints) {
                out.write("<wpt lat=\"" + theWayPoint.getCoordinate().getLatitude());
                out.write(" lon=\"" + theWayPoint.getCoordinate().getLongitude() + ">");
                out.newLine();
                out.write("<desc>" +theWayPoint.getDesc() + "</desc>");
                out.newLine();
                out.write("</wpt>");
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

    private static Coordinate getTopRight(GPX theData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static Coordinate getBottomLeft(GPX theData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static List<Waypoint> getWayPoints(GPX theData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static List<TrackSegment> getSegments(GPX theData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
