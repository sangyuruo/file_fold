package com.billionav.navi.naviscreen.version;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billionav.DRIR.jni.jniDRIR_CameraPreview;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Lience;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.update.UpdateController;
import com.billionav.ui.R;

public class ADT_Version_CheckVersionActivity extends ActivityBase{
	TextView checkVersion = null;
	TextView apkVersion = null;
	
	TextView versionDeclaration = null;
	TextView versionDeclarationLink = null;
	
	LinearLayout cameraVersionLayout = null;
	TextView cameraVersion = null;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_version_checkversion);
		setDefaultBackground();
		setTitle(R.string.STR_MM_06_01_01_92);
		findviews();
		checkVersion.setText(Html.fromHtml("<u>"+checkVersion.getText()+"</u>"));
		checkVersion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkVersion.setTextColor(Color.parseColor("#fc546c"));
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						requestApkNewVersion();
					}
				});
				checkVersion.setClickable(false);
			}
		});
		apkVersion.setText(SystemTools.getVersionString());
		
		int[] iIsMatchCamType = new int[1];
		String cameraLocalVersion = jniDRIR_CameraPreview.GetCameraDataVersion(iIsMatchCamType);
		if(!cameraLocalVersion.equals("")){
			cameraVersionLayout.setVisibility(View.VISIBLE);
			cameraVersion.setText(cameraLocalVersion);
		}
		
		operateLuxgenCorrelation();
	}
	private void findviews() {
		cameraVersionLayout = (LinearLayout) findViewById(R.id.version_cameraversion_layout);
		cameraVersion = (TextView) findViewById(R.id.version_cameraversion);
		
		checkVersion = (TextView) findViewById(R.id.version_check_textview);
		apkVersion = (TextView) findViewById(R.id.version_apkversion);
		
		versionDeclaration = (TextView) findViewById(R.id.version_declaration);
		versionDeclarationLink = (TextView) findViewById(R.id.version_declaration_link);
		
	}
	private void requestApkNewVersion() {
		UpdateController.getInstance().versionRequestApkList(true);
		
	}
	
	private void operateLuxgenCorrelation(){
		if(SystemTools.getApkEdition().equals(SystemTools.EDITION_LUXGEN)){
			versionDeclaration.setVisibility(View.VISIBLE);
			versionDeclarationLink.setVisibility(View.VISIBLE);
			versionDeclarationLink.setText(Html.fromHtml("<u>"+versionDeclarationLink.getText()+"</u>"));
			versionDeclarationLink.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					versionDeclarationLink.setTextColor(Color.parseColor("#fc546c"));
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							ForwardKeepDepthWinChange(ADT_Auth_Lience.class);
						}
					});
				}
			});
		}
	}

}
