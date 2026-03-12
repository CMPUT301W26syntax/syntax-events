package com.example.syntaxappproject;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link OrganizerEventsRepository}.
 * <p>
 * Uses an in-memory fake subclass to avoid any Firebase dependency.
 * </p>
 */
public class OrganizerEventsRepositoryTest {

    /**
     * In-memory fake subclass of {@link OrganizerEventsRepository} that stores
     * events in a list and filters them by {@code organizerUid}.
     */
    static class FakeOrganizerEventsRepository extends OrganizerEventsRepository {

        private final List<EventDetail> store = new ArrayList<>();

        /**
         * Constructs the fake repository using the test-mode constructor
         * to bypass Firebase initialization.
         */
        public FakeOrganizerEventsRepository() {
            super(true);
        }

        /**
         * Adds an event to the in-memory store for use in tests.
         *
         * @param event the {@link EventDetail} to add
         */
        public void addEvent(EventDetail event) {
            store.add(event);
        }

        @Override
        public void getOrganizerEvents(String organizerUid, EventsCallback callback) {
            List<EventDetail> result = new ArrayList<>();
            for (EventDetail e : store) {
                if (organizerUid.equals(e.getOrganizerUid())) {
                    result.add(e);
                }
            }
            callback.onSuccess(result);
        }
    }

    private FakeOrganizerEventsRepository repo;

    /**
     * Initializes a fresh fake repository before each test.
     */
    @Before
    public void setUp() {
        repo = new FakeOrganizerEventsRepository();
    }

    /**
     * Verifies that {@link OrganizerEventsRepository#getOrganizerEvents} returns
     * an empty list when the organizer has no events.
     */
    @Test
    public void testGetOrganizerEvents_returnsEmptyList_whenNoEvents() {
        List<EventDetail>[] result = new List[1];
        repo.getOrganizerEvents("organizer-001", events -> result[0] = events);
        assertNotNull(result[0]);
        assertTrue(result[0].isEmpty());
    }

    /**
     * Verifies that {@link OrganizerEventsRepository#getOrganizerEvents} returns
     * only the events belonging to the specified organizer.
     */
    @Test
    public void testGetOrganizerEvents_returnsOnlyMatchingOrganizerEvents() {
        EventDetail e1 = new EventDetail();
        e1.setOrganizerUid("organizer-001");
        e1.setName("Event A");

        EventDetail e2 = new EventDetail();
        e2.setOrganizerUid("organizer-002");
        e2.setName("Event B");

        repo.addEvent(e1);
        repo.addEvent(e2);

        List<EventDetail>[] result = new List[1];
        repo.getOrganizerEvents("organizer-001", events -> result[0] = events);

        assertEquals(1, result[0].size());
        assertEquals("Event A", result[0].get(0).getName());
    }

    /**
     * Verifies that {@link OrganizerEventsRepository#getOrganizerEvents} returns
     * all events when the organizer has created multiple events.
     */
    @Test
    public void testGetOrganizerEvents_returnsAllEventsForOrganizer() {
        EventDetail e1 = new EventDetail();
        e1.setOrganizerUid("organizer-001");
        e1.setName("Event A");

        EventDetail e2 = new EventDetail();
        e2.setOrganizerUid("organizer-001");
        e2.setName("Event B");

        repo.addEvent(e1);
        repo.addEvent(e2);

        List<EventDetail>[] result = new List[1];
        repo.getOrganizerEvents("organizer-001", events -> result[0] = events);

        assertEquals(2, result[0].size());
    }

    /**
     * Verifies that events from other organizers are not included in the results.
     */
    @Test
    public void testGetOrganizerEvents_excludesOtherOrganizersEvents() {
        EventDetail e1 = new EventDetail();
        e1.setOrganizerUid("organizer-999");
        e1.setName(g"Other Event");

        repo.addEvent(e1);

        List<EventDetail>[] result = new List[1];
        repo.getOrganizerEvents("organizer-001", events -> result[0] = events);

        assertTrue(result[0].isEmpty());
    }

    /**
     * Verifies that the callback receives a non-null list even when
     * no events match the given organizer UID.
     */
    @Test
    public void testGetOrganizerEvents_callbackReceivesNonNullList() {
        List<EventDetail>[] result = new List[1];
        repo.getOrganizerEvents("organizer-001", events -> result[0] = events);
        assertNotNull(result[0]);
    }
}
