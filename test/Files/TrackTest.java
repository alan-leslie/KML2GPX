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
import net.divbyzero.gpx.Track;
import net.divbyzero.gpx.TrackSegment;

import org.junit.Test;

public class TrackTest {

	@Test
	public void testSegmentListIsInitiallyEmpty() {
		Track track = new Track();
		
		assertEquals(0, track.getSegments().size());
	}

	@Test
	public void testSegmentsCanBeAdded() {
		Track track = new Track();
			
		TrackSegment t1 = new TrackSegment();
		TrackSegment t2 = new TrackSegment();
		TrackSegment t3 = t2;

		track.addSegment(t1);
		track.addSegment(t2);
		track.addSegment(t3);

		assertEquals(3, track.getSegments().size());

		track.addSegment(new TrackSegment());
		assertEquals(4, track.getSegments().size());
	}
	
	@Test
	public void testInitialLengthIsZero() {
		Track track = new Track();		
		assertEquals(0, track.length(), 0.0);
	}
	
	@Test
	public void testTrackLengthIsSumOfSegmentLengths() {
		class MockSegment extends TrackSegment {
			public double length() {
				return 100;
			}
		}
		
		TrackSegment s1 = new MockSegment();
		TrackSegment s2 = new MockSegment();
		
		Track track = new Track();
		track.addSegment(s1);
		
		assertEquals(s1.length(), track.length(), 0.0);
		assertEquals(100, track.length(), 0.0);
		
		track.addSegment(s2);
		assertEquals(s1.length() + s2.length(), track.length(), 0.0);
	}
	
	@Test
	public void testInitialCumulativeAscentIsZero() {
		Track track = new Track();
		assertEquals(0, track.cumulativeAscent(), 0.0);
	}

	@Test
	public void testInitialCumulativeDescentIsZero() {
		Track track = new Track();
		assertEquals(0, track.cumulativeDescent(), 0.0);
	}

	@Test
	public void testCumulativeAscentIsSumOfSegmetAscents() {
		class MockSegment extends TrackSegment {
			public double cumulativeAscent() {
				return 200;
			}
		}
		
		TrackSegment s1 = new MockSegment();
		TrackSegment s2 = new MockSegment();
		
		Track track = new Track();
		track.addSegment(s1);
		
		assertEquals(s1.cumulativeAscent(), track.cumulativeAscent(), 0.0);
		assertEquals(200, track.cumulativeAscent(), 0.0);
		
		track.addSegment(s2);
		assertEquals(s1.cumulativeAscent() + s2.cumulativeAscent(), track.cumulativeAscent(), 0.0);
		
	}

	@Test
	public void testCumulativeDescentIsSumOfSegmetAscents() {
		class MockSegment extends TrackSegment {
			public double cumulativeDescent() {
				return 300;
			}
		}
		
		TrackSegment s1 = new MockSegment();
		TrackSegment s2 = new MockSegment();
		
		Track track = new Track();
		track.addSegment(s1);
		
		assertEquals(s1.cumulativeDescent(), track.cumulativeDescent(), 0.0);
		assertEquals(300, track.cumulativeDescent(), 0.0);
		
		track.addSegment(s2);
		assertEquals(s1.cumulativeDescent() + s2.cumulativeDescent(), track.cumulativeDescent(), 0.0);
		
	}

}
