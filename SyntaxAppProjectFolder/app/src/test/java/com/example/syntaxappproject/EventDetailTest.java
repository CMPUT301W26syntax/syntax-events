package com.example.syntaxappproject;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
/**
 * Unit tests for the {@link EventDetail} model class.
 * <p>
 * Verifies that personal information, role flags, device identifier,
 * and notification preferences are correctly stored and retrieved.
 * </p>
 */
public class EventDetailTest {
    private EventDetail event;

    /**
     * Initializes a event before test:
     */
    @Before
    public void setUp() {
        event = new EventDetail(
                "event1",
                "Test Event",
                "Test Description",
                "Test Location",
                100,
                true,
                "2026-04-01",
                "2026-04-02",
                "2026-03-01",
                "2026-03-30",
                10,
                "Random",
                "poster.jpg"
        );
    }
    /**
     * Verifies that the constructor initializes event detail correctly.
     */
    @Test
    public void testConstructor() {
        assertEquals("event1", event.getEventId());
        assertEquals("Test Event", event.getName());
        assertEquals("Test Description", event.getDescription());
        assertEquals("Test Location", event.getLocation());
        assertEquals(100, event.getCapacity());
        assertTrue(event.isGeoReq());
        assertEquals("2026-04-01", event.getStartingEventDate());
        assertEquals("2026-04-02", event.getEndingEventDate());
        assertEquals("2026-03-01", event.getStartingRegistrationPeriod());
        assertEquals("2026-03-30", event.getEndingRegistrationPeriod());
        assertEquals(10, event.getWaitlistCount());
        assertEquals("Random", event.getLotteryCriteria());
        assertEquals("poster.jpg", event.getPoster());
    }
    /**
     * Verifies that the no-argument constructor initializes event detail correctly.
     */
    @Test
    public void testNoArgConstructor() {

    }
}
