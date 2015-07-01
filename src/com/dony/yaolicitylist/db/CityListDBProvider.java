package com.dony.yaolicitylist.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.dony.yaolicitylist.BuildConfig;
import com.dony.yaolicitylist.util.CommonUtil;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class CityListDBProvider extends ContentProvider {

	private static final String TAG = "CityListDBProvider";
	/* URI authority string */
	public static final String AUTHORITY = "com.dony.yaolicitylis.provider";

	/* URI paths names */
	public static final String CITY_TABLE = "city";

	private CityListDBHelper dbHelper;

	/* UriMatcher codes */
	private static final int PROVINCE_MATCH_IDS = 1;
	private static final int CITYS_MATCH = 10;
	private static final int CITYS_MATCH_IDS = 11;
	private static final int WEATHERS_MATCH_IDS = 21;
	private static final int DEFAULT_MATCH_IDS = 31;

	private static final UriMatcher sUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sUriMatcher.addURI(AUTHORITY, CITY_TABLE, CITYS_MATCH);
		sUriMatcher.addURI(AUTHORITY, CITY_TABLE + "/#", CITYS_MATCH_IDS);

	}

    public static boolean isFirstGet = false;
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "delete(" + uri + ", ...)");
		}
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			Log.e(TAG, "delete(): Wrong URI: " + uri);
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriWithID(match)) {
			selection = BaseColumns._ID
					+ "="
					+ uri.getLastPathSegment()
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		}

		SQLiteDatabase db;
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			// TODO Implement proper error handling
			Log.e(TAG, "delete(): Error opening writable database " + e);

			throw e;
		}

		int count;
		synchronized (this) {
			try {
				count = db.delete(tableName(match), selection, selectionArgs);
			} catch (SQLException e) {
				// TODO Implement proper error handling
				Log.e(TAG, "delete(): DB rows delete error " + e);

				throw e;
			}
		}

		if (BuildConfig.DEBUG)
			Log.d(TAG, "delete(): " + count + " rows deleted");

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "insert(" + uri + ", ...)");
		}
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			Log.e(TAG, "insert(): Wrong URI: " + uri);
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriWithID(match)) {
			Log.e(TAG, "insert(): Insert not allowed for this URI: " + uri);
			throw new IllegalArgumentException(
					"Insert not allowed for this URI: " + uri);
		}

		SQLiteDatabase db;
		long rowId;

		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			// TODO Implement proper error handling
			Log.e(TAG, "insert(): Error opening writeable database" + e);
			throw e;
		}

		synchronized (this) {
			try {
				rowId = db.insert(tableName(match), null, values);
			} catch (SQLException e) {
				// TODO Implement proper error handling
				Log.e(TAG, "Insert() failed " + e);

				throw e;
			}
		}

		if (rowId <= 0) {
			Log.e(TAG, "insert(): Error: insert() returned " + rowId);
			throw new RuntimeException("DB insert failed");
		}

		uri = ContentUris.withAppendedId(UriHelper.removeQuery(uri), rowId);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "insert(): new uri with rowId: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);

		return uri;
	}

	@Override
	public boolean onCreate() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "provider onCreate ......");
		}
		dbHelper = new CityListDBHelper(getContext(),CityListDBHelper.DB_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "query(" + uri + ",...)");
		}
		if(isFirstGet){
			if(BuildConfig.DEBUG)
				Log.d(TAG, "copy loacl db file =========================");
			replaceDBFile();
			isFirstGet = false;
		}
		
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			Log.e(TAG, "query(): Wrong URI");
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		int where_append_count = 0;
		qb.setTables(tableName(match));
		if (uriWithID(match)) {
			qb.appendWhere((where_append_count++ == 0 ? "" : " AND ")
					+ (BaseColumns._ID + "=" + uri.getLastPathSegment()));
		}

		if (TextUtils.isEmpty(sortOrder)) {
			sortOrder = "_ID ASC";
		}

		SQLiteDatabase db;
		try {
			db = dbHelper.getReadableDatabase();
		} catch (SQLiteException e) {
			// TODO Implement proper error handling
			Log.e(TAG, "query(): Error opening readable database " + e);

			throw e;
		}

		Cursor cursor;
		synchronized (this) {
			try {
				cursor = qb.query(db, projection, selection, selectionArgs,
						null, null, sortOrder);
			} catch (Throwable e) {
				Log.e(TAG, "query(): Exception at db query " + e);

				throw new RuntimeException("Exception at db query: "
						+ e.getMessage());
			}
		}

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "query(): Cursor has " + cursor.getCount() + " rows");
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "update(" + uri + ", ...)");
		}
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			Log.e(TAG, "update(): Wrong URI: " + uri);
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriWithID(match)) {
			selection = BaseColumns._ID
					+ "="
					+ uri.getLastPathSegment()
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		}

		SQLiteDatabase db;
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			// TODO Implement proper error handling
			Log.e(TAG, "update(): Error opening writable database " + e);

			throw e;
		}

		int count;
		synchronized (this) {
			try {
				count = db.update(tableName(match), values, selection,
						selectionArgs);
			} catch (SQLException e) {
				// TODO Implement proper error handling
				Log.e(TAG, "update() failed " + e);

				throw e;
			}
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "update(): " + count + " rows updated");
		}

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	private String tableName(int uri_match) {
		switch (uri_match) {
		case CITYS_MATCH:
			return CITY_TABLE;
		default:
			throw new Error(TAG + " No table defined for #" + uri_match);
		}
	}

	private boolean uriWithID(int uri_match) {
		switch (uri_match) {
		case PROVINCE_MATCH_IDS:
		case CITYS_MATCH_IDS:
		case WEATHERS_MATCH_IDS:
		case DEFAULT_MATCH_IDS:
			return true;

		default:
			return false;
		}
	}

	private void replaceDBFile() {
		// move db file from /data/data/package.../*.db to
		// /data/data/package.../databases/*.db
		String sourcefilePath = ObtainCityListDBData.DB_PATH + File.separator
				+ ObtainCityListDBData.DB_NAME;
		String destfilePath = ObtainCityListDBData.DB_PATH + File.separator
				+ "databases" + File.separator + ObtainCityListDBData.DB_NAME;

//		 new File(destfilePath).delete();
		CommonUtil.copyfile(sourcefilePath, destfilePath);
//		shellCommand(new String[]{"cp" ,ObtainCityListDBData.DB_NAME,
//		 "databases/"}, ObtainCityListDBData.DB_PATH);
		
		new File(sourcefilePath).delete();
		

	}

	public String shellCommand(String[] cmmand, String path) {
		String result = "";
		try {
			result = executeShell(cmmand, path);
		} catch (IOException e) {
			Log.e(TAG, "IOException " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public String executeShell(String[] cmmand, String directory)
			throws IOException {
		String result = "";
		try {
			ProcessBuilder builder = new ProcessBuilder(cmmand);

			if (directory != null)
				builder.directory(new File(directory));
			builder.redirectErrorStream(true);
			Process process = builder.start();

			InputStream is = process.getInputStream();
			byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				result = result + new String(buffer);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
