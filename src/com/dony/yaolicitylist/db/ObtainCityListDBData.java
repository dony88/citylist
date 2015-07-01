package com.dony.yaolicitylist.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dony.yaolicitylist.BuildConfig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class ObtainCityListDBData {
	private static final String TAG = "ObtainCityListDBData" ;
	private final int BUFFER_SIZE = 400000;
	private static final String PACKAGE_NAME = "com.dony.yaolicitylist";
	public static final String DB_NAME = "yaoli_cities.db";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME ;
	private Context mContext;

	public ObtainCityListDBData(Context context) {
		this.mContext = context;
	}

	public void openDateBase() {
		File cityDatabasesFile = new File(DB_PATH + "/" + DB_NAME, DB_NAME);
		if(!cityDatabasesFile.exists())
			this.openDateBase(DB_PATH + "/" + DB_NAME);

	}
	
	private SQLiteDatabase openDateBase(String dbFile) {
		File file = new File(dbFile);
		if (!file.exists()) {
			InputStream stream = null;
			try {
				stream = this.mContext.getResources().getAssets()
						.open("databases/yaoli_cities.db");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			if (stream == null){
				if(BuildConfig.DEBUG)
					Log.d(TAG, "open database file failed..............");
				return null;
			}

			try {
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				if(BuildConfig.DEBUG)
					Log.d(TAG, "copy file success==================");
				outputStream.close();
				stream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
