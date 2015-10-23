package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;

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
// Check if view with the text 'Hello.' is shown


        //onView(withId(R.id.bPersonalize)).perform(click());
       // onView(withId(R.id.tvPersonalize)).check(matches(withText("Personalize")));
//        intended(toPackage("com.example.o0none0o.myapplication.NewActivity"));
        onView(withId((R.id.bAdd))).perform(click());
        onView(withId(R.id.eventName)).perform(typeText("HELLO"));
      //  onView(withId(R.id.create)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.eventName)).check(matches(withText("HELLO")));
        ViewActions.closeSoftKeyboard();
        //onView(withId(R.id.create)).perform(click());

        onView(withId(R.id.btnDone)).perform(click());
        onData(instanceOf(CustomAdapter.class)).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(withText("HELLO")));
    }
}
