package com.example.o0none0o.reminder;

/**
 * Created by mitchem on 10/1/2015.
 */
/**
 * Help to pass object between activities
 */
    public class ActivityBridge {

    private static Object object;

    /**
     * set object to static variable and retrieve it from another activity
     *
     * @param obj
     */
    public static void setObject(Object obj) {
        object = obj;
    }

    /**
     * get object passed from previous activity
     *
     * @return
     */
    public static Object getObject() {
        Object obj = object;
// can get only once
        object = null;
        return obj;
    }
}
