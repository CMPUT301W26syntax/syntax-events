package com.example.syntaxappproject;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        EventDetail noArgEvent = new EventDetail();
        assertNull(noArgEvent.getEventId());
        assertNull(noArgEvent.getName());
        assertNull(noArgEvent.getDescription());
        assertNull(noArgEvent.getLocation());
        assertEquals(0, noArgEvent.getCapacity());
        assertFalse(noArgEvent.isGeoReq());
        assertNull(noArgEvent.getStartingEventDate());
        assertNull(noArgEvent.getEndingEventDate());
        assertNull(noArgEvent.getStartingRegistrationPeriod());
        assertNull(noArgEvent.getEndingRegistrationPeriod());
        assertEquals(0, noArgEvent.getWaitlistCount());
        assertNull(noArgEvent.getLotteryCriteria());
        assertNull(noArgEvent.getPoster());
    }
    /**
     * Verifies that the setter and getter of event id
     */
    @Test
    public void testSetAndGetEventId(){
        event.setEventId("123");
        assertEquals("123", event.getEventId());
    }
    /**
     * Verifies that the setter and getter of event name
     */
    @Test
    public void testSetAndGetEventName(){
        event.setName("New event");
        assertEquals("New event", event.getName());
    }
    /**
     * Verifies that the setter and getter of event description
     */
    @Test
    public void testSetAndGetEventDescription(){
        event.setDescription("this is a event");
        assertEquals("this is a event", event.getDescription());
    }
    /**
     * Verifies that the setter and getter of event location
     */
    @Test
    public void testSetAndGetEventLocation(){
        event.setLocation("Edmonton");
        assertEquals("Edmonton", event.getLocation());
    }
    /**
     * Verifies that the setter and getter of event capacity
     */
    @Test
    public void testSetAndGetEventCapacity(){
        event.setCapacity(10);
        assertEquals(10, event.getCapacity());
    }
    /**
     * Verifies that the setter of event geolocation requirement and return of {@link EventDetail#isGeoReq()}
     */
    @Test
    public void testSetAndIsEventGeoReq(){
        event.setGeoReq(true);
        assertTrue(event.isGeoReq());
    }
    /**
     * Verifies that the setter and getter of event start date
     */
    @Test
    public void testSetAndGetEventStartDate(){
        event.setStartingEventDate("2026-03-13");
        assertEquals("2026-03-13", event.getStartingEventDate());
    }
    /**
     * Verifies that the setter and getter of event end date
     */
    @Test
    public void testSetAndGetEventEndDate(){
        event.setEndingEventDate("2026-03-13");
        assertEquals("2026-03-13", event.getEndingEventDate());
    }
    /**
     * Verifies that the setter and getter of registration start date
     */
    @Test
    public void testSetAndGetRegistrationStartDate(){
        event.setStartingRegistrationPeriod("2026-03-13");
        assertEquals("2026-03-13", event.getStartingRegistrationPeriod());
    }
    /**
     * Verifies that the setter and getter of registration end date
     */
    @Test
    public void testSetAndGetRegistrationEndDate(){
        event.setEndingRegistrationPeriod("2026-03-13");
        assertEquals("2026-03-13", event.getEndingRegistrationPeriod());
    }
    /**
     * Verifies that the setter and getter of wait list count
     */
    @Test
    public void testSetAndGetWaitListCount(){
        event.setWaitlistCount(10);
        assertEquals(10, event.getWaitlistCount());
    }
    /**
     * Verifies that the setter and getter of lottery criteria
     */
    @Test
    public void testSetAndGetLotteryCriteria(){
        event.setLotteryCriteria("this is a lottery");
        assertEquals("this is a lottery", event.getLotteryCriteria());
    }
    /**
     * Verifies that the setter and getter of event poster
     */
    @Test
    public void testSetAndGetPoster(){
        event.setPoster("poster.png");
        assertEquals("poster.png", event.getPoster());
    }
}
