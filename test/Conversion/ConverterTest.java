/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conversion;

import net.divbyzero.gpx.Waypoint;
import net.divbyzero.gpx.TrackSegment;
import java.util.ArrayList;
import java.util.logging.Logger;
import KML.KMLDocument;
import net.divbyzero.gpx.GPX;
import net.divbyzero.gpx.Track;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author al
 */
public class ConverterTest {

    public ConverterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConversion() {
        Logger theLogger = Main.makeLogger();   
        KMLDocument theKML = new KMLDocument("WalkingDirectionsFromVictoriaToBoda.kml", theLogger);
        
        Converter theConverter = new Converter(theKML, theLogger);
        GPX theGPX = theConverter.convert();
        ArrayList<Track> tracks = theGPX.getTracks();
        assertEquals(3,tracks.size());
        
        try{
            Track firstTrack = tracks.get(0);
            TrackSegment theSegment = firstTrack.getSegments().get(0);
            Waypoint thePoint = theSegment.getWaypoints().get(0);
            double kmlLat = theKML.getThePlacemarks().get(0).getItem().getPointAt(0).getLatitude();
            double kmlLon = theKML.getThePlacemarks().get(0).getItem().getPointAt(0).getLongitude();
            assertEquals(kmlLat, thePoint.getCoordinate().getLatitude(), 0.001);
            assertEquals(kmlLon, thePoint.getCoordinate().getLongitude(), 0.001);
            
            Track midTrack = tracks.get(1);
            TrackSegment midTrackSegment = midTrack.getSegments().get(0);
            assertEquals(2, midTrackSegment.getWaypoints().size());
            Waypoint firstPoint = midTrackSegment.getWaypoints().get(0);
            double kmlLat1 = theKML.getThePlacemarks().get(1).getItem().getPointAt(0).getLatitude();
            double kmlLon1 = theKML.getThePlacemarks().get(1).getItem().getPointAt(0).getLongitude();
            assertEquals(kmlLat1, firstPoint.getCoordinate().getLatitude(), 0.001);
            assertEquals(kmlLon1, firstPoint.getCoordinate().getLongitude(), 0.001);

            Waypoint secondPoint = midTrackSegment.getWaypoints().get(1);
            double kmlLat2 = theKML.getThePlacemarks().get(1).getItem().getPointAt(1).getLatitude();
            double kmlLon2 = theKML.getThePlacemarks().get(1).getItem().getPointAt(1).getLongitude();
            assertEquals(kmlLat2, secondPoint.getCoordinate().getLatitude(), 0.001);
            assertEquals(kmlLon2, secondPoint.getCoordinate().getLongitude(), 0.001);
            
            Track lastTrack = tracks.get(2);
            TrackSegment lastTrackSegment = lastTrack.getSegments().get(0);
            Waypoint lastTrackPoint = lastTrackSegment.getWaypoints().get(0);
            double kmlLatLast = theKML.getThePlacemarks().get(2).getItem().getPointAt(0).getLatitude();
            double kmlLonLast = theKML.getThePlacemarks().get(2).getItem().getPointAt(0).getLongitude();
            assertEquals(kmlLatLast, lastTrackPoint.getCoordinate().getLatitude(), 0.001);
            assertEquals(kmlLonLast, lastTrackPoint.getCoordinate().getLongitude(), 0.001);
        } catch (Exception e){
            assert(false);
        }
    }
}
