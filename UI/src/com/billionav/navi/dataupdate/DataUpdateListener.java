package com.billionav.navi.dataupdate;

public interface DataUpdateListener {
	/** 	 
	 * ɾ��һ���ļ���ص����ؽ��
	 * @param  fileName  ɾ���ļ���
	 * @param  fileName  ɾ���ļ���ţ���1��ʼ����
	 * @param  fileName  �ܹ�Ҫɾ���ļ�����
	 * @param  isSuccess ɾ���Ƿ�ɹ� 
	 */
	public void onDeleteFile( String fileName, int fileIndex, int totalFile, boolean isSuccess );
	/** 	 
	 * ����������
	 */
	public void onDataClearCompleted();
}
