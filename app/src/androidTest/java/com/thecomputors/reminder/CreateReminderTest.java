package com.thecomputors.reminder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TimePicker;

import com.thecomputors.reminder.sqldb.AlertTime;
import com.thecomputors.reminder.sqldb.DbHelper;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.thecomputors.reminder.sqldb.Alert.COL_NAME;
import static org.hamcrest.Matchers.is;

/**
 * Created by o0None0o on 11/24/2015.
 */
public class CreateReminderTest extends ActivityInstrumentationTestCase2<CreateReminder> {
    private static final android.database.sqlite.SQLiteDatabase.CursorFactory MODE_PRIVATE = null;
    private static long test_time;
    private CreateReminder createReminder;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private int i, testHour1, testMinute1, testHour2,testMinute2;
public static TimePicker tp;
    public CreateReminderTest() {
        super(CreateReminder.class);
    }
    public void setUp() throws Exception{
                super.setUp();
                injectInstrumentation(InstrumentationRegistry.getInstrumentation());
                getActivity();
       // getTargetContext().deleteDatabase(dbHelper.DB_NAME);
     //   dbHelper = new DbHelper(getTargetContext());

            }

    @Test
    public void testAddReminder() {
        db=Reminder.db;


        
        Calendar c = Calendar.getInstance();
        testHour1= c.get(Calendar.HOUR) + 2;
        testMinute1= c.get(Calendar.MINUTE) + 10;
        testHour2=c.get(Calendar.HOUR)+3;
        testMinute2=c.get(Calendar.MINUTE)+20;
        onView(withId(R.id.msg_et)).perform(typeText("Reminder 1"));
        ViewActions.closeSoftKeyboard();
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(setTime(testHour1, testMinute1));

        onView(withId(R.id.btnSubmit)).perform(scrollTo(), click());
        onView(withId(R.id.btnAdd)).perform(click());
        onView(withId(R.id.msg_et)).perform(typeText("Reminder 2"));
        ViewActions.closeSoftKeyboard();
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(setTime(testHour2, testMinute2));
        onView(withId(R.id.btnSubmit)).perform(scrollTo(), click());
AlertTime al = new AlertTime();
       // db=dbHelper.getReadableDatabase();

        Cursor cs = db.rawQuery("Select * from Alert",null);

        cs.moveToFirst();
       assertThat(cs.getString(cs.getColumnIndex(COL_NAME)), is("Reminder 1"));

        cs.moveToNext();
        assertThat(cs.getString(cs.getColumnIndex(COL_NAME)), is("Reminder 2"));

                  }


 @Test
 public void shouldReturnReminderName() throws Exception {

    }

    public static ViewAction setTime(final int hour, final int minute) {
               return new ViewAction() {
                       @Override
                       public void perform(UiController uiController, View view) {
                               tp = (TimePicker) view;
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
