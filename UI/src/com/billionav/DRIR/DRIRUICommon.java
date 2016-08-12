package com.billionav.DRIR;

import android.util.Log;
import android.widget.Toast;

import com.billionav.DRIR.PictureHandler.PictureHandler;
import com.billionav.DRIR.jni.jniDRIR_DRVEventMsgBox;
import com.billionav.navi.camera.CameraView;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;



public class DRIRUICommon {
	private static String TAG = "DRIRUICommon";
	private static int siPreviewStartCnt = 0;
	public static boolean OnTrigger(NSTriggerInfo cTriggerInfo){
		switch(cTriggerInfo.GetTriggerID())
		{
		case NSTriggerID.UIC_MN_TRG_DRIR_TAKEPIC:
			{
				int iStartFlag = PictureHandler.DR_TAKEPIC_BEFORE;
				if (1 == cTriggerInfo.GetlParam1())
				{
					iStartFlag = PictureHandler.VOICE_TAKEPIC;
					//Start take pic
					PictureHandler.getInstance().setOneShootFlag(true, iStartFlag, cTriggerInfo.GetlParam2());
				}
				else
				{
					iStartFlag = PictureHandler.DR_TAKEPIC_BEFORE;
					//Start take pic
					PictureHandler.getInstance().setTakePicFlag(true, iStartFlag, cTriggerInfo.GetlParam2());
				}
				
				
			}
			break;		
		case NSTriggerID.UIC_MN_TRG_DRIR_STOPPREVIEW:
			Log.i("JNI_NATIVEC", "Receive the stop preview trigger");
			//CameraView.Instance().StopPreview();
			break;
		default:
			break;
		}
		return false;
	}
}
