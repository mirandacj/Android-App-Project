package com.thecomputors.reminder;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;

import java.util.Collection;

import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by mitchem on 11/23/2015.
 */
public class test_DisplayActivity_onClick extends ActivityInstrumentationTestCase2{

    Activity currentActivity;


    public test_DisplayActivity_onClick() {
        super(DisplayActivity.class);
    }

    public void setUp() throws Exception{
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        super.setUp();
        getActivity();

    }

    public void testOnClick(){
        onView(withId(R.id.btnAdd)).perform(click());
        getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(CreateReminder.class));
        pressBack();

        onView(withId(R.id.btnSettings)).perform(click());
        getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(DisplayPref.class));
        pressBack();

        onView(withId(R.id.btnLeft)).perform(click());
        onView(withId(R.id.btnRight)).perform(click());
        onView(withId(R.id.tvDateRange)).check(matches(withText("Today")));
    }

    public Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = (Activity)resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity;

    }
}
