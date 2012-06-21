package Conversion;

import KML.KMLDocument;
import KML.Placemark;
import KML.PointData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
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
 * TDOD - remove dupicate points
 * add intermediate points
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

    public GPX convert(double theSpeed) {
        GPX theGPX = new GPX();
        List<Placemark> thePlacemarks = theDoc.getThePlacemarks();

        for (Placemark thePlacemark : thePlacemarks) {
            Track theTrack = convertPlacemark(thePlacemark);
            theGPX.addTrack(theTrack);
        }

        setTimes(theSpeed, theGPX);

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
                Waypoint prevWayPoint = null;
                for (int i = 0; i < noOfPoints; ++i) {
                    Waypoint theWayPoint = new Waypoint();
                    PointData pointAt = thePlacemark.getItem().getPointAt(i);
                    Coordinate theCoords = new Coordinate();
                    theCoords.setLatitude(pointAt.getLatitude());
                    theCoords.setLongitude(pointAt.getLongitude());
                    theWayPoint.setCoordinate(theCoords);
                    
                    if(prevWayPoint == null){
                        theSegment.addWaypoint(theWayPoint);
                    } else {                     
                        if(!theCoords.equals(prevWayPoint.getCoordinate())){
                            theSegment.addWaypoint(theWayPoint);                            
                        }
                    }
                    
                    prevWayPoint = theWayPoint;
                }
                theTrack.addSegment(theSegment);
            }
        } catch (Exception ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return theTrack;
    }

    public static Coordinate getTopRight(GPX theData) {
        Coordinate retVal = new Coordinate();
        retVal.setLatitude(-90.0);
        retVal.setLongitude(-180.0);

        ArrayList<Track> tracks = theData.getTracks();
        for (Track theTrack : tracks) {
            for (TrackSegment theSegment : theTrack.getSegments()) {
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                for (Waypoint theWayPoint : waypoints) {
                    Coordinate theCoord = theWayPoint.getCoordinate();
                    if (theCoord.getLatitude() > retVal.getLatitude()) {
                        retVal.setLatitude(theCoord.getLatitude());
                    }
                    if (theCoord.getLongitude() > retVal.getLongitude()) {
                        retVal.setLongitude(theCoord.getLongitude());
                    }
                }
            }
        }

        return retVal;
    }

    public static Coordinate getBottomLeft(GPX theData) {
        Coordinate retVal = new Coordinate();
        retVal.setLatitude(90.0);
        retVal.setLongitude(180.0);

        ArrayList<Track> tracks = theData.getTracks();
        for (Track theTrack : tracks) {
            for (TrackSegment theSegment : theTrack.getSegments()) {
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                for (Waypoint theWayPoint : waypoints) {
                    Coordinate theCoord = theWayPoint.getCoordinate();
                    if (theCoord.getLatitude() < retVal.getLatitude()) {
                        retVal.setLatitude(theCoord.getLatitude());
                    }
                    if (theCoord.getLongitude() < retVal.getLongitude()) {
                        retVal.setLongitude(theCoord.getLongitude());
                    }
                }
            }
        }

        return retVal;
    }

    public static List<Waypoint> getWayPoints(GPX theData) {
        List<Waypoint> retVal = new ArrayList<Waypoint>();

        ArrayList<Track> tracks = theData.getTracks();
        for (Track theTrack : tracks) {
            for (TrackSegment theSegment : theTrack.getSegments()) {
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                if (waypoints.size() == 1) {
                    retVal.add(waypoints.get(0));
                }
            }
        }

        return retVal;
    }

    public static List<TrackSegment> getSegments(GPX theData) {
        List<TrackSegment> retVal = new ArrayList<TrackSegment>();

        ArrayList<Track> tracks = theData.getTracks();
        for (Track theTrack : tracks) {
            for (TrackSegment theSegment : theTrack.getSegments()) {
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                if (waypoints.size() > 1) {
                    retVal.add(theSegment);
                }
            }
        }

        return retVal;
    }

    private void setTimes(double theSpeed, GPX theData) {
        Calendar theDate = new GregorianCalendar();
        theDate.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Date now = new Date();
        theDate.setTimeInMillis(now.getTime());

        List<TrackSegment> theSegments = Converter.getSegments(theData);
        if (!theSegments.isEmpty()) {
            for (TrackSegment theSegment : theSegments) {
                List<Waypoint> trackWayPoints = theSegment.getWaypoints();
                Waypoint prevWayPoint = null;

                for (Waypoint theWayPoint : trackWayPoints) {
                    if (prevWayPoint != null) {
                        double theLength = theWayPoint.calculateDistanceTo(prevWayPoint);
                        double theTime = theLength / theSpeed;
                        theDate.setTimeInMillis(theDate.getTimeInMillis() + (long) (theTime * 1000));
                    }

                    theWayPoint.setTime(theDate.getTime());
                    prevWayPoint = theWayPoint;
                }
            }
        }
    }

    public void addIntermediatePoints(GPX theData) {
        ArrayList<Track> tracks = theData.getTracks();
        double length = 0.0;
        for (Track theTrack : tracks) {
            length += theTrack.length();
        }
        double lengthIncrement = length / 100.0;
        
        List<TrackSegment> theSegments = Converter.getSegments(theData);
        if (!theSegments.isEmpty()) {
            for (TrackSegment theSegment : theSegments) {
                List<Waypoint> trackWayPoints = theSegment.getWaypoints();
                Waypoint prevWayPoint = null;   
            }
        }
    }
}
