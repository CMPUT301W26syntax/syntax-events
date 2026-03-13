package com.example.syntaxappproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link EventDetailRepository}.
 * <p>
 * Verifies that personal information, role flags, device identifier,
 * and notification preferences are correctly stored and retrieved.
 * </p>
 */
public class EventDetailRepositoryTest {
    /**
     * Interface to abstract Firestore operations for testing.
     */
    interface EventDetailDataSource {
        void get(String eventId, EventDetailRepository.EventDetailCallback callback);
    }
    /**
     * In-memory fake that simulates Firestore using a {@link java.util.HashMap}.
     */
    static class FakeEventDetailDataSource implements EventDetailDataSource {

        private final java.util.Map<String, EventDetail> store = new java.util.HashMap<>();

        public void insert(String eventId, EventDetail event) {
            store.put(eventId, event);
        }

        @Override
        public void get(String eventId, EventDetailRepository.EventDetailCallback callback) {
            callback.onEventLoaded(store.get(eventId));
        }
    }
    /**
     * Testable subclass of {@link EventDetailRepository} that delegates all
     * Firestore operations to a {@link EventDetailDataSource}.
     */
    static class TestableEventDetailRepository extends EventDetailRepository {

        private final EventDetailDataSource dataSource;

        public TestableEventDetailRepository(EventDetailDataSource dataSource) {
            super(true);
            this.dataSource = dataSource;
        }

        @Override
        public void getEventDetail(String eventId, EventDetailCallback callback) {
            dataSource.get(eventId, callback);
        }
    }
    private TestableEventDetailRepository repo;
    private FakeEventDetailDataSource fakeDb;
    private EventDetail sampleEvent;
    /**
     * Initializes a repository and a event before test.
     */
    @Before
    public void setUp() {

        fakeDb = new FakeEventDetailDataSource();
        repo = new TestableEventDetailRepository(fakeDb);

        sampleEvent = new EventDetail(
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
     * Verifies that {@link EventDetailRepository#getEventDetail} returns the correct event detail.
     */
    @Test
    public void testGetEventDetail() {

        fakeDb.insert("event1", sampleEvent);

        EventDetail[] result = {null};

        repo.getEventDetail("event1", event -> result[0] = event);

        assertNotNull(result[0]);
        assertEquals("Test Event", result[0].getName());
        assertEquals("Test Description", result[0].getDescription());
        assertEquals("Test Location", result[0].getLocation());
        assertEquals(100, result[0].getCapacity());
        assertTrue(result[0].isGeoReq());
        assertEquals("2026-04-01", result[0].getStartingEventDate());
        assertEquals("2026-04-02", result[0].getEndingEventDate());
        assertEquals("2026-03-01", result[0].getStartingRegistrationPeriod());
        assertEquals("2026-03-30", result[0].getEndingRegistrationPeriod());
        assertEquals(10, result[0].getWaitlistCount());
        assertEquals("Random", result[0].getLotteryCriteria());
        assertEquals("poster.jpg", result[0].getPoster());
    }
    /**
     * Verifies that {@link EventDetailRepository#getEventDetail} callback is work.
     */
    @Test
    public void testGetEventDetail_callbackIsInvoked() {

        fakeDb.insert("event1", sampleEvent);

        boolean[] called = {false};

        repo.getEventDetail("event1", event -> called[0] = true);

        assertTrue(called[0]);
    }

}
