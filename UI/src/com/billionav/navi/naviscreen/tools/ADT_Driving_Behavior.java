package com.billionav.navi.naviscreen.tools;


import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_RankListItem;
import com.billionav.ui.R;

public class ADT_Driving_Behavior extends ActivityBase {
    private ListViewNavi rank_list_view = null;
    private final ArrayList<UserControl_RankListItem> rankList = new ArrayList<UserControl_RankListItem>();
    private CProgressDialog progressDialog = null;
    private final int pagesize = 20;
    private RankListAdapter rankListAdapter = null;
    private LinearLayout loadingBar = null;
    private int request_status = 0; //0 need request ,1 requesting ,2 no need
    
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_driving_behavior_rank_list);
		setTitle(R.string.STR_MM_10_04_02_19);
		setDefaultBackground();
		rank_list_view = (ListViewNavi) findViewById(R.id.rank_list);
		loadingBar = (LinearLayout) View.inflate(this, R.layout.adt_driving_behavior_rank_list_item_load, null);
		
		if (isNetEnable()) {
			long isSend = UserControl_ManagerIF.Instance()
					.SNSMYDATAGetRankList("total", "", "", pagesize, rankList.size());
			if (isSend != -1 ) {
				progressDialog = CProgressDialog.makeProgressDialog(
						ADT_Driving_Behavior.this, R.string.STR_MM_10_04_01_02);
				progressDialog.show();
			}
		}
	}

	public void InitListView(){
		if(rankList!=null){
			rankListAdapter = new RankListAdapter(this);
			rank_list_view.addFooterView(loadingBar);	
			rank_list_view.setAdapter(rankListAdapter);
			rank_list_view.setClickable(false);
			rank_list_view.setCacheColorHint(0);
			rank_list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					if(request_status==2||request_status==1){
						return;
					}
					if(firstVisibleItem+visibleItemCount == totalItemCount){
						if(loadingBar!=null){
							rank_list_view.removeFooterView(loadingBar);
							rank_list_view.addFooterView(loadingBar);	
						}
						UserControl_ManagerIF.Instance().SNSMYDATAGetRankList("total", "", "", pagesize, rankList.size());
						request_status = 1;
					}
				}
			});
			if(loadingBar!=null){
				rank_list_view.removeFooterView(loadingBar);
			}
		}
	}
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {

		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_UC_MYDATA_GET_RANKLIST: //
			progressDialog.dismiss();
			if(request_status == 1){
				rank_list_view.removeFooterView(loadingBar);
			}
			request_status = 0;
			if(iParams == 0){
				if(UserControl_ManagerIF.Instance().getM_cRankListInfo().getItems().size()<20){
					request_status = 2;
				}
				rankList.addAll(UserControl_ManagerIF.Instance().getM_cRankListInfo().getItems());
				if(rankList.size()>0){
					if( null == rankListAdapter){
						InitListView();	
					}else{
						rankListAdapter.notifyDataSetChanged();
					}
					
					if(rankList.size() >= 100){
						request_status = 2;
					}
				}
				
			}else if (iParams == 1){
				CustomToast.showToast(ADT_Driving_Behavior.this, R.string.STR_COM_013, Toast.LENGTH_SHORT);
			}else if(iParams == 2){
				CustomToast.showToast(ADT_Driving_Behavior.this, R.string.STR_COM_014, Toast.LENGTH_SHORT);
			}
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}
	
	public class RankListAdapter extends BaseAdapter{

		 private LayoutInflater mInflater;

		 public RankListAdapter(Context context){
				mInflater = LayoutInflater.from(context);
			}
		 
		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			
			if(rankList.size()>=100){
				return 100;
			}else{
				return rankList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return rankList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			RankListItem item = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.adt_driving_behavior_rank_list_item, null);
				item = new RankListItem();
				item.nickNameView = (TextView) convertView.findViewById(R.id.rank_list_item_name);
				item.icon = (TextView)convertView.findViewById(R.id.rank_list_item_icon);
				item.scoresView = (TextView) convertView.findViewById(R.id.rank_list_item_scores);
				convertView.setTag(item);
			}else{
				item = (RankListItem) convertView.getTag();
			}
			item.icon.setText(String.valueOf(position+1));
			item.nickNameView.setEllipsize(TruncateAt.END);
			item.nickNameView.setText(rankList.get(position).getM_strUserNickName().toString());
			item.scoresView.setText(new DecimalFormat("#########.###").format(rankList.get(position).getM_fStars()));
			convertView.setBackgroundResource(R.drawable.list_selector_background);
			return convertView;
		}
		
		private class RankListItem{
			TextView icon = null;
			TextView nickNameView = null;
			TextView scoresView = null;
		}
	}
}
