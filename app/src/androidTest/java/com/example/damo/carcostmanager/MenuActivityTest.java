package com.example.damo.carcostmanager;

import android.app.Activity;
import android.app.Instrumentation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MenuActivityTest {


    @Rule
    public ActivityTestRule<MenuActivity> menuActivityActivityTestRule = new ActivityTestRule<MenuActivity>(MenuActivity.class);

    private MenuActivity menuActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(StatsActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        menuActivity = menuActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchOfStatsActivityOnButtonClick(){
        assertNotNull(menuActivity.findViewById(R.id.statsCarBtn));

        onView(withId(R.id.statsCarBtn)).perform(click());

        Activity statsActivity =  getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(statsActivity);

        statsActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
    }
}