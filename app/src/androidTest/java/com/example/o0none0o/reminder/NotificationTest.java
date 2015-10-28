package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TimePicker;

import org.hamcrest.Matchers;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.o0none0o.reminder.CreateEventTest.setTime;

/**
 * Created by o0None0o on 10/8/2015.
 */
public class NotificationTest extends ActivityInstrumentationTestCase2<CreateEvent> {
    private CreateEvent createEvent;
    private long startTime;
    private long waitingTime;
    private IdlingResource.ResourceCallback resourceCallback;

    public NotificationTest()
    {
        super(CreateEvent.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    public void testNoti() {

        Calendar c = Calendar.getInstance();


        onView(withId(R.id.eventName)).perform(typeText("Name 1"));
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.eventDesc)).perform(typeText("Desc 1"));
        closeSoftKeyboard();
     /*   onView(withId(R.id.create)).perform(click());
        onView(withId(R.id.eventName)).perform(clearText());
        onView(withId(R.id.eventName)).perform(typeText("Name 2"));

        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.eventDesc)).perform(clearText());
        onView(withId(R.id.eventDesc)).perform(typeText("Desc 2"));
        closeSoftKeyboard();*/

        onView(withId(R.id.btnChangeTime)).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(setTime(c.get(Calendar.HOUR), c.get(Calendar.MINUTE) + 1));
        onView(withText("OK")).perform(click());


        onView(withId(R.id.create)).perform(click());
        onView(withId(R.id.btnDone)).perform(click());
      //  IdlingPolicies.setIdlingResourceTimeout(2, TimeUnit.MINUTES);
      // onView(withText("Snooze")).perform(click());
        //IdlingPolicies.setIdlingResourceTimeout(2, TimeUnit.MINUTES);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        onView(withText("Snooze")).perform(click());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        onView(withText("Dismiss")).perform(click());


    }



}
