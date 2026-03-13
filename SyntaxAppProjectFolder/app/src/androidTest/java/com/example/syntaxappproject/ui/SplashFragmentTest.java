package com.example.syntaxappproject.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

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
 * Instrumented tests for {@link SplashFragment} navigation routing.
 */
@RunWith(AndroidJUnit4.class)
public class SplashFragmentTest {

    private AuthenticationService mockAuth;
    private ProfileRepository mockRepo;
    private NavController mockNavController;
    private Profile fakeProfile;

    /**
     * Initializes mocks and test profile before each test.
     */
    @Before
    public void setUp() {
        mockAuth = mock(AuthenticationService.class);
        mockRepo = mock(ProfileRepository.class);
        mockNavController = mock(NavController.class);

        doAnswer(inv -> "test-uid").when(mockAuth).getCurrentUserId();

        doAnswer(inv -> {
            AuthenticationService.AuthCallback cb = inv.getArgument(0);
            cb.onComplete(true);
            return null;
        }).when(mockAuth).signInAnonymously(any());

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
    }

    /**
     * Launches fragment with injected mocks and configured profile response.
     */
    private FragmentScenario<SplashFragment> launchFragment(Profile profileToReturn) {
        FragmentScenario<SplashFragment> scenario = FragmentScenario.launchInContainer(
                SplashFragment.class,
                new Bundle(),
                R.style.Theme_SyntaxAppProject
        );

        scenario.onFragment(fragment -> {
            fragment.setAuthService(mockAuth);
            fragment.setProfileRepo(mockRepo);
            Navigation.setViewNavController(fragment.requireView(), mockNavController);

            doAnswer(inv -> {
                ProfileRepository.ProfileCallback cb = inv.getArgument(1);
                cb.onResult(profileToReturn);
                return null;
            }).when(mockRepo).getProfile(anyString(), any(ProfileRepository.ProfileCallback.class));
        });

        return scenario;
    }

    /**
     * Verifies new users (no profile) navigate to profile setup.
     */
    @Test
    public void enterButton_newUser_navigatesToProfile() {
        launchFragment(null);
        onView(withId(R.id.enterButton)).perform(click());
        verify(mockNavController).navigate(R.id.action_splash_to_profile);
    }

    /**
     * Verifies existing regular users navigate to home screen.
     */
    @Test
    public void enterButton_existingUser_navigatesToHome() {
        launchFragment(fakeProfile);
        onView(withId(R.id.enterButton)).perform(click());
        verify(mockNavController).navigate(R.id.action_splash_to_home);
    }

    /**
     * Verifies admin users navigate to admin dashboard.
     */
    @Test
    public void enterButton_adminUser_navigatesToAdmin() {
        Profile adminProfile = new Profile(
                "Admin User",
                "admin@example.com",
                null,
                "Admin",
                false,
                false,
                true,
                "admin-uid"
        );
        launchFragment(adminProfile);
        onView(withId(R.id.enterButton)).perform(click());
        verify(mockNavController).navigate(R.id.action_splash_to_admin);
    }
}
