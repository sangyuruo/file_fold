package com.billionav.navi.naviscreen.report;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.jni.UIUserReportJNI;
import com.billionav.jni.UIUserReportJNI.BroadcastResourceByID;
import com.billionav.jni.UIUserReportJNI.QueryPostDetailRes;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.PointTools.PointDetailInfoListener;
import com.billionav.navi.uitools.SystemTools;
//import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_report_detail extends ActivityBase{

	private RelativeLayout info_layout = null;
	private RelativeLayout severity_layout = null;
	private RelativeLayout pic_layout = null;
	private RelativeLayout position_layout = null;
	private LinearLayout detail_layout = null;
	private TextView content_text = null;
	private TextView time_text = null;
	private TextView info_text = null;
	private TextView source_text = null;
	private ImageView pic_image = null;
	private CProgressDialog progressdialog = null;
	private TextView position_text = null;
	private ImageView content_icon = null;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_report_detail);
		setTitleText(R.string.STR_MM_08_05_01_01);
		setDefaultBackground();
		findViews();
		if (isNetEnable()) {
//			String postId = jniOrbisControl.Instance().GetPostIDByIndex(index);
			UIUserReportJNI.sendUserReportDeatil(getBundleNavi().getString("postID"));
//			long isTrue = UserControl_ManagerIF.Instance()
//					.SNSQueryPosterDetails(postId);
//			if (isTrue != -1) {
				lockForClickListener();
				progressdialog = CProgressDialog.makeProgressDialog(
						ADT_report_detail.this, R.string.STR_MM_10_04_02_18);
				progressdialog.show();
//			}
		}
	}
	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_UC_PM_QUERY_POSTER_DETAILS:
			progressdialog.dismiss();
		    if(iParams == 0){
		    	updateReportDetail();
		    }else if(iParams == 1){
		    	CustomToast.showToast(ADT_report_detail.this, R.string.STR_COM_013, Toast.LENGTH_SHORT);
		    }else if(iParams == 2){
		    	CustomToast.showToast(ADT_report_detail.this, R.string.STR_COM_014, Toast.LENGTH_SHORT);
		    }
			return true;
		case NSTriggerID.UIC_MN_TRG_UC_GR_GET_RESOURCE:
			if(iParams == 0){
//				byte[] temp = UserControl_ManagerIF.Instance().getM_bytePosterPictureInfo();
				BroadcastResourceByID resource = UIUserReportJNI.getBroadcastResourceByID();
				byte[] temp = resource.ResourceBuffer;
				if(temp != null){
					if(temp.length!=0){
						pic_layout.setVisibility(View.VISIBLE);
					pic_image.setVisibility(View.VISIBLE);
					Bitmap bit = BitmapFactory.decodeByteArray(temp, 0, temp.length);
					pic_image.setImageBitmap(bit);
				}
				}
			}
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}

	private void updateReportDetail() {
		
		QueryPostDetailRes detailRes = UIUserReportJNI.getUserReportDeatil();
		
//		String typeStr = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrType();
//		int category = Integer.parseInt(UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrCategory());
		int type = detailRes.cDetailInfo.Type;
		int category = detailRes.cDetailInfo.category;
//		int type = -1;
//		if(!TextUtils.isEmpty(typeStr)){
//			type = Integer.parseInt(typeStr);
//		}
		setReportContent(category,type);
//		String positionLocStr = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrLonLat();
//		String timeStr = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrPostTime();
		
		String timeStr = detailRes.cDetailInfo.PostTime;
		timeStr = SystemTools.getLocalTimeText(timeStr);
        int[] lonlat = new int[2];
        lonlat[0] = detailRes.cDetailInfo.lon;
        lonlat[1] = detailRes.cDetailInfo.lat;
//        if(!TextUtils.isEmpty(positionLocStr)) {
//            String[] tempLonLat = positionLocStr.split(",");
//            lonlat = new int[]{Integer.parseInt(tempLonLat[0]),Integer.parseInt(tempLonLat[1])};
            PointTools.getInstance().requestPointName(lonlat[0], lonlat[1], new PointDetailInfoListener() {
				
				@Override
				public void onPointDetailInfoRequstFinished(String name,
						String address, String tel, long distance) {
					// TODO Auto-generated method stub
					if(position_layout==null){
						return;
					}
					position_layout.setVisibility(View.VISIBLE);
					position_text.setText(name);
				}
			});
//        }

//		String userName = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrCreateNeckName();
//		String urlImage = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrURL();
//		String infoStr = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrText();
		String userName = detailRes.cDetailInfo.NickName;
//		String urlImage = UserControl_ManagerIF.Instance().getM_cPosterDetails().getStrURL();
		String infoStr = detailRes.cDetailInfo.Text;
		
		
		if(!TextUtils.isEmpty(timeStr)){
			time_text.setText(timeStr);
		}
		if(!TextUtils.isEmpty(infoStr)){
			info_layout.setVisibility(View.VISIBLE);
			info_text.setText(infoStr);
		}else{
			info_layout.setVisibility(View.VISIBLE);
			setDefaultInfo(category,type);
		}
		if(!TextUtils.isEmpty(userName)){
			if(userName.equals("anonymous")){
				source_text.setText(R.string.STR_MM_08_05_01_46);
			}else{
				source_text.setText(userName);
			}
		}
//		if(!TextUtils.isEmpty(urlImage)){
//			UserControl_ManagerIF.Instance().SNSGetResource(1, urlImage, "", "");
//			pic_layout.setVisibility(View.GONE);
//		}else{
//			pic_layout.setVisibility(View.GONE);
//		}
		detail_layout.setVisibility(View.VISIBLE);
	}

	private void setDefaultInfo(int category ,int type){
		switch(category){
		case 1:
			info_text.setText(R.string.STR_MM_08_05_01_25);
			break;
		case 2:
			info_text.setText(R.string.STR_MM_08_05_01_22);
			break;
		case 3:
			info_text.setText(R.string.STR_MM_08_05_01_23);
			break;
		case 4:
			if(type ==1){
				info_text.setText(R.string.STR_MM_08_05_01_43);
			}else if(type ==2){
				info_text.setText(R.string.STR_MM_08_05_01_44);
			}else if(type == 3){
				info_text.setText(R.string.STR_MM_08_05_01_41);
			}else if(type == 4){
				info_text.setText(R.string.STR_MM_08_05_01_42);
			}
			break;
		case 5: 
			info_text.setText(R.string.STR_MM_08_05_01_21);
			break;
	
			default:
				break;
		}
	}
	private void setReportContent(int category,int type) {
		switch(category){
		case 0:// camera
			content_text.setText(R.string.STR_MM_08_05_01_45);
			content_icon.setImageResource(R.drawable.navicloud_and_260b);
			break;
		case 1:// Traffic control
			content_text.setText(R.string.STR_MM_08_05_01_30);
			content_icon.setImageResource(R.drawable.navicloud_and_263b);
			break;
		case 2:// Traffic accident
			content_text.setText(R.string.STR_MM_08_05_01_27);
			content_icon.setImageResource(R.drawable.navicloud_and_261b);
			break;
		case 3://Traffic congestion
			content_text.setText(R.string.STR_MM_08_05_01_28);
			content_icon.setImageResource(R.drawable.navicloud_and_262b);
			break;
		case 4://Dangerous sections
			if(type == 1){
				content_text.setText(R.string.STR_MM_08_05_01_34);
				content_icon.setImageResource(R.drawable.navicloud_and_267b);
			}else if(type ==2){
				content_text.setText(R.string.STR_MM_08_05_01_35);
				content_icon.setImageResource(R.drawable.navicloud_and_268b);
			}else if(type == 3){
				content_text.setText(R.string.STR_MM_08_05_01_36);
				content_icon.setImageResource(R.drawable.navicloud_and_265b);
			}else if(type == 4){
				content_text.setText(R.string.STR_MM_08_05_01_37);
				content_icon.setImageResource(R.drawable.navicloud_and_266b);
			}
			break;
		case 5://police
			content_text.setText(R.string.STR_MM_08_05_01_26);
			content_icon.setImageResource(R.drawable.navicloud_and_264b);
			break;
			default:
				break;
		}
	}
	private void findViews() {
		info_layout = (RelativeLayout) findViewById(R.id.info_layout);
		severity_layout = (RelativeLayout) findViewById(R.id.severity_layout);
		pic_layout = (RelativeLayout) findViewById(R.id.pic_layout);
		position_layout =(RelativeLayout) findViewById(R.id.position_layout);
		content_text = (TextView) findViewById(R.id.report_content);
		time_text = (TextView) findViewById(R.id.time);
		info_text = (TextView) findViewById(R.id.info);
		source_text = (TextView) findViewById(R.id.source);
		pic_image = (ImageView) findViewById(R.id.pic);
		detail_layout = (LinearLayout) findViewById(R.id.detail_layout);
		content_icon = (ImageView)findViewById(R.id.report_content_icon);
		detail_layout.setVisibility(View.INVISIBLE);
		position_text = (TextView) findViewById(R.id.position);
		severity_layout.setVisibility(View.GONE);
		position_layout.setVisibility(View.GONE);
	}
}
