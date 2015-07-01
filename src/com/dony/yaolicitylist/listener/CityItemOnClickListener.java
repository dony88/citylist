package com.dony.yaolicitylist.listener;

import com.dony.yaolicitylist.R;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CityItemOnClickListener implements OnItemClickListener {
    private final Context mContext ;
        
	public CityItemOnClickListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
        TextView mTextView = (TextView) arg1.findViewById(R.id.city_name);
        if(mTextView != null)
        	Toast.makeText(mContext, mTextView.getText() + String.valueOf(arg2), Toast.LENGTH_SHORT).show();
	}

}
