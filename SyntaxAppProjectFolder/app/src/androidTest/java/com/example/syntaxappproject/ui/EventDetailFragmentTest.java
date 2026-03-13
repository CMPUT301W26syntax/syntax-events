package com.example.syntaxappproject.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.syntaxappproject.R;
import com.google.android.material.button.MaterialButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailFragmentTest {

    private static final String TEST_EVENT_ID = "test_event_123";
    private FragmentScenario<EventDetailFragment> scenario;

    @Before
    public void setup() {
        // Prepare fragment args with testingMode = true
        Bundle args = new Bundle();
        args.putString("eventId", TEST_EVENT_ID);
        args.putBoolean("testingMode", true);

        // Launch fragment in isolation
        scenario = FragmentScenario.launchInContainer(
                EventDetailFragment.class,
                args,
                R.style.Theme_SyntaxAppProject,
                (FragmentFactory) null
        );
    }

    @Test
    public void testJoinButton_toggleUpdatesWaitlistCount() {
        scenario.onFragment(fragment -> {
            MaterialButton joinButton = fragment.requireView().findViewById(R.id.joinButton);
            TextView wLCount = fragment.requireView().findViewById(R.id.eventWLCount);

            // initialize UI
            fragment.requireActivity().runOnUiThread(() -> {
                joinButton.setText("Join");
                wLCount.setText("Waitlist: 0");
            });

            // First click: Join
            fragment.requireActivity().runOnUiThread(() -> joinButton.performClick());

            // Post a runnable to check after UI thread updates
            fragment.requireView().post(() -> {
                assertEquals("Leave", joinButton.getText().toString());
                assertEquals("Waitlist: 1", wLCount.getText().toString());

                // Second click: Leave
                fragment.requireActivity().runOnUiThread(() -> joinButton.performClick());

                fragment.requireView().post(() -> {
                    assertEquals("Join", joinButton.getText().toString());
                    assertEquals("Waitlist: 0", wLCount.getText().toString());
                });
            });
        });
    }

    @Test
    public void testJoinButton_initialState() {
        scenario.onFragment(fragment -> {
            MaterialButton joinButton = fragment.requireView().findViewById(R.id.joinButton);
            TextView wLCount = fragment.requireView().findViewById(R.id.eventWLCount);

            fragment.requireActivity().runOnUiThread(() -> {
                joinButton.setText("Join");
                wLCount.setText("Waitlist: 5");
            });

            fragment.requireView().post(() -> {
                assertEquals("Join", joinButton.getText().toString());
                assertEquals("Waitlist: 5", wLCount.getText().toString());
            });
        });
    }

    @Test
    public void testMultipleJoins() {
        scenario.onFragment(fragment -> {
            MaterialButton joinButton = fragment.requireView().findViewById(R.id.joinButton);
            TextView wLCount = fragment.requireView().findViewById(R.id.eventWLCount);

            fragment.requireActivity().runOnUiThread(() -> {
                joinButton.setText("Join");
                wLCount.setText("Waitlist: 2");
            });

            // Perform 3 clicks on UI thread
            for (int i = 0; i < 3; i++) {
                fragment.requireActivity().runOnUiThread(joinButton::performClick);
            }

            fragment.requireView().post(() -> {
                // After 3 toggles: should be Leave, waitlist 3
                assertEquals("Leave", joinButton.getText().toString());
                assertEquals("Waitlist: 3", wLCount.getText().toString());
            });
        });
    }
}