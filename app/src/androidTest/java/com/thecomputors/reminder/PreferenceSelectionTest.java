package com.thecomputors.reminder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TimePicker;

import com.thecomputors.reminder.sqldb.DbHelper;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

/**
 * Created by o0None0o on 11/24/2015.
 */
public class PreferenceSelectionTest extends ActivityInstrumentationTestCase2<DisplayActivity> {
    private static final android.database.sqlite.SQLiteDatabase.CursorFactory MODE_PRIVATE = null;
    private static long test_time;
    private CreateReminder createReminder;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private int i, testHour1, testMinute1, testHour2, testMinute2;
    public static TimePicker tp;
    private Activity activity;

    public PreferenceSelectionTest() {
        super(DisplayActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    @Test
    public void testPrefSelection()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
        settings.edit().clear().commit();
        String fontTest = Reminder.getFontPref();
        String dateTest = Reminder.getDateFormat();
        boolean is24Hours = Reminder.is24Hours();
        boolean isVibrate = Reminder.isVibrate();
        String themeTest = Reminder.getThemePref();

        onView(withId(R.id.btnSettings)).perform(click());
assertThat(fontTest, is("Default"));
        assertThat(dateTest, is("yyyy-M-d"));
        assertThat(is24Hours, is(true));
        assertThat(isVibrate, is(true));
        assertThat(themeTest, is("cheerful"));
        pressBack();

        onView(withId(R.id.btnSettings)).perform(click());
        onView(withId(R.id.layout)).perform(swipeUp());
        onView(withText("what")).perform(scrollTo(),click());


    }




}


