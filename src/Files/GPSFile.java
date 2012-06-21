package Files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import net.divbyzero.gpx.Coordinate;
import net.divbyzero.gpx.GPX;
import net.divbyzero.gpx.Track;
import net.divbyzero.gpx.TrackSegment;
import net.divbyzero.gpx.Waypoint;

/**
 *
 * @author al
 */
public class GPSFile {

    public static void writeData(String fileName,
            String theTitle,
            double theSpeed,
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
            out.write("<bounds maxlat=\"" + Double.toString(topRight.getLatitude()) + "\"");
            out.write(" maxlon=\"" + Double.toString(topRight.getLongitude()) + "\"");
            out.write(" minlat=\"" + Double.toString(bottomLeft.getLatitude()) + "\"");
            out.write(" minlon=\"" + Double.toString(bottomLeft.getLongitude()) + "\"/>");
            out.newLine();
            out.write("</metadata>");
            out.newLine();
            
            List<Waypoint> theWayPoints = getWayPoints(theData);
            List<TrackSegment> theSegments = getSegments(theData);


            for (Waypoint theWayPoint : theWayPoints) {
                out.write("<wpt lat=\"" + theWayPoint.getCoordinate().getLatitude() + "\"");
                out.write(" lon=\"" + theWayPoint.getCoordinate().getLongitude() + "\">");
                out.newLine();
                out.write("<desc>" + theWayPoint.getDesc() + "</desc>");
                out.newLine();
                out.write("</wpt>");
                out.newLine();
            }

            if (!theSegments.isEmpty()) {
                out.write("<trk>");
                out.newLine();
                Calendar theDate = new GregorianCalendar();
                theDate.setTimeZone(TimeZone.getTimeZone("Europe/London"));
                Date now = new Date();
                theDate.setTimeInMillis(now.getTime());
                SimpleDateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                theDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
                SimpleDateFormat theTimeFormat = new SimpleDateFormat("HH:mm:ss");
                theDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
                
                for (TrackSegment theSegment : theSegments) {
                    out.write("<trkseg>");
                    out.newLine();
                    
                    List<Waypoint> trackWayPoints = theSegment.getWaypoints();
                    Waypoint prevWayPoint = null;
                 
                    for(Waypoint theWayPoint: trackWayPoints){
                        if(prevWayPoint != null){
                            double theLength = theWayPoint.calculateDistanceTo(prevWayPoint);
                            double theTime = theLength/theSpeed;
                            theDate.setTimeInMillis(theDate.getTimeInMillis() + (long)(theTime * 1000));
                        }

                        out.write("<trkpt lat=\"" + theWayPoint.getCoordinate().getLatitude() + "\"");
                        out.write(" lon=\"" + theWayPoint.getCoordinate().getLongitude() + "\">");
                        out.newLine();
                        out.write("<time>");
                        Date dateIncrement = theDate.getTime();
                        String format = theDateFormat.format(dateIncrement) + "T" + theTimeFormat.format(dateIncrement) + "Z";
                        out.write(format);
                        out.write("</time>");
                        out.newLine();
                        out.write("<ele>" + "0.0" + "</ele>");
                        out.newLine();
                        out.write("</trkpt>");
                        out.newLine();
                        
                        prevWayPoint = theWayPoint;
                    }
                    
                    out.write("</trkseg>");
                    out.newLine();
                }
                
                out.write("</trk>");
                out.newLine();
            }

            out.write("</gpx>");
            out.newLine();

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
        Coordinate retVal = new Coordinate();
        retVal.setLatitude(-90.0);
        retVal.setLongitude(-180.0);
        
        ArrayList<Track> tracks = theData.getTracks();
        for(Track theTrack : tracks){
            for(TrackSegment theSegment : theTrack.getSegments()){
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                for(Waypoint theWayPoint: waypoints){
                    Coordinate theCoord = theWayPoint.getCoordinate();
                    if(theCoord.getLatitude() > retVal.getLatitude()){
                        retVal.setLatitude(theCoord.getLatitude());
                    }
                    if(theCoord.getLongitude() > retVal.getLongitude()){
                        retVal.setLongitude(theCoord.getLongitude());
                    }              
                }
            }
        }
        
        return retVal;
    }

    private static Coordinate getBottomLeft(GPX theData) {
        Coordinate retVal = new Coordinate();
        retVal.setLatitude(90.0);
        retVal.setLongitude(180.0);
        
        ArrayList<Track> tracks = theData.getTracks();
        for(Track theTrack : tracks){
            for(TrackSegment theSegment : theTrack.getSegments()){
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                for(Waypoint theWayPoint: waypoints){
                    Coordinate theCoord = theWayPoint.getCoordinate();
                    if(theCoord.getLatitude() < retVal.getLatitude()){
                        retVal.setLatitude(theCoord.getLatitude());
                    }
                    if(theCoord.getLongitude() < retVal.getLongitude()){
                        retVal.setLongitude(theCoord.getLongitude());
                    }              
                }
            }
        }
        
        return retVal;
    }

    private static List<Waypoint> getWayPoints(GPX theData) {
        List<Waypoint> retVal = new ArrayList<Waypoint>();
        
        ArrayList<Track> tracks = theData.getTracks();
        for(Track theTrack : tracks){
            for(TrackSegment theSegment : theTrack.getSegments()){
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                if(waypoints.size() == 1){
                    retVal.add(waypoints.get(0));
                }
            }
        }
        
        return retVal;
    }

    private static List<TrackSegment> getSegments(GPX theData) {
        List<TrackSegment> retVal = new ArrayList<TrackSegment>();
        
        ArrayList<Track> tracks = theData.getTracks();
        for(Track theTrack : tracks){
            for(TrackSegment theSegment : theTrack.getSegments()){
                ArrayList<Waypoint> waypoints = theSegment.getWaypoints();
                if(waypoints.size() > 1){
                    retVal.add(theSegment);
                }
            }
        }
        
        return retVal;
    }
}
