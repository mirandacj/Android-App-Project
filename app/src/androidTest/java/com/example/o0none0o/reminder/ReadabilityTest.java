package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.text.format.DateUtils;
import android.widget.TimePicker;

import org.hamcrest.Matchers;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by o0None0o on 10/8/2015.
 */
public class ReadabilityTest extends ActivityInstrumentationTestCase2<PersonalizeActivity> {
    private PersonalizeActivity personalizeActivity;


    public ReadabilityTest()
    {
        super(PersonalizeActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    public void testFont() {
onView(withText("Insoma")).perform(click());

    }
}
