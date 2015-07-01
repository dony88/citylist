package com.dony.yaolicitylist.location;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKGeocoderAddressComponent;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.dony.yaolicitylist.R;

import android.content.Context;
import android.location.Location;
import android.widget.TextView;
import android.widget.Toast;

public class GetCurrentCity {
	private static final String TAG = "GetCurrentCity";
	private Context mContext;
	public String mStrKey = "532D3A24D5AEFB2C1F507FE8825616A472C9F04E";
	public BMapManager mBMapMan = null;
	public boolean mapKeyRight = true;

	MKSearch mSearch;
	String currentCity;

	TextView mTextView;

	public GetCurrentCity(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}

	public void getCurrentCity(TextView mTextView) {
		this.mTextView = mTextView;
		
		initMKSearch();
		getCurrentGeoPoint();
	}
	
	public void close(){
		mBMapMan.stop();
		mBMapMan.destroy();
	}

	private void initMKSearch() {
		mBMapMan = new BMapManager(mContext);
		mBMapMan.init(this.mStrKey, new MKGeneralListener() {
			public void onGetNetworkState(int iError) {
				Toast.makeText(mContext, mContext.getString(R.string.net_work_disable), Toast.LENGTH_SHORT).show();
			}

			public void onGetPermissionState(int iError) {
				if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
					mapKeyRight = false;
				}
			}
		});

		mBMapMan.getLocationManager().setNotifyInternal(10, 5);
		mBMapMan.start();

		mSearch = new MKSearch();
		mSearch.init(mBMapMan, new MKSearchListener() {

			public void onGetAddrResult(MKAddrInfo res, int error) {
				// TODO Auto-generated method stub	
				if (error != 0 || res == null) {
					return;
				}
				MKGeocoderAddressComponent addressComponent = res.addressComponents;
				currentCity = addressComponent.city;
				mTextView.setText(currentCity);
			}

			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// TODO Auto-generated method stub	
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				// TODO Auto-generated method stub	
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				// TODO Auto-generated method stub	
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				// TODO Auto-generated method stub	
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				// TODO Auto-generated method stub	
			}
		});
	}

	private void getCurrentGeoPoint() {
		LocationListener mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					GeoPoint pt = new GeoPoint(
							(int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					mSearch.reverseGeocode(pt);
				}
			}
		};
		mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
	}
}
