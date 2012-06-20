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

import java.util.Date;

import net.divbyzero.gpx.Coordinate;
import net.divbyzero.gpx.Waypoint;

import org.junit.Test;

public class WaypointTest {

	@Test
	public void testTimeIsInitiallyNull() {
		Waypoint w = new Waypoint();
		assertNull(w.getTime());
	}
	
	@Test
	public void testTimeCanBeSet() {
		Date time = new Date();

		Waypoint w = new Waypoint();
		w.setTime(time);
		
		assertEquals(time, w.getTime());
	}
	
	@Test
	public void testCoordinateIsInitiallyNull() {
		Waypoint w = new Waypoint();
		assertNull(w.getCoordinate());
	}

	@Test
	public void testCoordinateCanBeSet() {
		Waypoint w = new Waypoint();
		Coordinate c = new Coordinate();
		c.setLatitude(1.0);
		c.setLongitude(1.0);
		
		w.setCoordinate(c);
		assertEquals(c, w.getCoordinate());
	}
	
	@Test
	public void testNameIsInitiallyEmpty() {
		Waypoint w = new Waypoint();
		assertEquals("", w.getName());
	}
	
	@Test
	public void testNameCanBeSet() {
		Waypoint w = new Waypoint();
		w.setName("Test");
		assertEquals("Test", w.getName());
	}

	@Test
	public void testElevationIsInitiallyEmpty() {
		Waypoint w = new Waypoint();
		assertEquals(0.0, w.getElevation(), 0.0);
	}
	
	@Test
	public void testElevationCanBeSet() {
		Waypoint w = new Waypoint();
		w.setElevation(200);
		assertEquals(200, w.getElevation(), 0.0);
	}

	@Test
	public void testDistanceToSelfIsZero() {
		Waypoint w = new Waypoint();
		assertEquals(0, w.calculateDistanceTo(w), 0.0);
		
		Coordinate c = new Coordinate();
		c.setLatitude(1.0);
		c.setLongitude(2.0);
		w.setCoordinate(c);
		assertEquals(0, w.calculateDistanceTo(w), 0.0);
	}
	
	@Test
	public void testDistanceBetweenSydneyAndNewYork() {
		Waypoint newYorkCity = new Waypoint();
		Coordinate c1 = new Coordinate();
		c1.setLatitude(40.712778);
		c1.setLongitude(-74.005833);
		newYorkCity.setCoordinate(c1);

		Waypoint sydney = new Waypoint();
		Coordinate c2 = new Coordinate();
		c2.setLatitude(-33.85);
		c2.setLongitude(151.2);
		sydney.setCoordinate(c2);

		/* The distance between New York City and Sydney is *somewhere*
		 * around 16.000 kilometers.
		 */
		assertTrue(newYorkCity.calculateDistanceTo(sydney) > 1000 * 16000);
		assertTrue(newYorkCity.calculateDistanceTo(sydney) < 1000 * 16500);
	}
}

