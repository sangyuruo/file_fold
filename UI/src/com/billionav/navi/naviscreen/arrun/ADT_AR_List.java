package com.billionav.navi.naviscreen.arrun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.DRIR.Upload.UploadHandler;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.map.ADT_DR_Main;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.uicommon.UIC_ARVideoCommon;
import com.billionav.navi.uicommon.UIC_ARVideoCommon.VideoInfo;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;
public class ADT_AR_List extends ActivityBase implements OnScreenBackListener{
	
	private int count = 0;
    private int current_status = 3;
    private static final int CHECK_MAX_FILE_COUNT = 500;
    private static final int USER_NOT_LOGIN = 101;
    private static final int ANY_ERROR = -2;
    private int select_index = 0;
	private ListViewNavi listView;
	private final ArrayList<VideoInfo> videoInfoes = new ArrayList<UIC_ARVideoCommon.VideoInfo>();
	private ArrayList<VideoInfo> videoInfoes_all = null;
	private BaseAdapter adapter;
	private boolean beAbleToLoadPic = true;
	private static final String[] videoSuffix = new String[] { "3gp" };
	private final RelativeLayout[] panel = new RelativeLayout[4];
	private final int[] ids = {R.id.layout_all,
			R.id.layout_vedio, 
			R.id.layout_record,
			R.id.layout_pic };
	private boolean isReflash = true;
	private static final int MAX_BITMAP_QUEUE_SIZE = 30;
	private final LinkedList<Bitmap> bitmapQueue = new LinkedList<Bitmap>();

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		
		super.OnCreate(savedInstanceState);
		
		setContentView(R.layout.scr_ar_tab_list);
		setTitle(R.string.STR_MM_05_02_01_12);
		setDefaultBackground();
		isReflash = true;
		listView = (ListViewNavi) findViewById(R.id.ar_tab_list01);
		addActionItem3(R.string.STR_MM_01_03_03_01, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(videoInfoes!=null){
					if(videoInfoes_all.size()>0){
					ForwardWinChange(ADT_AR_List_Delete.class);
					}
				}
				
			}
		});
		adapter = new ArListAdapter(ADT_AR_List.this);

	}

	@Override
	protected void OnResume() {
		super.OnResume();
		if (isReflash||UIC_ARVideoCommon.getIsNeedReresh()) {
				
			new Handler() {

				@Override
				public void handleMessage(Message msg) {
					videoInfoes_all = UIC_ARVideoCommon
							.getVideoInfoes(getApplicationContext());
					if (videoInfoes_all.size() > 0) {
						videoInfoes.clear();
						videoInfoes.addAll(videoInfoes_all);
						listView.setCacheColorHint(0);
						listView.setAdapter(adapter);
						registerListViewEvent();

						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								AsynLoadThumTask task = new AsynLoadThumTask(
										++count);
								task.execute(0);
							}
						}, 150);

						init();
					}else{
						if(UIC_ARVideoCommon.getIsAllSelected()&&UIC_ARVideoCommon.getIsNeedReresh()){
							videoInfoes.clear();
							adapter.notifyDataSetChanged();
							UIC_ARVideoCommon.setIsAllSelected(false);
						}
					}
					UIC_ARVideoCommon.setIsNeedReresh(false);
				}

			}.sendEmptyMessage(1);
			isReflash = false;
		}
		
	}


	private void registerListViewEvent() {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isReflash = false;
				listItemClick(position);
			}
		});
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					beAbleToLoadPic = true;
					adapter.notifyDataSetChanged();
					break;
				default:
					beAbleToLoadPic = false;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}
	


	
	private void listItemClick(int index) {

		UIC_ARVideoCommon.getInstance().setPathOfWaitPlay(
				videoInfoes.get(index).getPath());
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		String path = videoInfoes.get(index).getPath();
		Uri name = Uri.parse("file://"+path);
		if (isVedioFile(path)) {
			intent.setDataAndType(name, "video/*");
		} else {
			intent.setDataAndType(name, "image/*");
		}
		DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_EXTERNAL_OTHER_MODE);
		startActivity(intent);
	}

	private boolean isVedioFile(String path) {
		int dotIndex = path.lastIndexOf(".");
		if (dotIndex > -1 && dotIndex < path.length() - 1) {
			String suffix = path.substring(dotIndex + 1);
			for (String str : videoSuffix) {
				if (suffix.equals(str)) {
					return true;
				}
			}
		}
		return false;

	}

	private static class ViewHolder {

		ImageView image02 = null;
		TextView txtView01 = null;
		TextView txtView02 = null;
		Button imagebtn01 = null;
		ProgressBar progressbar = null;
		ImageView complete = null;
		LinearLayout layout = null;
	}

	private class AsynLoadThumTask extends AsyncTask<Integer, Integer, Integer> {
		
		private final int id;
	
		
		private AsynLoadThumTask(int id){
			this.id = id;
		}

		@Override
		protected synchronized Integer doInBackground(Integer... params) {
			if(id != count) {
				return null;
			}

			if(UserControl_ManagerIF.Instance().HasLogin() && isWifiEnable()){
				
				ArrayList<String>  dm_path = new ArrayList<String>();
				ArrayList<String>  dr_path = new ArrayList<String>();
				ArrayList<String>  pic_path = new ArrayList<String>();

				for(int i = 0;i < videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_DM){
						dm_path.add(videoInfoes_all.get(i).getPath());
						if(dm_path.size() >= CHECK_MAX_FILE_COUNT)
						{
							String[] str = (String[])dm_path.toArray(new String[dm_path.size()]);
							UploadHandler.getInstance().IsOnServer(str, UploadHandler.UPLOAD_FILE_TYPE_DM);
							dm_path.clear();
						}
					}else if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_DR){
						dr_path.add(videoInfoes_all.get(i).getPath());
						if(dr_path.size() >= CHECK_MAX_FILE_COUNT)
						{
							String[] str = (String[])dr_path.toArray(new String[dr_path.size()]);
							UploadHandler.getInstance().IsOnServer(str, UploadHandler.UPLOAD_FILE_TYPE_DM);
							dr_path.clear();
						}
					}else if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_PIC){
						pic_path.add(videoInfoes_all.get(i).getPath());
						if(pic_path.size() >= CHECK_MAX_FILE_COUNT)
						{
							String[] str = (String[])pic_path.toArray(new String[pic_path.size()]);
							UploadHandler.getInstance().IsOnServer(str, UploadHandler.UPLOAD_FILE_TYPE_DM);
							pic_path.clear();
						}
					}
				}
				if(!dm_path.isEmpty()){
					String[] str = (String[])dm_path.toArray(new String[dm_path.size()]);
					UploadHandler.getInstance().IsOnServer(str, UploadHandler.UPLOAD_FILE_TYPE_DM);
				}
				if(!dr_path.isEmpty()){
					String[] str = (String[])dr_path.toArray(new String[dr_path.size()]);
					UploadHandler.getInstance().IsOnServer(str, UploadHandler.UPLOAD_FILE_TYPE_DR);
				}
				if(!pic_path.isEmpty()){
					String[] str = (String[])pic_path.toArray(new String[pic_path.size()]);
					UploadHandler.getInstance().IsOnServer(str, UploadHandler.UPLOAD_FILE_TYPE_PIC);
				}
			}else{
				if (!isWifiEnable()) {
					for (int i = 0; i < videoInfoes_all.size(); i++) {
						videoInfoes_all.get(i).setStatus(ANY_ERROR);
					}
				} else {
					for (int i = 0; i < videoInfoes_all.size(); i++) {
						videoInfoes_all.get(i).setStatus(USER_NOT_LOGIN);
					}
				}
			}
			
			publishProgress(3);
			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if(id != count) {
				return;
			}
			videoInfoes.clear();
			if(current_status == 0){
				for(int i=0;i<videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_DM){
						videoInfoes.add(videoInfoes_all.get(i));
					}
				}
			}else if(current_status == 1){
				for(int i=0;i<videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_DR){
						videoInfoes.add(videoInfoes_all.get(i));
					}
				}
			}else if(current_status == 2){
				for(int i=0;i<videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_PIC){
						videoInfoes.add(videoInfoes_all.get(i));
					}
				}
			}else if(current_status == 3){
				videoInfoes.addAll(videoInfoes_all);
			}
			
			adapter.notifyDataSetChanged();
		
		}

	}

	public class ArListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private SimpleDateFormat sd;

		public ArListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			sd = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
		}

		@Override
		public int getCount() {
			return videoInfoes.size();
		}

		@Override
		public Object getItem(int position) {
			return videoInfoes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.list_item_media_operate, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout_right);
				viewHolder.image02 = (ImageView) convertView
						.findViewById(R.id.list_Item_Media_Operate_image02);
				viewHolder.txtView01 = (TextView) convertView
						.findViewById(R.id.list_Item_Media_Operate_text01);
				viewHolder.txtView02 = (TextView) convertView
						.findViewById(R.id.list_Item_Media_Operate_text02);
				viewHolder.imagebtn01 = (Button) convertView
						.findViewById(R.id.list_Item_Media_Operate_image03);
				viewHolder.complete = (ImageView) convertView.findViewById(R.id.uploaded);
				viewHolder.complete.setVisibility(View.INVISIBLE);
				viewHolder.progressbar = (ProgressBar) convertView.findViewById(R.id.list_Item_Media_Operate_image_progressbar);
                viewHolder.progressbar.setVisibility(View.INVISIBLE);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Bitmap bm = null;
			if(beAbleToLoadPic){
				if(2 == videoInfoes.get(position).getType()){
					
					bm = UIC_ARVideoCommon.getImageThumbnail(videoInfoes.get(position).getPath(),60,60);
				}else{
					
					bm = ThumbnailUtils.createVideoThumbnail(videoInfoes.get(position).getPath(), Images.Thumbnails.MICRO_KIND);
				}
			}
			if(bm == null){
				viewHolder.image02.setImageResource(R.drawable.navicloud_and_592a);
			}else{
				viewHolder.image02.setImageBitmap(bm);
			}
			
			if(bitmapQueue.size() < MAX_BITMAP_QUEUE_SIZE){
			}else{
				Bitmap toDelete = bitmapQueue.removeFirst();
				if(toDelete != null){
					if(!toDelete.isRecycled()){
						toDelete.recycle();
					}
				}
			}
			if(bm != null){
				bitmapQueue.add(bm);
			}

			String txt1 = sd.format(videoInfoes.get(position).getDate());
			viewHolder.txtView01.setText(txt1);


			String sizeStr = null;
			if((videoInfoes.get(position).getSize()/1024)>1024){
				sizeStr = String.valueOf(UIC_ARVideoCommon.b2m(videoInfoes.get(
						position).getSize()))+"MB";
			}else{
				sizeStr = String.valueOf(videoInfoes.get(
						position).getSize()/1024)+"KB";
			}
			viewHolder.txtView02.setText(sizeStr);

			viewHolder.imagebtn01.setId(position);


			
			class upload implements OnClickListener{
				private final ViewHolder vh;
				boolean status;
				upload(ViewHolder vh,boolean status) {
					this.vh = vh;
					this.status = status;
				}
			@Override
			public void onClick(View v) {
				// status true wei shang chuan false shangchuanzhong
					if (status) {
						int ret = UploadHandler.getInstance().AddFile(
								videoInfoes.get(v.getId()).getPath(),
								videoInfoes.get(v.getId()).getType());
						if (ret == UploadHandler.ADD_FILE_SUCESS) {
							videoInfoes.get(v.getId()).setStatus(UploadHandler.FILE_UPLOADING);
							vh.imagebtn01.setVisibility(View.VISIBLE);
							vh.imagebtn01.setText(R.string.STR_MM_05_02_01_19);
							vh.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_757a);

							vh.progressbar.setVisibility(View.VISIBLE);
//							new AnimationHander(vh).sendEmptyMessage(1);
							status = false;
						} else if (ret == UploadHandler.ADD_FILE_EXISTED) {

						} else if (ret == UploadHandler.ABNORMAL) {
							
						}
					} else {
						int ret = UploadHandler.getInstance().cancelUpload(
								videoInfoes.get(v.getId()).getPath(),
								videoInfoes.get(v.getId()).getType());

						if (ret == UploadHandler.UPLOAD_CANCEL_OK) {
							videoInfoes.get(v.getId()).setStatus(UploadHandler.FILE_NOT_UPLOAD);
							vh.imagebtn01.setVisibility(View.VISIBLE);
							vh.imagebtn01.setText(R.string.STR_MM_05_02_01_17);
							vh.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_756a);
							vh.progressbar.setVisibility(View.INVISIBLE);
							status = true;
						} else if (ret == UploadHandler.UPLOAD_CANCEL_OK_UPLOADING) {
							videoInfoes.get(v.getId()).setStatus(UploadHandler.FILE_NOT_UPLOAD);
							vh.imagebtn01.setVisibility(View.VISIBLE);
							vh.imagebtn01.setText(R.string.STR_MM_05_02_01_17);
							vh.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_756a);
							vh.progressbar.setVisibility(View.INVISIBLE);
							status = true;
						} else if (ret == UploadHandler.ABNORMAL) {

						}
					}
				}
				
			}
			
			int status = videoInfoes.get(position).getStatus();
			if (status == -1) {
				viewHolder.imagebtn01.setVisibility(View.INVISIBLE);
				viewHolder.progressbar.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.INVISIBLE);
				viewHolder.layout.setVisibility(View.INVISIBLE);
			} else if (status == UploadHandler.FILE_NOT_UPLOAD) {
				viewHolder.layout.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setText(R.string.STR_MM_05_02_01_17);
				viewHolder.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_756a);
				viewHolder.imagebtn01.setOnClickListener(new upload(viewHolder, true));
				viewHolder.progressbar.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.INVISIBLE);

			}else if(status == UploadHandler.FILE_UPLOADING){
				viewHolder.layout.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setText(R.string.STR_MM_05_02_01_19);
				viewHolder.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_757a);
				viewHolder.imagebtn01.setOnClickListener(new upload(viewHolder, false));
                viewHolder.progressbar.setVisibility(View.VISIBLE);
                viewHolder.complete.setVisibility(View.INVISIBLE);
//                new AnimationHander(viewHolder).sendEmptyMessage(1);

			}else if(status == UploadHandler.FILE_INLIST_NOT_UPLOAD){
				viewHolder.layout.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setText(R.string.STR_MM_05_02_01_19);
				viewHolder.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_757a);
				viewHolder.imagebtn01.setOnClickListener(new upload(viewHolder, false));
                viewHolder.progressbar.setVisibility(View.VISIBLE);
                viewHolder.complete.setVisibility(View.INVISIBLE);
//                new AnimationHander(viewHolder).sendEmptyMessage(1);
                
			}else if(status == UploadHandler.FILE_UPLOADED){
				viewHolder.layout.setVisibility(View.INVISIBLE);
				viewHolder.imagebtn01.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.VISIBLE);
				viewHolder.progressbar.setVisibility(View.INVISIBLE);

			} else if (status == USER_NOT_LOGIN) {
				viewHolder.layout.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setText(R.string.STR_MM_05_02_01_17);
				viewHolder.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_756a);
				viewHolder.imagebtn01.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDialog(R.string.STR_MM_10_04_02_20,
								R.string.MSG_05_02_01_01,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										isReflash = true;
										ForwardWinChange(ADT_Auth_Login.class);
									}
								});
					}
				});
				viewHolder.progressbar.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.INVISIBLE);

			} else if (status ==ANY_ERROR){
				viewHolder.layout.setVisibility(View.VISIBLE);
				viewHolder.imagebtn01.setVisibility(View.VISIBLE);
				viewHolder.progressbar.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.INVISIBLE);
				viewHolder.imagebtn01.setText(R.string.STR_MM_05_02_01_17);
				viewHolder.imagebtn01.setBackgroundResource(R.drawable.navicloud_and_757a);
				viewHolder.imagebtn01.setOnClickListener(null);
			}else{
				
				viewHolder.layout.setVisibility(View.INVISIBLE);
				viewHolder.imagebtn01.setVisibility(View.INVISIBLE);
				viewHolder.progressbar.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.INVISIBLE);
			}
			if(SystemTools.getApkEdition().equals(SystemTools.EDITION_LUXGEN)){
				viewHolder.layout.setVisibility(View.INVISIBLE);
				viewHolder.imagebtn01.setVisibility(View.INVISIBLE);
				viewHolder.progressbar.setVisibility(View.INVISIBLE);
				viewHolder.complete.setVisibility(View.INVISIBLE);
			}
			convertView.setBackgroundResource(R.drawable.list_selector_background);
			return convertView;
		}
	}

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {

		switch (cTriggerInfo.GetTriggerID()) {
		case NSTriggerID.UIC_MN_TRG_DRIR_UPLOAD:
			for(int i = 0; i<videoInfoes_all.size();i++){
				if(videoInfoes_all.get(i).getPath().equals(cTriggerInfo.GetString1())){
					if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_UPLOAD_ERSULT_START){
						videoInfoes_all.get(i).setStatus(UploadHandler.FILE_UPLOADING);
					}else if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_UPLOAD_ERSULT_END){
						CustomToast.showToast(this, R.string.MSG_05_02_01_02, Toast.LENGTH_SHORT);
						videoInfoes_all.get(i).setStatus(UploadHandler.FILE_UPLOADED);
					}else if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_UPLOAD_ERSULT_FAIL){
						CustomToast.showToast(this, R.string.MSG_05_02_01_03, Toast.LENGTH_SHORT);
						videoInfoes_all.get(i).setStatus(UploadHandler.FILE_NOT_UPLOAD);
					}else if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_STATUA_NOT_UPLOAD){
						videoInfoes_all.get(i).setStatus(UploadHandler.FILE_NOT_UPLOAD);
					}else if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_STATUA_UPLOADING){
						videoInfoes_all.get(i).setStatus(UploadHandler.FILE_UPLOADING);
					}else if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_STATUA_UPLOADED){
						videoInfoes_all.get(i).setStatus(UploadHandler.FILE_UPLOADED);
					}else if((int)cTriggerInfo.GetlParam1() == UploadHandler.TRIGGER_FILE_STATUA_ABNORMAL){
					
						videoInfoes_all.get(i).setStatus(ANY_ERROR);
					}
						
					break;
				}
			}
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}

		return super.OnTrigger(cTriggerInfo);
	}

	 private void showDialog(int titleID,int messageID,  DialogInterface.OnClickListener listener){
		   CustomDialog b = new CustomDialog(
					ADT_AR_List.this);
			b.setMessage(messageID);

			b.setTitle(titleID);
			b.setNegativeButton(R.string.STR_MM_10_04_02_22, null);

			b.setPositiveButton(R.string.STR_MM_10_04_02_23,listener);
			b.show();
	   }
	public void init() {
		
		for (int i = 0; i < panel.length; i++) {
			panel[i] = (RelativeLayout) findViewById(ids[i]);
		}
		setFocusIndex(select_index);
		panel[0].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select_index = 0;
				setFocusIndex(select_index);
				current_status = 3;
				videoInfoes.clear();
				videoInfoes.addAll(videoInfoes_all);
				adapter.notifyDataSetChanged();
			}
		});

		panel[1].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select_index = 1;
				setFocusIndex(select_index);
				current_status = 0;
				videoInfoes.clear();
				for(int i=0;i<videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_DM){
						videoInfoes.add(videoInfoes_all.get(i));
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		panel[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_index = 2;
				setFocusIndex(select_index);
				current_status = 1;
				videoInfoes.clear();
				for(int i=0;i<videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_DR){
						videoInfoes.add(videoInfoes_all.get(i));
					}
				}
			    adapter.notifyDataSetChanged();
			}
		});
		panel[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_index = 3;
				setFocusIndex(select_index);
				current_status = 2;
				videoInfoes.clear();
				for(int i=0;i<videoInfoes_all.size();i++){
					if(videoInfoes_all.get(i).getType() == UploadHandler.UPLOAD_FILE_TYPE_PIC){
						videoInfoes.add(videoInfoes_all.get(i));
					}
				}
				adapter.notifyDataSetChanged();
			}
		});

	}

	public void setFocusIndex(int index) {
		for (int i = 0; i < panel.length; i++) {
			if (i == index) {
				if(index == 0){
					panel[i].setBackgroundResource(R.drawable.navicloud_and_766b);
				}else if(index == 3){
					panel[i].setBackgroundResource(R.drawable.navicloud_and_766b);
				}else{
					panel[i].setBackgroundResource(R.drawable.navicloud_and_766b);
				}
				
				panel[i].getChildAt(0).setSelected(true);
				
			} else {
				if(i == 0){
					panel[i].setBackgroundResource(R.drawable.navicloud_and_766a);
				}else if(i == 3){
					panel[i].setBackgroundResource(R.drawable.navicloud_and_766a);
				}else{
					panel[i].setBackgroundResource(R.drawable.navicloud_and_766a);
				}
				panel[i].getChildAt(0).setSelected(false);
			}
		}
	}

	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
        while(bitmapQueue.size()>0){
    		Bitmap toDelete = bitmapQueue.removeFirst();
			if(toDelete != null){
				if(!toDelete.isRecycled()){
					toDelete.recycle();
				}
     }
     }
	}

	@Override
	public void onBack() {
		ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_EXTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_LOW_DM_MODE);
		ForwardKeepDepthWinChange(ADT_DR_Main.class);
		
	}
}
