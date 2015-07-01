package com.dony.yaolicitylist.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.dony.yaolicitylist.R;
import com.dony.yaolicitylist.bean.CityBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter {
	private static final String TAG = "CityListAdapter";
	private Context mContext;
	private ArrayList<CityBean> cityList;

	private String[] alphabetic;
	private HashMap<String, Integer> alphaIndexer;
	private HashMap<Integer, String> sections;


	public CityListAdapter(Context mContext, ArrayList<CityBean> cityList) {
		super();
		this.mContext = mContext;
		this.cityList = cityList;

		initData();
	}

	private void initData() {
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new HashMap<Integer, String>();
		StringBuffer localStringBuffer = new StringBuffer();
		this.alphaIndexer.put("#", Integer.valueOf(0));
		this.sections.put(Integer.valueOf(0), "#");
		localStringBuffer.append("#");

		this.alphaIndexer.put("$", Integer.valueOf(1));
		this.sections.put(Integer.valueOf(1), "$");
		localStringBuffer.append(",$");

		int m = cityList.size();
		for (int n = 0; n < m; n++) {
			String str = cityList.get(n).getPinyin().toString().substring(0, 1)
					.toUpperCase();
			if (!alphaIndexer.containsKey(str)) {
				this.alphaIndexer.put(str, Integer.valueOf(n + 3));
				this.sections.put(Integer.valueOf(n+3), str);
				localStringBuffer.append("," + str);
			}
		}
		this.alphabetic = localStringBuffer.toString().split(",");
	}

	public void setCityList(ArrayList<CityBean> cityList) {
		if (cityList == null)
			cityList = new ArrayList<CityBean>();
		else
			this.cityList = cityList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cityList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cityList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (cityList.size() == 0)
			return null;

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.citylist_item, null);
			holder = new ViewHolder();
			holder.cityNameTv = (TextView) convertView
					.findViewById(R.id.city_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cityNameTv.setPadding(20, 10, 0, 10);
		holder.cityNameTv.setText(cityList.get(position).getName());

		return convertView;
	}

	static class ViewHolder {
		private TextView cityNameTv;
		// private View divideView;
	}

	public String[] getAlphabetic() {
		return alphabetic;
	}

	public HashMap<String, Integer> getAlphaIndexer() {
		return alphaIndexer;
	}

	public HashMap<Integer, String> getSections() {
		return sections;
	}
}
