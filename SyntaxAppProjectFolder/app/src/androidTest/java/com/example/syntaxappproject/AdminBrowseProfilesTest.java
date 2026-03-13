package com.example.syntaxappproject;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AdminBrowseProfilesTest {

    @Test
    public void testBrowseProfilesScreenDisplays() {
        FragmentScenario.launchInContainer(AdminBrowseProfiles.class);
        onView(withText("Browse Profiles")).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_profiles)).check(matches(isDisplayed()));
    }
}