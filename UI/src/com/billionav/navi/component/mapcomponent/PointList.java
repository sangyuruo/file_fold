/**
 * 
 */
package com.billionav.navi.component.mapcomponent;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchDetailInfoResultJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.naviscreen.report.ADT_report_detail;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Detail;
import com.billionav.ui.R;

/**
 * @author liuzhaofeng
 *
 */
public class PointList extends CustomDialog implements TriggerListener{
	
	private ArrayList<PointBean> list;
	private ArrayAdapter<PointBean> adapter;
	private final ListViewNavi listView;
	
	private long requestId = -1;
	private int index = -1;
	private final SparseArray<PointBean> requstList;
	
	public PointList(Context context) {
		super(context);
		listView = new ListViewNavi(context);
		addView(listView);
		
		requstList = new SparseArray<PointBean>();
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				GlobalTrigger.getInstance().removeTriggerListener(PointList.this);
				requstList.clear();
			}
		});
	}
	
	public void show(ArrayList<PointBean> list) {
		adapter = createAdapter(this.list = list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PointBean point = PointList.this.list.get(position);
				
				BundleNavi.getInstance().putInt("index", point.getIndex());
				BundleNavi.getInstance().putInt("searchKind", point.getType());
				
				if(point.getType() == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_USER_REPORT) {
					MenuControlIF.Instance().ForwardWinChange(ADT_report_detail.class);
				} else {
					MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Detail.class);
				}

				dismiss();
			}
		});
		show();
		GlobalTrigger.getInstance().addTriggerListener(this);
		findPointNeedRequest();
		requestPoint();
	}
	
	private void findPointNeedRequest() {
		for(int i=0; i<list.size(); i++) {
			String name = list.get(i).getName();
			int type = list.get(i).getType();
			
			if(type == UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL && TextUtils.isEmpty(name)) {
				requstList.put(i, list.get(i));
			}
		}
	}
	
	private ArrayAdapter<PointBean> createAdapter(ArrayList<PointBean> list) {
		ArrayAdapter<PointBean> adapter = new ArrayAdapter<PointBean>(getContext(), R.layout.dialog_item_select, R.id.text1, list) ;
		return adapter;
	}

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UISR_SEARCH_OK
				&& triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UISR_SEARCH_EXCEPTION
				&& triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UISR_SEARCH_ERROR) {
			return false;
		}
		
		if(triggerInfo.m_lParam1 != requestId) {
			return false;
		}
		
		if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_UISR_SEARCH_OK) {
			
			UISearchDetailInfoResultJNI mDetailResult = UISearchControlJNI.Instance().GetDetailInfoResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_CHECK_TOUCH_POINT);

			requstList.get(index).setName(mDetailResult.GetTouchedMarkName(index));
			requstList.remove(index);
			
			adapter.notifyDataSetChanged();
			requestPoint();
		}
		
		return false;
	}
	
	private void requestPoint() {
		if(requstList.size()>0){
			index = requstList.keyAt(0);
			PointBean point = requstList.valueAt(0);
			UISearchControlJNI.Instance().OnUpdateDetailInfo(UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL, point.getIndex());
			requestId = UISearchControlJNI.Instance().GetReqID(UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL);
			
		} else {
			requestId = -1;
			index = -1;
		}
	}

}
