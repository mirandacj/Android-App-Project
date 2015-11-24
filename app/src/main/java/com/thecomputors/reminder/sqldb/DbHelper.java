package com.thecomputors.reminder.sqldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thecomputors.reminder.Util;
import com.thecomputors.reminder.Reminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {


	public static final String DB_NAME = "reminder.db";
	public static final int DB_VERSION = 1;
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat(Reminder.DEFAULT_DATE_FORMAT);

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Alert.getSql());
		db.execSQL(AlertTime.getSql());
		db.execSQL(AlertMsg.getSql());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Alert.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AlertTime.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AlertMsg.TABLE_NAME);
		
		onCreate(db);		
	}
	
	public void shred(SQLiteDatabase db) {
		db.delete(AlertMsg.TABLE_NAME, AlertMsg.COL_STATUS+" = ?", new String[]{AlertMsg.CANCELLED});
	}
	
	private final String populateSQL = Util.concat("SELECT ",
														"a."+Alert.COL_FROMDATE+", ",
														"a."+Alert.COL_TODATE+", ",
														"a."+Alert.COL_INTERVAL+", ",
														"at."+ AlertTime.COL_AT,
												" FROM "+Alert.TABLE_NAME+" AS a",
												" JOIN "+ AlertTime.TABLE_NAME+" AS at",
												" ON a."+Alert.COL_ID+" = at."+ AlertTime.COL_ALERTID,
												" WHERE a."+Alert.COL_ID+" = ?");
	
	public void populate(long alertId, SQLiteDatabase db) {

		String[] selectionArgs = {String.valueOf(alertId)};
		Cursor c = db.rawQuery(populateSQL, selectionArgs);
		
		if (c.moveToFirst()) {			
			Calendar cal = Calendar.getInstance(); 			
			AlertMsg alertMsg = new AlertMsg();
			long now = System.currentTimeMillis();
			db.beginTransaction();
	        try {
				do {
					Date fromDate = sdf.parse(c.getString(0)); //yyyy-M-d
					cal.setTime(fromDate);
					
					//at
					String[] tokens = c.getString(3).split(":"); //hh:mm
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tokens[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(tokens[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					String interval = c.getString(2); //mm hh dd MM yy
					
					if (interval == null) {//one time
						alertMsg.reset();
						alertMsg.setAlertId(alertId);
						alertMsg.setDateTime(cal.getTimeInMillis());
						if (alertMsg.getDateTime() < now-Util.MIN)
							alertMsg.setStatus(AlertMsg.EXPIRED);
						alertMsg.save(db);
						
					} else {//repeating

						interval = "0 0 1 0 0"; //date++;
							
							//Day
						if (!"0".equals(tokens[1])) {
								cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(tokens[1]));
								interval = "0 0 7 0 0"; //week++;
							}
						//Every
						if (!"0".equals(tokens[0]) && "0".equals(tokens[1])) {
							cal.set(Calendar.DATE, Integer.parseInt(tokens[0]));
							interval = "0 0 0 1 0"; //month++;
							}
							//Month
						if (!"0".equals(tokens[2])) {
							cal.set(Calendar.MONTH, Integer.parseInt(tokens[2])-1);
							interval = "0 0 0 0 1"; //year++;
						}

							while(cal.getTime().before(fromDate)) {
								nextDate(cal, interval);
							}
						
						Date toDate = sdf.parse(c.getString(1));
						toDate.setHours(0);
						toDate.setMinutes(0);
						toDate.setSeconds(0);
						toDate.setDate(toDate.getDate()+1);
						while(cal.getTime().before(toDate)) {
							alertMsg.reset();
							alertMsg.setAlertId(alertId);
							alertMsg.setDateTime(cal.getTimeInMillis());
							if (alertMsg.getDateTime() < now-Util.MIN)
								alertMsg.setStatus(AlertMsg.EXPIRED);
							alertMsg.save(db);
							nextDate(cal, interval);
						}						
					}					
					
				} while(c.moveToNext());
				
		        db.setTransactionSuccessful();
		    } catch (Exception e) {
//		    	Log.e(TAG, e.getMessage(), e);
		    } finally {
		    	db.endTransaction();
		    }
		}
		c.close();
	}
	
	private void nextDate(Calendar cal, String interval) {
		String[] tokens = interval.split(" ");
		cal.add(Calendar.MINUTE, Integer.parseInt(tokens[0]));
		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(tokens[1]));
		cal.add(Calendar.DATE, Integer.parseInt(tokens[2]));
		cal.add(Calendar.MONTH, Integer.parseInt(tokens[3]));
		cal.add(Calendar.YEAR, Integer.parseInt(tokens[4]));
	}
	
	private final String listSQL = Util.concat("SELECT ",
													"a."+Alert.COL_NAME+", ",
													"am."+ AlertMsg.COL_ID+", ",
													"am."+ AlertMsg.COL_DATETIME+", ",
													"am."+ AlertMsg.COL_STATUS,
											" FROM "+Alert.TABLE_NAME+" AS a",
											" JOIN "+ AlertMsg.TABLE_NAME+" AS am",
											" ON a."+Alert.COL_ID+" = am."+ AlertMsg.COL_ALERTID);

	/*
		params: database, start time, end time
	 */

	public Cursor listNotifications(SQLiteDatabase db, String... args) {
		String selection = "am."+ AlertMsg.COL_STATUS+" != '"+ AlertMsg.CANCELLED+"'";
		selection += (args!=null && args.length>0 && args[0]!=null) ? " AND am."+ AlertMsg.COL_DATETIME+" >= "+args[0] : "";
		selection += (args!=null && args.length>1 && args[1]!=null) ? " AND am."+ AlertMsg.COL_DATETIME+" <= "+args[1] : "";
		
		String sql = Util.concat(listSQL,
								" WHERE "+selection,
								" ORDER BY am."+ AlertMsg.COL_DATETIME+" ASC");

		return db.rawQuery(sql, null);		
	}
	
	public int cancelNotification(SQLiteDatabase db, long id, boolean isAlertId) {
		ContentValues cv = new ContentValues();
		cv.put(AlertMsg.COL_STATUS, AlertMsg.CANCELLED);
		return db.update(AlertMsg.TABLE_NAME,
							cv, 
							(isAlertId ? AlertMsg.COL_ALERTID : AlertMsg.COL_ID)+" = ?",
							new String[]{String.valueOf(id)});
	}
	
	public int cancelNotification(SQLiteDatabase db, String startTime, String endTime) {
		ContentValues cv = new ContentValues();
		cv.put(AlertMsg.COL_STATUS, AlertMsg.CANCELLED);
		return db.update(AlertMsg.TABLE_NAME,
							cv, 
							AlertMsg.COL_DATETIME+" >= ? AND "+ AlertMsg.COL_DATETIME+" <= ?",
							new String[]{startTime, endTime});
	}	
	
	 public static final String getDateStr(int year, int month, int date) {
		 return Util.concat(year, "-", month, "-", date);
	 }
	 
	 public static final String getTimeStr(int hour, int minute) {
		 return Util.concat(hour, ":", minute>9 ? "":"0", minute);
	 }	 

}
