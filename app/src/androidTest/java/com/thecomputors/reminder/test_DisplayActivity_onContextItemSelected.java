package com.thecomputors.reminder;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by mitchem on 11/23/2015.
 */
public class test_DisplayActivity_onContextItemSelected extends ActivityInstrumentationTestCase2 {

    public test_DisplayActivity_onContextItemSelected() {
        super(DisplayActivity.class);
    }

    public void setUp() throws Exception{
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        super.setUp();
        getActivity();

    }

    public void testContextItemSelected() throws InterruptedException {

        onView(withId(R.id.btnAdd)).perform(click());
        onView(withId(R.id.msg_et)).perform(click());
        onView(withId(R.id.msg_et)).perform(typeText("test"));
        onView(withId(R.id.btnSubmit)).perform(scrollTo(), click());

        onView(withId(R.id.btnLeft)).perform(click());
        onView(withId(R.id.btnRight)).perform(click());

        onData(is(instanceOf(android.database.sqlite.SQLiteCursor.class))).atPosition(0).perform(click());

    }
}
