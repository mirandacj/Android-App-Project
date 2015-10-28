package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Gravity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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



   //     onView(withId(R.id.bPersonalize)).perform(click());
  //     onView(withId(R.id.tvPersonalize)).check(matches(withText("Personalize")));
//pressBack();


        onView(withId((R.id.bAdd))).perform(click());
        onView(withId(R.id.eventName)).perform(typeText("Name 1"));
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.eventDesc)).perform(typeText("Desc 1"));
        closeSoftKeyboard();
        onView(withId(R.id.create)).perform(click());
        onView(withId(R.id.eventName)).perform(clearText());
        onView(withId(R.id.eventName)).perform(typeText("Name 2"));

        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.eventDesc)).perform(clearText());
        onView(withId(R.id.eventDesc)).perform(typeText("Desc 2"));
        closeSoftKeyboard();


        onView(withId(R.id.create)).perform(click());
        onView(withId(R.id.btnDone)).perform(click());
        DrawerLayout drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
    //    onView(withId(R.id.btnDone)).perform(click());
    //    onData(instanceOf(CustomAdapter.class)).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(withText("HELLO")));
    }
}
