package Conversion;

import KML.KMLDocument;
import KML.Placemark;
import KML.PointData;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.divbyzero.gpx.Coordinate;
import net.divbyzero.gpx.GPX;
import net.divbyzero.gpx.Track;
import net.divbyzero.gpx.TrackSegment;
import net.divbyzero.gpx.Waypoint;

/**
 *
 * @author al
 */
public class Converter {

    private KMLDocument theDoc = null;
    private Logger theLogger = null;

    public Converter(KMLDocument theDoc, Logger theLogger) {
        this.theDoc = theDoc;
        this.theLogger = theLogger;
    }

    public KMLDocument getTheDoc() {
        return theDoc;
    }

    public GPX convert() {
        GPX theGPX = new GPX();
        List<Placemark> thePlacemarks = theDoc.getThePlacemarks();

        for (Placemark thePlacemark : thePlacemarks) {
            Track theTrack = convertPlacemark(thePlacemark);
            theGPX.addTrack(theTrack);
        }

        return theGPX;
    }

    private Track convertPlacemark(Placemark thePlacemark) {
        Track theTrack = new Track();
        int noOfPoints = thePlacemark.getItem().noOfPoints();

        try {
            if (noOfPoints == 1) {
                Waypoint theWayPoint = new Waypoint();
                theWayPoint.setName(thePlacemark.getName());
                theWayPoint.setDesc(thePlacemark.getDescription());
                PointData pointAt = thePlacemark.getItem().getPointAt(0);
                Coordinate theCoords = new Coordinate();
                theCoords.setLatitude(pointAt.getLatitude());
                theCoords.setLongitude(pointAt.getLongitude());
                theWayPoint.setCoordinate(theCoords);
                TrackSegment theSegment = new TrackSegment();
                theSegment.addWaypoint(theWayPoint);
                theTrack.addSegment(theSegment);
            } else {
                TrackSegment theSegment = new TrackSegment();
                for (int i = 0; i < noOfPoints; ++i) {
                    Waypoint theWayPoint = new Waypoint();
                    PointData pointAt = thePlacemark.getItem().getPointAt(i);
                    Coordinate theCoords = new Coordinate();
                    theCoords.setLatitude(pointAt.getLatitude());
                    theCoords.setLongitude(pointAt.getLongitude());
                    theWayPoint.setCoordinate(theCoords);
                    theSegment.addWaypoint(theWayPoint);
                }
                theTrack.addSegment(theSegment);
            }
        } catch (Exception ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return theTrack;
    }
}
