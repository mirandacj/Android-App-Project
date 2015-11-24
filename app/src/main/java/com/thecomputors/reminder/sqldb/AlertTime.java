package com.thecomputors.reminder.sqldb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thecomputors.reminder.Util;

public class AlertTime extends AbstractModel {
	
	public static final String TABLE_NAME = "alerttime";
	public static final String COL_ID = AbstractModel.COL_ID;
	public static final String COL_ALERTID = "alert_id";
	public static final String COL_AT = "at";
	
	static String getSql() {
		return Util.concat("CREATE TABLE ", TABLE_NAME, " (",
				AbstractModel.getSql(),
				COL_ALERTID, " INTEGER, ",
				COL_AT, " INTEGER",
				");");
	}
	
	long save(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ALERTID, alertId);
		cv.put(COL_AT, at);
		
		return db.insert(TABLE_NAME, null, cv);
	}
	
	boolean update(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		super.update(cv);
		if (alertId > 0)
			cv.put(COL_ALERTID, alertId);
		if (at != null)
			cv.put(COL_AT, at);		
		
		return db.update(TABLE_NAME, cv, COL_ID+" = ?", new String[]{String.valueOf(id)}) 
				== 1 ? true : false;
	}
	
	public boolean load(SQLiteDatabase db) {
		Cursor cursor = db.query(TABLE_NAME, null, COL_ID+" = ?", new String[]{String.valueOf(id)}, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				reset();
				super.load(cursor);
				alertId = cursor.getLong(cursor.getColumnIndex(COL_ALERTID));
				at = cursor.getString(cursor.getColumnIndex(COL_AT));
				return true;
			}
			return false;
		} finally {
		}
	}
	
	/*
		params: database, alertId, orderBy
	 */
	public static Cursor list(SQLiteDatabase db, String... args) {
		String[] columns = {COL_ID, COL_AT};
		String selection = "1 = 1";
		selection += (args!=null && args.length>0 && args[0]!=null) ? " AND "+ COL_ALERTID +" = "+args[0] : "";
		String orderBy = (args!=null && args.length>1) ? args[1] : COL_AT+" DESC";
		
		return db.query(TABLE_NAME, columns, selection, null, null, null, orderBy);
	}
	
	public boolean delete(SQLiteDatabase db) {
		return db.delete(TABLE_NAME, COL_ID+" = ?", new String[]{String.valueOf(id)})
				== 1 ? true : false;
	}	
	
	//--------------------------------------------------------------------------

	private long alertId;
	private String at;
	
	public void reset() {
		super.reset();
		alertId = 0;
		at = null;
	}

	public long getAlertId() {
		return alertId;
	}
	public void setAlertId(long alertId) {
		this.alertId = alertId;
	}
	public String getAt() {
		return at;
	}
	public void setAt(String at) {
		this.at = at;
	}
	
	public AlertTime() {}
	
	public AlertTime(long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		return id == ((AlertTime)obj).id;
	}
 
	@Override
	public int hashCode() {
		return 1;
	}	

}
