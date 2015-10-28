package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TimePicker;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by o0None0o on 10/8/2015.
 */
public class CreateEventTest extends ActivityInstrumentationTestCase2<CreateEvent> {
    private CreateEvent createEvent;
    public CreateEventTest()
    {
        super(CreateEvent.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    public void testEspresso() {



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

    }
    public static ViewAction setTime(final int hour, final int minute) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                TimePicker tp = (TimePicker) view;
                tp.setCurrentHour(hour);
                tp.setCurrentMinute(minute);
            }
            @Override
            public String getDescription() {
                return "Set the passed time into the TimePicker";
            }



            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }
        };
    }
}
