package com.billionav.navi.system;

import java.io.File;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.StatFs;


//import com.pset.jni.jniDirectionsIF;
import com.billionav.jni.FileSystemJNI;
import com.billionav.navi.update.UpdateController;

public class ADataManager {
	
	public static final String DATA_PATH_JP = "./mnt/sdcard/.kanavijp";
	public static final String DATA_PATH_CH = "./mnt/sdcard/.kanavicn";
	public static final String DATA_PATH_US = "./mnt/sdcard/.kanavius";

	private AssetManager mAssertManager = null;
	private boolean mNeedReplace = false;

	public final static int STORAGE_OK = 0;
	public final static int STORAGE_UNMOUNTED = 1;
	public final static int STORAGE_LOWCAPACITY = 2;
	
	private final int MIN_SIZE = 5;	// 5M

	private ADataManager(Context c) {
		if(c != null) {
			mAssertManager = c.getAssets();
			
			// Set Assets to NaviFile
			NaviFile.SetAssetManager(mAssertManager);
		}
	}
	
	public static int prepareAdata(Context c) {
		ADataManager am = new ADataManager(c);
		int ret = am.checkStorage();
		if(ret != STORAGE_OK) {
			PLog.e("ADataManager", "SD Card not ready or low capacity!");
		}

		am = null;
		return ret;
	}
	
	int checkStorage() {
		if(!_isSDCardReady()) {
			return STORAGE_UNMOUNTED;
		}
		if(!_isSDCardCapacityEnough()) {
			return STORAGE_LOWCAPACITY;
		}
		return STORAGE_OK;
	}

	protected boolean _isSDCardReady() {
		PLog.d("ADataManager", "Current storage\t"
				+ StorageAdapter.getCurrentStorageDirectory().getPath()
				+ "\t state\t" + StorageAdapter.getCurrentStorageState());

		return StorageAdapter.getCurrentStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	protected boolean _isSDCardCapacityEnough() {
		// DO NOT care the following case:
		// internal storage exists, but its capacity is too low for apl,
		// and need to check external storage's state and capacity,
		// reset current storage to external storage
		// but, when user remove something in internal storage, and capacity
		// is
		// enough,
		// apl is restarted, it should still to use external storage,
		// otherwise,
		// errors will occur

		StatFs statfs = new StatFs(StorageAdapter
				.getCurrentStorageDirectory().getPath());
		// every block size
		long blockSize = statfs.getBlockSize() /1024; // KB as unit
		// total block count
		 long totalBlocks = statfs.getBlockCount();
		// available block count
		long availableBlocks = statfs.getAvailableBlocks();

		PLog.d("ADataManager", "Current storage\t"
				+ StorageAdapter.getCurrentStorageDirectory().getPath()
				+ "\t"+availableBlocks*blockSize/1024+"/"+totalBlocks*blockSize/1024+" MB");
		
		return ((blockSize * availableBlocks/1024) > MIN_SIZE) ? true : false;
	}
	
	
	boolean isNeedReplace() {
		return mNeedReplace;
	}

	// *********************************************************************
	//  Must be called after ADataManager is instanced at least once.
	//  Copy files under asset/appdata to sdcard dir defined by BaseGroup.
	//  Copy files under asset/appdata/NDATA/NDATA to NDATA's dir defined by BaseGroup.
	//  do nothing if above dir is not included in apk.
	// *********************************************************************
	 public static void CopyAppData(Context c) {
		String output = null;
		int syncmode;
		File f = new File(FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH));
//		File f = new File("/mnt/sdcard/.kanavicn/NDATA");
		
		
		if (UpdateController.getInstance().hasAPKUpdated(c) || !f.exists()){
			syncmode = NaviFile.NAVI_SYNCMODE_OVERWRITE;

			output = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_SDCARD_PATH);
//			output = "/mnt/sdcard/.kanavicn";
			NaviFile.AssetDirCopy("appdata", output, syncmode, "NDATA");

			output = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH);
//			output = "/mnt/sdcard/.kanavicn/NDATA";
			NaviFile.AssetDirCopy("appdata/NDATA", output, syncmode);
		}
		
		f = new File(FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH));
//		f = new File("/mnt/sdcard/kanavicn/USER");
		
		if(null!= f && !f.exists()){
			f.mkdirs();
		}
		f = null;
		output = null;
	}

}

