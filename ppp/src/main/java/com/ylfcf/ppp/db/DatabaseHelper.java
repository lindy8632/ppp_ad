package com.ylfcf.ppp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库操作类
 * @author Mr.liu
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper{
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static int DB_VERSION = 8;
	public static final String DATABASE = "YLFCF_Database";

	public DatabaseHelper(Context context) {
		super(context, DATABASE, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS gesturepwd" +
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT, userId VARCHAR, phone VARCHAR, status VARCHAR, pwd VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS gesturepwd");
		onCreate(db);
	}

}
