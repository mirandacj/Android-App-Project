package com.thecomputors.reminder.sqldb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thecomputors.reminder.Util;

import java.util.List;

public class Alert extends AbstractModel {
	
	public static final String TABLE_NAME = "alert";
	public static final String COL_ID = AbstractModel.COL_ID;
	public static final String COL_CREATEDTIME = "created_time";
	public static final String COL_MODIFIEDTIME = "modified_time";
	public static final String COL_NAME = "name";
	public static final String COL_FROMDATE = "from_date";
	public static final String COL_TODATE = "to_date";
	public static final String COL_INTERVAL = "interval";
	public static final String COL_SOUND = "sound";
	
	static String getSql() {
		return Util.concat("CREATE TABLE ", TABLE_NAME, " (",
				AbstractModel.getSql(),
				COL_CREATEDTIME, " INTEGER, ",
				COL_MODIFIEDTIME, " INTEGER, ",
				COL_NAME, " TEXT, ",
				COL_FROMDATE, " DATE, ",
				COL_TODATE, " DATE, ",
				COL_INTERVAL, " TEXT, ",
				COL_SOUND, " INTEGER",
				");");
	}
	
	long save(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		long now = System.currentTimeMillis();
		cv.put(COL_CREATEDTIME, now);
		cv.put(COL_MODIFIEDTIME, now);
		cv.put(COL_NAME, name==null ? "" : name);
		cv.put(COL_FROMDATE, fromDate);
		cv.put(COL_TODATE, toDate);
		cv.put(COL_INTERVAL, interval);
		cv.put(COL_SOUND, sound ? 1 : 0);
		
		return db.insert(TABLE_NAME, null, cv);
	}
	
	boolean update(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		super.update(cv);
		cv.put(COL_MODIFIEDTIME, System.currentTimeMillis());
		if (name != null)
			cv.put(COL_NAME, name);
		if (fromDate != null)
			cv.put(COL_FROMDATE, fromDate);
		if (toDate != null)
			cv.put(COL_TODATE, toDate);
		if (interval != null)
			cv.put(COL_INTERVAL, interval);		
		if (sound != null)
			cv.put(COL_SOUND, sound ? 1 : 0);		
		
		return db.update(TABLE_NAME, cv, COL_ID+" = ?", new String[]{String.valueOf(id)}) 
				== 1 ? true : false;
	}
	
	public boolean load(SQLiteDatabase db) {
		Cursor cursor = db.query(TABLE_NAME, null, COL_ID+" = ?", new String[]{String.valueOf(id)}, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				reset();
				super.load(cursor);
				createdTime = cursor.getLong(cursor.getColumnIndex(COL_CREATEDTIME));
				modifiedTime = cursor.getLong(cursor.getColumnIndex(COL_MODIFIEDTIME));
				name = cursor.getString(cursor.getColumnIndex(COL_NAME));
				fromDate = cursor.getString(cursor.getColumnIndex(COL_FROMDATE));
				toDate = cursor.getString(cursor.getColumnIndex(COL_TODATE));
				interval = cursor.getString(cursor.getColumnIndex(COL_INTERVAL));
				sound = cursor.getInt(cursor.getColumnIndex(COL_SOUND)) == 1 ? true : false;
				return true;
			}
			return false;
		} finally {
		}
	}
	
	public static Cursor list(SQLiteDatabase db) {
		String[] columns = {COL_ID, COL_NAME};
		
		return db.query(TABLE_NAME, columns, null, null, null, null, COL_CREATEDTIME+" DESC");
	}
	
	public boolean delete(SQLiteDatabase db) {
		boolean status = false;
		String[] whereArgs = new String[]{String.valueOf(id)};

		db.beginTransaction();
        try {
			db.delete(AlertTime.TABLE_NAME, AlertTime.COL_ALERTID +" = ?", whereArgs);
			status = db.delete(TABLE_NAME, COL_ID+" = ?", whereArgs)
					== 1 ? true : false;
	        db.setTransactionSuccessful();
	    } catch (Exception e) {
	    } finally {
	    	db.endTransaction();
	    }
		return status;		
	}	
	
	//--------------------------------------------------------------------------

	private long createdTime;
	private long modifiedTime;
	private String name;
	private String fromDate;
	private String toDate;
	private String interval;
	private Boolean sound = Boolean.FALSE;
	private List<AlertTime> occurrences;
	
	public void reset() {
		super.reset();
		createdTime = 0;
		modifiedTime = 0;
		name = null;
		fromDate = null;
		toDate = null;
		interval = null;
		sound = Boolean.FALSE;
		occurrences = null;
	}	

	public long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}
	public long getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}	
	public Boolean getSound() {
		return sound;
	}
	public void setSound(Boolean sound) {
		this.sound = sound;
	}	
	public List<AlertTime> getOccurrences() {
		return occurrences;
	}
	public void setOccurrences(List<AlertTime> occurrences) {
		this.occurrences = occurrences;
	}

	public Alert() {}
	
	public Alert(long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		return id == ((Alert)obj).id;
	}
 
	@Override
	public int hashCode() {
		return 1;
	}	

}
