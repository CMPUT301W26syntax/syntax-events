package com.example.syntaxappproject;

import android.os.Bundle;
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
public class AdminEventDetailsTest {
    @Test
    public void testEventDetailsScreenDisplays() {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", "testevent2");
        bundle.putString("name", "Swimming Lessons");
        bundle.putString("description", "Beginner swimming lessons");
        bundle.putString("location", "Edmonton");
        bundle.putString("organizerUid", "organizer123");
        bundle.putLong("capacity", 20);
        bundle.putBoolean("geoReq", false);
        bundle.putString("startingEventDate", "2026/03/20");
        bundle.putString("endingEventDate", "2026/03/30");
        bundle.putString("startingRegistrationPeriod", "2026/03/01");
        bundle.putString("endingRegistrationPeriod", "2026/03/10");
        bundle.putLong("waitlistCount", 15);
        bundle.putString("lotteryCriteria", "Random");
        bundle.putString("poster", "https://example.com/poster.jpg");
        FragmentScenario.launchInContainer(AdminEventDetails.class, bundle);
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withId(R.id.btn_remove_event)).check(matches(isDisplayed()));
    }
}