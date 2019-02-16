package com.example.damo.carcostmanager;

import org.junit.After;
import org.junit.Before;
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


import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchOfLoginActivityOnButtonClick(){
        assertNotNull(mainActivity.findViewById(R.id.button));

        onView(withId(R.id.button)).perform(click());

        Activity loginActivity =  getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(loginActivity);

        loginActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
    }
}