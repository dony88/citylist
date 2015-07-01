package com.dony.yaolicitylist.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.dony.yaolicitylist.BuildConfig;
import com.dony.yaolicitylist.util.CommonUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CityListDBHelper extends SQLiteOpenHelper {

	private static final String TAG = "CityListDbHelper";
	private static CityListDBHelper dbHelper;
	private SQLiteDatabase db_r = null;
	private SQLiteDatabase db_w = null;
	public static final int DB_VERSION = 1;
	private final Context mContext;

	public static synchronized CityListDBHelper getInstance(Context context, int version) {
		if (dbHelper == null) {
			dbHelper = new CityListDBHelper(context, version);
		}
		return dbHelper;
	}

	public CityListDBHelper(Context context, int version) {
		super(context, ObtainCityListDBData.DB_NAME, null, version);
		mContext = context;
	}

	public CityListDBHelper(Context context, String dbName ,int version) {
		super(context, dbName, null, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreate Database");
		}
		try {
			db.beginTransaction();
			
			db.setTransactionSuccessful();
		} catch (Throwable e) {
			Log.v(TAG, "onCreate(): DB creation failed:" + e);
			throw new RuntimeException("DB creation failed: " + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onUpgrade(oldVersion = " + oldVersion
					+ ", newVersion = " + newVersion + ")...");
		}

		db.execSQL("DROP TABLE IF EXISTS " + ObtainCityListDBData.DB_NAME);
		onCreate(db);
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		if (db_r == null || !db_r.isOpen()) {
			try {
				db_r = super.getReadableDatabase();
			} catch (SQLiteException e) {
				db_r = null;
				Log.d(TAG, "getReadableDatabase(): Error opening");
				throw e;
			}
		}
		return db_r;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (db_w == null || !db_w.isOpen() || db_w.isReadOnly()) {
			try {
				db_w = super.getWritableDatabase();
			} catch (SQLiteException e) {
				db_w = null;
				Log.d(TAG, "getWritableDatabase(): Error");
				throw e;
			}
		}
		return db_w;
	}
}
