package com.dony.yaolicitylist.db;

import com.dony.yaolicitylist.BuildConfig;

import android.net.Uri;
import android.util.Log;

public class UriHelper {

	private static final String TAG = "UriHelper";

	public static Uri getUri(String path) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "getUri(" + path + ')');

		Uri uri = prepare(path).build();

		if (BuildConfig.DEBUG)
			Log.d(TAG, "uri: " + uri);

		return uri;
	}

	private static Uri.Builder prepare(String path) {
		return new Uri.Builder().scheme("content")
				.authority(CityListDBProvider.AUTHORITY).path(path).query("")
				.fragment("");

	}

	static Uri removeQuery(Uri uri) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "removeQuery(" + uri + ")");

		Uri newUri = uri.buildUpon().query("").build();

		if (BuildConfig.DEBUG)
			Log.d(TAG, "new uri: " + newUri);

		return newUri;
	}

}
