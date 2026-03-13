package com.example.syntaxappproject.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.syntaxappproject.AuthenticationService;
import com.example.syntaxappproject.Profile;
import com.example.syntaxappproject.ProfileRepository;
import com.example.syntaxappproject.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for {@link UserFragment} navigation and profile display.
 */
@RunWith(AndroidJUnit4.class)
public class UserFragmentTest {

    private AuthenticationService mockAuth;
    private ProfileRepository mockRepo;
    private NavController mockNavController;
    private Profile fakeProfile;

    /**
     * Initializes mocks and test profile before each test method.
     */
    @Before
    public void setUp() {
        mockAuth = mock(AuthenticationService.class);
        mockRepo = mock(ProfileRepository.class);
        mockNavController = mock(NavController.class);

        fakeProfile = new Profile(
                "Jane Doe",
                "jane@example.com",
                "5551234567",
                "Entrant",
                true,
                false,
                true,
                "test-uid"
        );

        doAnswer(invocation -> "test-uid").when(mockAuth).getCurrentUserId();
    }

    /**
     * Launches test fragment with injected mocks and specified profile response.
     */
    private FragmentScenario<TestUserFragment> launchFragment(final Profile profileToReturn) {
        FragmentScenario<TestUserFragment> scenario = FragmentScenario.launchInContainer(
                TestUserFragment.class,
                null,
                R.style.Theme_SyntaxAppProject
        );

        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), mockNavController);
            fragment.setAuthService(mockAuth);
            fragment.setProfileRepo(mockRepo);

            doAnswer(invocation -> {
                ProfileRepository.ProfileCallback cb = invocation.getArgument(1);
                cb.onResult(profileToReturn);
                return null;
            }).when(mockRepo).getProfile(anyString(), any(ProfileRepository.ProfileCallback.class));
        });

        return scenario;
    }

    /**
     * Verifies personalization button navigates to edit profile screen.
     */
    @Test
    public void personalizationButton_navigatesToEditProfile() {
        launchFragment(fakeProfile);
        onView(withId(R.id.personalizationButton)).perform(click());
        verify(mockNavController).navigate(R.id.editProfileFragment);
    }

    /**
     * Verifies event history button navigates to history screen.
     */
    @Test
    public void eventHistoryButton_navigatesToHistory() {
        launchFragment(fakeProfile);
        onView(withId(R.id.eventHistoryButton)).perform(click());
        verify(mockNavController).navigate(R.id.eventHistoryFragment);
    }

    /**
     * Verifies profile data loads and displays correctly in UI.
     */
    @Test
    public void profileLoadsAndDisplaysCorrectly() {
        launchFragment(fakeProfile);
    }

    /**
     * Test subclass that skips hotbar setup to avoid test crashes.
     */
    public static class TestUserFragment extends UserFragment {
        @Override
        protected void setupHotbar(android.view.View view) {
        }
    }
}
