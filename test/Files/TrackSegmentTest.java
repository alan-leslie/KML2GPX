/*
 * Copyright (c) 2009 Martin Jansen
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import net.divbyzero.gpx.Coordinate;
import net.divbyzero.gpx.TrackSegment;
import net.divbyzero.gpx.Waypoint;

import org.junit.Test;

public class TrackSegmentTest {

	@Test
	public void testWaypointListIsInitiallyEmpty() {
		TrackSegment segment = new TrackSegment();
		
		assertEquals(0, segment.getWaypoints().size());		
	}

	@Test
	public void testWaypointsCanBeAdded() {
		TrackSegment segment = new TrackSegment();
		
		Waypoint w1 = new Waypoint();
		Waypoint w2 = new Waypoint();
		Waypoint w3 = w2;
		
		segment.addWaypoint(w1);
		segment.addWaypoint(w2);
		segment.addWaypoint(w3);

		assertEquals(3, segment.getWaypoints().size());

		segment.addWaypoint(new Waypoint());
		assertEquals(4, segment.getWaypoints().size());
	}
	
	@Test
	public void testCumulativeAscentIsInitiallyZero()
	{
		TrackSegment segment = new TrackSegment();
		assertEquals(0, segment.cumulativeAscent(), 0.0);
	}

	@Test
	public void testCumulativeDescentIsInitiallyZero()
	{
		TrackSegment segment = new TrackSegment();
		assertEquals(0, segment.cumulativeDescent(), 0.0);
	}
	
	@Test
	public void testCumulativeAscentForOneWaypointIsZero()
	{		
		TrackSegment segment = new TrackSegment();
		segment.addWaypoint(new Waypoint());
		assertEquals(0, segment.cumulativeAscent(), 0.0);
	}

	@Test
	public void testCumulativeDescentForOneWaypointIsZero()
	{		
		TrackSegment segment = new TrackSegment();
		segment.addWaypoint(new Waypoint());
		assertEquals(0, segment.cumulativeDescent(), 0.0);
	}
	
	@Test
	public void testCumulativeAscentAndDescentAreEqual()
	{
		TrackSegment segment = new TrackSegment();
		
		Waypoint w1 = new Waypoint();
		w1.setElevation(100);
		
		Waypoint w2 = new Waypoint();
		w2.setElevation(150);

		segment.addWaypoint(w1);
		segment.addWaypoint(w2);
		segment.addWaypoint(w1);
		
		assertEquals(50, segment.cumulativeAscent(), 0.0);
		assertEquals(50, segment.cumulativeDescent(), 0.0);
		assertEquals(segment.cumulativeAscent(), segment.cumulativeDescent(), 0.0);
	}
	
	@Test
	public void testCumulativeAscentAndDescentForDistinctWaypoints()
	{
		TrackSegment segment = new TrackSegment();
		
		Waypoint w1 = new Waypoint();
		w1.setElevation(100);
		
		Waypoint w2 = new Waypoint();
		w2.setElevation(100);
		
		Waypoint w3 = new Waypoint();
		w3.setElevation(150);
		
		Waypoint w4 = new Waypoint();
		w4.setElevation(80);
		
		segment.addWaypoint(w1);
		segment.addWaypoint(w2);
		segment.addWaypoint(w3);
		segment.addWaypoint(w4);
		
		assertEquals(50, segment.cumulativeAscent(), 0.0);
		assertEquals(70, segment.cumulativeDescent(), 0.0);
	}

	@Test
	public void testCumulativeAscentAndDescentForWaypointsOnParIsZero()
	{
		TrackSegment segment = new TrackSegment();
		
		Waypoint w1 = new Waypoint();
		w1.setElevation(100);
		
		Waypoint w2 = new Waypoint();
		w2.setElevation(100);
		
		Waypoint w3 = new Waypoint();
		w3.setElevation(100);
		
		Waypoint w4 = new Waypoint();
		w4.setElevation(100);
		
		segment.addWaypoint(w1);
		segment.addWaypoint(w2);
		segment.addWaypoint(w3);
		segment.addWaypoint(w4);
		
		assertEquals(0, segment.cumulativeAscent(), 0.0);
		assertEquals(0, segment.cumulativeDescent(), 0.0);
	}
	
	@Test
	public void testLengthIsInitiallyZero() {
		TrackSegment segment = new TrackSegment();
		assertEquals(0, segment.length(), 0.0);
		
		segment.addWaypoint(new Waypoint());
		assertEquals(0, segment.length(), 0.0);
	}
	
	@Test
	public void testLengthInSegmentIsGreaterThanZero() {
		class MockCoordinate extends Coordinate {
			private double lat;
			private double lon;
			
			public MockCoordinate(double lat, double lon) {
				super();
				this.lat = lat;
				this.lon = lon;
			}
			
			public double getLatitude() {
				return lat;
			}
			
			public double getLongitude() {
				return lon;
			}
		}
		
		TrackSegment segment = new TrackSegment();
				
		Waypoint w1 = new Waypoint();
		w1.setCoordinate(new MockCoordinate(42, 50));

		Waypoint w2 = new Waypoint();
		w2.setCoordinate(new MockCoordinate(43, 44));
		
		segment.addWaypoint(w1);
		segment.addWaypoint(w2);
		
		assertTrue(segment.length() > 0);
	}
	
	@Test
	public void testStartTimeIsInitiallyNull() {
		TrackSegment segment = new TrackSegment();
		assertNull(segment.startingTime());
		
		segment.addWaypoint(new Waypoint());
		assertNull(segment.startingTime());
		segment.addWaypoint(new Waypoint());
		assertNull(segment.startingTime());
	}
	
	@Test
	public void testEndTimeIsInitiallyNull() {
		TrackSegment segment = new TrackSegment();
		assertNull(segment.endTime());
		
		segment.addWaypoint(new Waypoint());
		assertNull(segment.endTime());
		segment.addWaypoint(new Waypoint());
		assertNull(segment.endTime());		
	}
	
	@Test
	public void testEndAndStartingTime() {
		TrackSegment segment = new TrackSegment();
		
		Calendar cal = Calendar.getInstance();
		
		Waypoint w1 = new Waypoint();
		cal.set(2009, 9, 27, 12, 20);
		w1.setTime(cal.getTime());
		segment.addWaypoint(w1);

		Waypoint w2 = new Waypoint();
		cal.set(2009, 9, 27, 12, 26);
		w2.setTime(cal.getTime());
		segment.addWaypoint(w2);

		Waypoint w3 = new Waypoint();
		cal.set(2009, 9, 27, 12, 22);
		w3.setTime(cal.getTime());
		segment.addWaypoint(w3);
		
		assertEquals(w1.getTime(), segment.startingTime());
		assertEquals(w2.getTime(), segment.endTime());
	}
}
