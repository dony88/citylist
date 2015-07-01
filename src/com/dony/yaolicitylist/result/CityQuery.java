package com.dony.yaolicitylist.result;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.dony.yaolicitylist.BuildConfig;
import com.dony.yaolicitylist.bean.CityBean;
import com.dony.yaolicitylist.db.CityListDBProvider;
import com.dony.yaolicitylist.db.UriHelper;

public class CityQuery {
	public static final String TAG = "CityQuery";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PINYIN = "pinyin";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String IS_OPEN = "isOpen";
	public static final String DIVISION = "divisionStr";

	public static final int ID_INDEX = 0;
	public static final int NAME_INDEX = 1;
	public static final int PINYIN_INDEX = 2;
	public static final int LAT_INDEX = 3;
	public static final int LNG_INDEX = 4;
	public static final int ISOPEN_INDEX = 5;
	public static final int DIVISION_INDEX = 6;

	public static final int CAPACITY_SIZE = 480;
	public static final String[] SUMMARY_PROJECTION = new String[] { ID, NAME,
			PINYIN, LAT, LNG, IS_OPEN, DIVISION };
	public static final String QUERY_CITY_LIST_BY_ALL = null;
	public static final String QUERY_CITY_LIST_BY_SEARCH = NAME + " like '%"
			+ "?" + "%' or " + PINYIN + " like '%" + "?" + "%'";
	public static final String SORT_ORDER_BY_ID = ID + " ASC";
	public static final String SORT_ORDER_BY_PINYIN = PINYIN + " ASC";

	public static final ArrayList<CityBean> getCityList(Context mContext) {
		CityListDBProvider.isFirstGet = true;
		ArrayList<CityBean> cityList = new ArrayList<CityBean>(CAPACITY_SIZE);
		Uri uri = UriHelper.getUri(CityListDBProvider.CITY_TABLE);

		Cursor cursor = mContext.getContentResolver().query(uri,
				SUMMARY_PROJECTION, QUERY_CITY_LIST_BY_ALL, null, SORT_ORDER_BY_PINYIN);
		if (cursor == null || !cursor.moveToFirst()) {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			return null;
		}
		do {
			CityBean city = new CityBean();
			city.setId(cursor.getInt(ID_INDEX));
			city.setName(cursor.getString(NAME_INDEX));
			city.setPinyin(cursor.getString(PINYIN_INDEX));

			cityList.add(city);
		} while (cursor.moveToNext());
		
        cursor.close();
        cursor = null;
        
		return cityList;
	}

	public static final ArrayList<CityBean> getSearchCityList(Context mContext,
			String searchStr) {
//		CityListDBProvider.isFirstGet = false;
		ArrayList<CityBean> cityList = new ArrayList<CityBean>();
		Uri uri = UriHelper.getUri(CityListDBProvider.CITY_TABLE);

		Cursor cursor = mContext.getContentResolver()
				.query(uri, SUMMARY_PROJECTION, NAME + " like '%"
						+ searchStr + "%' or " + PINYIN + " like '%" + searchStr + "%'",
						null, SORT_ORDER_BY_PINYIN);
//		Cursor cursor = mContext.getContentResolver()
//				.query(uri, SUMMARY_PROJECTION, QUERY_CITY_LIST_BY_SEARCH,
//						new String []{ searchStr ,searchStr }, SORT_ORDER);
		if (cursor == null || !cursor.moveToFirst()) {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			return null;
		}
		do {
			CityBean city = new CityBean();
			city.setId(cursor.getInt(ID_INDEX));
			city.setName(cursor.getString(NAME_INDEX));
			city.setPinyin(cursor.getString(PINYIN_INDEX));

			cityList.add(city);
		} while (cursor.moveToNext());
		
        cursor.close();
        cursor = null;
        
		return cityList;
	}
}
