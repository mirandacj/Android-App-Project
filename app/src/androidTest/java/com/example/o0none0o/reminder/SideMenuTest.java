package com.example.o0none0o.reminder;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by o0None0o on 10/8/2015.
 */
public class SideMenuTest extends ActivityInstrumentationTestCase2<DisplayActivity> {
    private DisplayActivity displayActivity;
    public SideMenuTest()
    {
        super(DisplayActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }
    private static ViewAction actionOpenDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.START);
            }
        };
    }
    private static ViewAction actionCloseDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "close drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(GravityCompat.START);
            }
        };
    }
    public void testSideMenu() {


        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        onData(anything()).inAdapterView(withId(R.id.left_drawer)).atPosition(1).perform(click());
        onView(withId(R.id.textView))
                .check(matches(withText(containsString("Monthly"))));
        onView(withId(R.id.drawer_layout)).perform(actionCloseDrawer());
        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        onData(anything()).inAdapterView(withId(R.id.left_drawer)).atPosition(0).perform(click());
        onView(withId(R.id.tvYearly))
                .check(matches(withText(containsString("Yearly"))));
        onView(withId(R.id.drawer_layout)).perform(actionCloseDrawer());
        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        // onView(withId(R.id.drawer_layout)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.left_drawer)).atPosition(2).perform(click());
        onView(withId(R.id.tvDaily))
                .check(matches(withText(containsString("Daily"))));
        onView(withId(R.id.drawer_layout)).perform(actionCloseDrawer());
    }
}
