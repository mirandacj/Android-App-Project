package com.example.o0none0o.reminder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mitchem on 9/30/2015.
 */
public class Event implements Comparable {
    private String name;
    private Calendar end;
    private String description;

    public Event(){
        this.name = "Example";
        this.end = null;
        this.description = "Sample";

    }

    public Event(String name, String end, String description) {
        setName(name);
        setEnd(end);
        setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.trim().length() > 0){ this.name = name; }
        else { this.name = "Event Name"; }
    }

    public Calendar getEnd() {
        return end;
    }


    public void setEnd(String end) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Calendar date = Calendar.getInstance();
        try{
            date.setTime(format.parse(end));
        } catch (ParseException e){
            e.printStackTrace();
        }
        this.end = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int compareTo(Object otherDate) throws IllegalArgumentException{
        //Assuming equal
        int compare = 0;
        //Tests to make sure the other object is a Event object
        if((otherDate==null)||(!(otherDate instanceof Event))){
            throw new IllegalArgumentException();
        }
        //Tests to see if it is the same object, if it is: DONE
        else if(otherDate==this){
            return compare;
        }
        Event testDate = (Event)otherDate;
        return this.getEnd().compareTo(testDate.getEnd());
    }

}
