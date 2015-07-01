package com.dony.yaolicitylist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.dony.yaolicitylist.BuildConfig;

public class CommonUtil {
	private static final String TAG = "CommonUtil";

	public static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);
       
			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			if (BuildConfig.DEBUG)
				Log.d(TAG, "file copied ..................");
		} catch (FileNotFoundException ex) {
			if (BuildConfig.DEBUG)
				Log.d(TAG, ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			if (BuildConfig.DEBUG)
				Log.d(TAG, e.getMessage());
		}
	}
}
