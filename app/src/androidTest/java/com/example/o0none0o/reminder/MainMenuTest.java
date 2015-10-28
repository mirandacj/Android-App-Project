package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by o0None0o on 10/8/2015.
 */
public class MainMenuTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private MainMenu mainActivity;
    public MainMenuTest()
    {
        super(MainMenu.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    public void testEspresso() {
        onView(withId((R.id.bAdd))).perform(click());
        pressBack();
        onView(withId(R.id.bDisp)).perform(click());
        pressBack();
        onView(withId(R.id.bPersonalize)).perform(click());
        onView(withId(R.id.tvPersonalize)).check(matches(withText("Personalize")));
        pressBack();
    }
}
