package com.dony.yaolicitylist;

import java.util.ArrayList;
import java.util.HashMap;

import com.dony.yaolicitylist.bean.CityBean;
import com.dony.yaolicitylist.db.CityListDBHelper;
import com.dony.yaolicitylist.db.ObtainCityListDBData;
import com.dony.yaolicitylist.listener.CityItemOnClickListener;
import com.dony.yaolicitylist.listener.HotCityOnClickListener;
import com.dony.yaolicitylist.location.GetCurrentCity;
import com.dony.yaolicitylist.result.CityQuery;
import com.dony.yaolicitylist.result.MyConstants;
import com.dony.yaolicitylist.widget.CityListAdapter;
import com.dony.yaolicitylist.widget.QuickAlphabeticBar;
import com.dony.yaolicitylist.widget.QuickAlphabeticBar.OnTouchingAlphaChangedListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class YaolicitylistActivity extends Activity implements TextWatcher,OnClickListener,OnTouchingAlphaChangedListener{
	/** Called when the activity is first created. */
	private static final String TAG = YaolicitylistActivity.class
			.getSimpleName();
	private final Context mContext = this;

	private ListView mListView;
	private EditText searchEt;
	
	private LinearLayout headerGPSView;
	private TextView cityPositionLabelTv;
	private TextView enterYaoliLabelTv;
	private Button enterYaoliBtn;
	private ProgressBar getCurrentPositionPrssBr;
	
	private LinearLayout hotHeaderTitleView;
	private TextView hotHeaderTitleLabelTv;
	
	private LinearLayout cityListHeaderTitleView;
	private TextView cityListHeaderTitleLabelTv;
	
	private LinearLayout itemView;
	private TextView cityNameTv;

	private QuickAlphabeticBar quickAlphabeticBar;
	private HashMap<String, Integer> alphaIndexer;
	private HashMap<Integer, String> sections;
	private TextView overlay;
	private Handler handler;
	private OverlayThread overlayThread;
	private String[] alphabetic;
	private static boolean isShowOverlay = true;
	
	private ArrayList<CityBean> cityList;
	private ArrayList<CityBean> searchCityList;
	private CityListAdapter cityListAdapter;
	private GetCurrentCity getCurrentCity;
	
	private ObtainCityListDBData obtainCityListDBData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citylist);

		cityList = new ArrayList<CityBean>();
		searchCityList = new ArrayList<CityBean>();
		
		// from asserts obtain db file
		obtainCityListDBData = new ObtainCityListDBData(mContext);
		obtainCityListDBData.openDateBase();
				
		new CityListDBHelper(mContext, CityListDBHelper.DB_VERSION).getReadableDatabase().close();
		
		
		mListView = (ListView) this.findViewById(R.id.citylistview);
		searchEt = (EditText) this.findViewById(R.id.city_search);
		quickAlphabeticBar = (QuickAlphabeticBar) this.findViewById(R.id.quickAlphabeticBar);
		
		onLoadData();
		ensurUi();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		getCurrentCity.close();
		super.onDestroy();
	}

	private void onLoadData() {
		cityList = CityQuery.getCityList(mContext);
	    cityListAdapter = new CityListAdapter(mContext, cityList);
	}
    
	private void ensurUi(){
		listViewAddHeaderView();
		
		searchEt.addTextChangedListener(this);
		mListView.setAdapter(cityListAdapter);
		mListView.setOnItemClickListener(new CityItemOnClickListener(mContext));
		
		alphabetic = cityListAdapter.getAlphabetic();
		alphaIndexer = cityListAdapter.getAlphaIndexer();
		sections = cityListAdapter.getSections();
		quickAlphabeticBar.setAlphas(alphabetic);
		
		quickAlphabeticBar.setOnTouchingAlphaChangedListener(this);
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (s.length() != 0) {
			listViewHiddenHeadeView();
			
			isShowOverlay = false;
			if(searchCityList != null)
				searchCityList.clear();
			searchCityList = CityQuery.getSearchCityList(mContext,s.toString());
		    cityListAdapter.setCityList(searchCityList);    
		} else {
			listViewVisibleHeadeView();
			
			isShowOverlay = true;
			cityListAdapter.setCityList(cityList);
		}
		cityListAdapter.notifyDataSetChanged();
		mListView.invalidate();		
	}
	
	private void listViewAddHeaderView()
	  {
	    this.headerGPSView = ((LinearLayout)LayoutInflater.from(getApplicationContext()).inflate(R.layout.citylist_header_gps, null));
	    this.cityPositionLabelTv = ((TextView)this.headerGPSView.findViewById(R.id.city_position_label));
	    this.enterYaoliLabelTv = ((TextView)this.headerGPSView.findViewById(R.id.enter_yaoli_label));
	    this.enterYaoliBtn = ((Button)this.headerGPSView.findViewById(R.id.enter_yaoli_bt));
	    this.getCurrentPositionPrssBr = ((ProgressBar)this.headerGPSView.findViewById(R.id.get_current_position));
	    setLocationCityUIAndListner();
	    this.mListView.addHeaderView(this.headerGPSView);
	    	    
	    this.hotHeaderTitleView = ((LinearLayout)LayoutInflater.from(getApplicationContext()).inflate(R.layout.citylist_header_title, null));
	    this.hotHeaderTitleLabelTv = ((TextView)this.hotHeaderTitleView.findViewById(R.id.header_title_label));
	    this.hotHeaderTitleLabelTv.setText(getString(R.string.hot_city_label));
	    int i = MyConstants.hotCityList.length;
	    for (int j = 0; j < i; j++)
	    {
	      LinearLayout cityListItemLL = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.citylist_item, null);
	      TextView cityNameTv = (TextView) cityListItemLL.findViewById(R.id.city_name);
	      cityNameTv.setBackgroundResource(R.drawable.hot_city_item_bg);
	      cityNameTv.setPadding(20, 10, 0, 10);
	      cityNameTv.setText(MyConstants.hotCityList[j]);
	      cityNameTv.setTag(j);
	      cityNameTv.setOnClickListener(new HotCityOnClickListener(mContext));
	      if (j == i - 1)
	      {
	        View divideView = (View) cityListItemLL.findViewById(R.id.divideView);
	        divideView.setBackgroundColor(Color.TRANSPARENT);
	      }
	      
	      this.hotHeaderTitleView.addView(cityListItemLL);
	    }
	    this.mListView.addHeaderView(this.hotHeaderTitleView);
	    
	    this.cityListHeaderTitleView = ((LinearLayout)LayoutInflater.from(getApplicationContext()).inflate(R.layout.citylist_header_title, null));
	    this.cityListHeaderTitleLabelTv = ((TextView)this.cityListHeaderTitleView.findViewById(R.id.header_title_label));
	    this.cityListHeaderTitleLabelTv.setText(getString(R.string.city_list_label));
	    this.mListView.addHeaderView(this.cityListHeaderTitleView);

	  }

	private void listViewHiddenHeadeView(){
		if(this.headerGPSView != null)
			for(int i = 0;i<headerGPSView.getChildCount();i++){
				headerGPSView.getChildAt(i).setVisibility(View.GONE);
			}
		if(this.hotHeaderTitleView != null)
			for(int i = 0;i<hotHeaderTitleView.getChildCount();i++){
				hotHeaderTitleView.getChildAt(i).setVisibility(View.GONE);
			}
	}
	
	private void listViewVisibleHeadeView(){
		for(int i = 0;i<headerGPSView.getChildCount();i++){
			headerGPSView.getChildAt(i).setVisibility(View.VISIBLE);
		}
		for(int i = 0;i<hotHeaderTitleView.getChildCount();i++){
			hotHeaderTitleView.getChildAt(i).setVisibility(View.VISIBLE);
		}
		hotHeaderTitleView.setVisibility(View.VISIBLE);
	}
	
	private void setLocationCityUIAndListner(){
	    this.cityPositionLabelTv.setText(getString(R.string.location_city_label));
	    this.enterYaoliLabelTv.setText(getString(R.string.enter_yaoli_label));
	    getCurrentCity = new GetCurrentCity(mContext);
	    getCurrentCity.getCurrentCity(enterYaoliBtn);
	    
	    this.enterYaoliBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTouchingAlphaChanged(String s) {
		// TODO Auto-generated method stub
		if (alphaIndexer == null)
			return;

		if (alphaIndexer.get(s) != null && isShowOverlay) {
			int position = alphaIndexer.get(s);
			mListView.setSelection(position);
			overlay.setText(sections.get(position));
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			handler.postDelayed(overlayThread, 1500);
		}
	}
	
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}
	
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}
}