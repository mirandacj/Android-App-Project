package com.thecomputors.reminder;

/**
 * Created by mitchem on 11/24/2015.
 */

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;

import java.util.Collection;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class test_HomeActivity_signup extends ActivityInstrumentationTestCase2<HomeActivity>{

    Activity currentActivity;


    public test_HomeActivity_signup() {
        super(HomeActivity.class);
    }

    public void setUp() throws Exception{
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        super.setUp();
        getActivity();

    }

    public void testLogin(){
        onView(withId(R.id.buttonSignUP)).perform(click());
        onView(withId(R.id.editTextUserName)).perform(click(), typeText("testcase"));
        onView(withId(R.id.editTextPassword)).perform(click(), typeText("testing"));
        onView(withId(R.id.editTextConfirmPassword)).perform(click(), typeText("testing"));
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(DisplayActivity.class));

    }

    public void getActivityInstance(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });

    }

}