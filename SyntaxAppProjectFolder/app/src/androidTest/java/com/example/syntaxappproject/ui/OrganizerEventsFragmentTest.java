package com.example.syntaxappproject.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.EventDetail;
import com.example.syntaxappproject.OrganizerEventsRepository;
import com.example.syntaxappproject.R;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Instrumented tests for {@link OrganizerEventsFragment} UI behavior.
 */
@RunWith(AndroidJUnit4.class)
public class OrganizerEventsFragmentTest {

    private AuthenticationService mockAuth;
    private OrganizerEventsRepository mockRepo;
    private NavController mockNavController;
    private EventDetail fakeEvent;

    /**
     * Initializes mocks and test data before each test method.
     */
    @Before
    public void setUp() {
        mockAuth = mock(AuthenticationService.class);
        mockRepo = mock(OrganizerEventsRepository.class);
        mockNavController = mock(NavController.class);

        when(mockAuth.getCurrentUserId()).thenReturn("test-uid-123");

        fakeEvent = new EventDetail(
                "testEvent123",
                "Test Event",
                "Test description",
                "Location",
                100, false,
                "2026-03-01", "2026-03-02",
                "2026-03-01", "2026-03-02",
                0, "", "");
    }

    /**
     * Launches fragment with injected mocks and attached NavController.
     */
    private FragmentScenario<OrganizerEventsFragment> launchFragment() {
        FragmentFactory factory = new FragmentFactory() {
            @Override
            public Fragment instantiate(ClassLoader classLoader, String className) {
                if (!className.equals(OrganizerEventsFragment.class.getName())) {
                    return super.instantiate(classLoader, className);
                }
                OrganizerEventsFragment fragment = new OrganizerEventsFragment();
                injectMocks(fragment);
                return fragment;
            }
        };

        FragmentScenario<OrganizerEventsFragment> scenario = FragmentScenario.launchInContainer(
                OrganizerEventsFragment.class,
                new Bundle(),
                R.style.Theme_SyntaxAppProject,
                factory
        );

        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), mockNavController);
            fragment.setEventsForTest(Collections.singletonList(fakeEvent));
        });

        return scenario;
    }

    /**
     * Injects mock dependencies into fragment via reflection.
     */
    private void injectMocks(OrganizerEventsFragment fragment) {
        try {
            Field authField = OrganizerEventsFragment.class.getDeclaredField("authService");
            authField.setAccessible(true);
            authField.set(fragment, mockAuth);

            Field repoField = OrganizerEventsFragment.class.getDeclaredField("repo");
            repoField.setAccessible(true);
            repoField.set(fragment, mockRepo);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mocks: " + e.getMessage(), e);
        }
    }

    /**
     * Verifies RecyclerView correctly displays event titles.
     */
    @Test
    public void recyclerView_displaysEventTitle() {
        launchFragment();
        onView(withText("Test Event")).check(matches(isDisplayed()));
    }

    /**
     * Verifies clicking RecyclerView item triggers navigation to event detail.
     */
    @Test
    public void clickingEvent_navigatesToEventDetail() {
        launchFragment();

        onView(withId(R.id.organizerEventList))
                .perform(actionOnItemAtPosition(0, click()));

        verify(mockNavController).navigate(
                eq(R.id.toEventDetailFragment),
                any(Bundle.class)
        );
    }
}
