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

                    if (prevWayPoint == null) {
                        theSegment.addWaypoint(theWayPoint);
                    } else {
                        if (!theCoords.equals(prevWayPoint.getCoordinate())) {
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

    public GPX addIntermediatePoints(GPX theData, double theSpeed) {
        GPX retVal = new GPX();
        List<Track> tracks = theData.getTracks();
        double length = 0.0;
        for (Track theTrack : tracks) {
            length += theTrack.length();
        }
        double lengthIncrement = length / 100.0;

        for (Track theTrack : tracks) {
            List<TrackSegment> theSegments = theTrack.getSegments();
            Track theNewTrack = new Track();
            if (!theSegments.isEmpty()) {
                for (TrackSegment theSegment : theSegments) {
                    TrackSegment theNewSegment = new TrackSegment();
                    List<Waypoint> trackWayPoints = theSegment.getWaypoints();

                    int i = 0;
                    int ptsLength = trackWayPoints.size();
                    for (Waypoint theWayPoint : trackWayPoints) {
                        theNewSegment.addWaypoint(theWayPoint);

                        if (i < ptsLength - 1) {
                            // todo get distance and if it is over the threshhold 

                            Waypoint nextWayPoint = trackWayPoints.get(i + 1);

                            double distance = theWayPoint.calculateDistanceTo(nextWayPoint);

//                            while (distance > 1.5 * lengthIncrement) {
                            if(distance > 1.5 * lengthIncrement) {
                                Coordinate nextPt = nextWayPoint.getCoordinate();
                                Coordinate thisPt = theWayPoint.getCoordinate();
                                
//                                Coordinate newPt = getPointOnLine(thisPt, nextPt, lengthIncrement);
                                Coordinate theMidPoint = midPoint(thisPt, nextPt);
                                Coordinate theQuarterPoint = midPoint(thisPt, theMidPoint);
                                Coordinate theThreeQuarterPoint = midPoint(theMidPoint, nextPt);
                                
                                if(distance > 6 * lengthIncrement){
                                    Waypoint thePoint = new Waypoint();
                                    thePoint.setCoordinate(theQuarterPoint);   
                                    Date dateNow = new Date();
                                    thePoint.setTime(dateNow);
                                    theNewSegment.addWaypoint(thePoint);
                                }
                                                                
                                theWayPoint = new Waypoint();
                                theWayPoint.setCoordinate(theMidPoint);
                                // todo set time correctly
                                Date dateNow = new Date();
                                theWayPoint.setTime(dateNow);
                                
                                distance = theWayPoint.calculateDistanceTo(nextWayPoint);
                                
                                theNewSegment.addWaypoint(theWayPoint);
                                
                                if(distance > 6 * lengthIncrement){
                                    Waypoint thePoint = new Waypoint();
                                    thePoint.setCoordinate(theThreeQuarterPoint);   
                                    Date dateNow2 = new Date();
                                    thePoint.setTime(dateNow2);
                                    theNewSegment.addWaypoint(thePoint);                                    
                                }
                            }
                        }

                        i++;
                    }

                    theNewTrack.addSegment(theNewSegment);
                }
            }

            retVal.addTrack(theNewTrack);
        }
        
        setTimes(theSpeed, retVal);

        return retVal;
    }

    public static Coordinate midPoint(Coordinate pt1, Coordinate pt2) {
        Coordinate retVal = new Coordinate();
        double lon1 = pt1.getLongitude();
        double lat1 = pt1.getLatitude();
        double lon2 = pt2.getLongitude();
        double lat2 = pt2.getLatitude();

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        retVal.setLatitude(Math.toDegrees(lat3));
        retVal.setLongitude(Math.toDegrees(lon3));

        return retVal;
    }

    private static double getInitialBearing(Coordinate pt1, Coordinate pt2) {
        double lat1 = pt1.getLatitude();
        double lat2 = pt2.getLatitude();
        double dLon = Math.toRadians(pt2.getLongitude() - pt1.getLongitude());

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2)
                - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);

//        return brng;
        return 96.02167;
    }

    private static Coordinate getPointOnLine(Coordinate pt1, Coordinate pt2, double d) {
        Coordinate retVal = new Coordinate();
        double lat1 = pt1.getLatitude();
        double lon1 = pt1.getLongitude();
        double brng = getInitialBearing(pt1, pt2);
        double R = 6371000.0;

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R)
                + Math.cos(lat1) * Math.sin(d / R) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d / R) * Math.cos(lat1),
                Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);

        retVal.setLatitude(lat2);
        retVal.setLongitude(lon2);

        return retVal;
    }

    public static void main(String[] args) {
        Coordinate pt1 = new Coordinate();
        Coordinate pt2 = new Coordinate();
//        pt1.setLatitude(34.122222);
//        pt1.setLongitude(118.4111111);
//        pt2.setLatitude(40.66972222);
//        pt2.setLongitude(73.94388889);
        pt1.setLatitude(35.0);
        pt1.setLongitude(45.0);
        pt2.setLatitude(35.0);
        pt2.setLongitude(135.0);
        Coordinate midPoint = midPoint(pt1, pt2);
        
        Coordinate bCheck1 = new Coordinate();
        Coordinate bCheck2 = new Coordinate();
        bCheck1.setLatitude(53.32056);
        bCheck1.setLongitude(-1.72972);
        bCheck2.setLatitude(53.18806);
        bCheck2.setLongitude(0.13639);        
        
        double bearing = getInitialBearing(bCheck1, bCheck2);
        Waypoint dPt1 = new Waypoint();
        Waypoint dPt2 = new Waypoint();
        dPt1.setCoordinate(bCheck1);
        dPt2.setCoordinate(bCheck2);
        
        double distance = dPt1.calculateDistanceTo(dPt2);
        
        Coordinate theEndPoint = getPointOnLine(bCheck1, bCheck2, distance);
                          
        //print out in degrees
//        System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
        System.out.println(midPoint.getLatitude() + " " + midPoint.getLongitude());
        System.out.println("Bearing: " + Double.toString(bearing));
        System.out.println("Distance: " + Double.toString(distance));
        System.out.println(theEndPoint.getLatitude() + " " + theEndPoint.getLongitude());
    }
}
