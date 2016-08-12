package com.billionav.navi.dataupdate;

public interface DataUpdateListener {
	/** 	 
	 * 删除一个文件后回调返回结果
	 * @param  fileName  删除文件名
	 * @param  fileName  删除文件序号，从1开始计数
	 * @param  fileName  总共要删除文件个数
	 * @param  isSuccess 删除是否成功 
	 */
	public void onDeleteFile( String fileName, int fileIndex, int totalFile, boolean isSuccess );
	/** 	 
	 * 清空数据完成
	 */
	public void onDataClearCompleted();
}
