package com.billionav.jni;

public class UIPOIMemoryControlJNI {
	
	public static final int UIC_PM_POIOPENMENU = 0;		///<  :���j���[�I�[�v�����̒n�_���
	public static final int UIC_PM_POIPATHPOINT = 1;		///<  :�s����E�o�^�̒n�_���
	public static final int UIC_PM_POISTART = 2;			///<  :�o���n�̒n�_���
	public static final int UIC_PM_POILASTSEARCH = 3;		///<  :�ŏI�����n�_���
	public static final int UIC_PM_POIDEFAULTSCREEN = 4;	///<  :�����ʂ̒n�_���
	public static final int UIC_PM_POIDISPPOINT = 5;		///<  :�n�}
	public static final int UIC_PM_POIPOINTPOINT = 6;		///<  :point register only
	
	/**
	 * Instance 
	 * synchronized method
	 */
	public static synchronized UIPOIMemoryControlJNI Instance() {
		if (m_instance == null) {
			m_instance = new UIPOIMemoryControlJNI();
		}
		return m_instance;
	}

	/**
	 * POIMemoryData�N���X�擾�i�ǂݏ����p�j
	 *
	 * @param	const enum UIC_PM_ePOIDATAID ePOIDataID:�n�_���ID[IN]
	 * @return	POIMemoryData & :POIMemoryData�N���X
	 */
	public UIPOIMemoryData GetPOIMemoryData(int iPOIDataID){
		UIPOIMemoryData data = new UIPOIMemoryData();
		
		if (null != data) {
			data.SetDataID(iPOIDataID);
		}
		
		return data;
	}

	/**
	 * POIMemoryData�N���X�擾�i�������ݗp�j
	 *
	 * @param	const enum UIC_PM_ePOIDATAID ePOIDataID:�n�_���ID[IN]
	 * @return	POIMemoryData & :POIMemoryData�N���X
	 * @note  �w�肵���n�_��ʂ̒n�_���������ēn���܂��B
	 */
	public UIPOIMemoryData GetInitializedPOIMemoryData(int iPOIDataID){
		UIPOIMemoryData data = new UIPOIMemoryData();
		
		if (null != data) {
			data.SetDataID(iPOIDataID);
			data.Initialize();
		}
		
		return data;
	}


	/**
	 * Get Area By LonLat
	 *
	 * @param	
	 * @return
	 */
	public native String GetAreaNameByLonLat(long lat, long lng);
	
	private static UIPOIMemoryControlJNI m_instance = null;

}
