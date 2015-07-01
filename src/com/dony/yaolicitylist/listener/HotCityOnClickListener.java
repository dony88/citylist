package com.dony.yaolicitylist.listener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.dony.yaolicitylist.result.MyConstants;

public class HotCityOnClickListener implements OnClickListener{
	private final Context mContext;
	
	public HotCityOnClickListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch ((Integer)v.getTag()) {
		case MyConstants.SHANG_HAI:
			Toast.makeText(mContext, "SHANG_HAI", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.BEI_JING:
			Toast.makeText(mContext, "BEI_JING", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.GUANG_ZHOU:
			Toast.makeText(mContext, "GUANG_ZHOU", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.SHEN_ZHEN:
			Toast.makeText(mContext, "SHEN_ZHEN", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.WU_HAN:
			Toast.makeText(mContext, "WU_HAN", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.TIAN_JI:
			Toast.makeText(mContext, "TIAN_JI", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.XI_AN:
			Toast.makeText(mContext, "XI_AN", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.NAN_JING:
			Toast.makeText(mContext, "NAN_JING", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.HANG_ZHOU:
			Toast.makeText(mContext, "HANG_ZHOU", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.CHENG_DOU:
			Toast.makeText(mContext, "CHENG_DOU", Toast.LENGTH_SHORT).show();
			break;
		case MyConstants.CHONG_QING:
			Toast.makeText(mContext, "CHONG_QING", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

}
