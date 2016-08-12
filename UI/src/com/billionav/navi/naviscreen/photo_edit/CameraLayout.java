package com.billionav.navi.naviscreen.photo_edit;


import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.ui.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class CameraLayout extends RelativeLayout{
	private MySurfaceView cameraView;
	private Matrix matrix;
	public CameraLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	private void init(Context context, AttributeSet attrs) {
		cameraView = new MySurfaceView(context); 
		addView(cameraView);
	}
	public void setPic(Bitmap bm){
		cameraView.setPic(bm);
	}
	public void recycleBitmapBuff(){
		cameraView.recycleBitmapBuff();
	}
	public Bitmap getCutBitmap() {
		Matrix m = new Matrix();
		Bitmap bm = Bitmap.createBitmap(cameraView.getBitmap()
				, (int)(cameraView.touchGrid.getLeft() - cameraView.imageLeft)
				, (int)(cameraView.touchGrid.getTop() - cameraView.imageTop)
				, (int)(cameraView.touchGrid.getRight() - cameraView.touchGrid.getLeft())
				, (int)(cameraView.touchGrid.getBottom() - cameraView.touchGrid.getTop()));
		float scaleX = (float)BASIC_WIDTH/bm.getWidth();
		float scaleY = (float)BASIC_WIDTH/bm.getHeight();
		m.postScale(scaleX, scaleY);
		bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, false);
		return bm;	
	}
	
	public void resetVars() {
		cameraView.recycleBitmapBuff();
		cameraView.touchGrid = null;
//		matrix = null;
		
	}
	public class MySurfaceView extends View {
		private TouchGrid touchGrid;
		private Bitmap image;
		private float imageLeft;
		private float imageTop;
		private float imageRight;
		private float imageBottom;
		private float boundary;
		public void setPic(Bitmap bm){
			image =bm;
			matrix = null;
		}
		public MySurfaceView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		public MySurfaceView(Context context) {
			super(context);
		}
		public Bitmap getBitmap(){
			
			return image;
		}
		public void recycleBitmapBuff(){
			matrix = null;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if(matrix == null){
				if(touchGrid == null){
					touchGrid = new TouchGrid();
					boundary = touchGrid.touchPointRaidus;
				}
				matrix = new Matrix();
				if(isInEditMode()) {
					return;
				}
				if((getWidth()-boundary*2)<image.getWidth() ||
						(getHeight()-boundary*2)<image.getHeight()){
					float scaleX = ((float)getWidth()-boundary*2)/image.getWidth();
					float scaleY = ((float)getHeight()-boundary*2)/image.getHeight();
					float scale = scaleX <scaleY?scaleX:scaleY;
					matrix.postScale(scale, scale);
				}else if(image.getWidth() < BASIC_WIDTH || image.getHeight() < BASIC_WIDTH){
					float scaleX = (float)BASIC_WIDTH/image.getWidth();
					float scaleY = (float)BASIC_WIDTH/image.getHeight();
					float scale = scaleX >scaleY?scaleX:scaleY;
					matrix.postScale(scale, scale);
				}
				image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
				imageLeft = (getWidth() - image.getWidth()) / 2;
				imageRight = (getWidth() + image.getWidth()) / 2;
				imageTop = (getHeight() - image.getHeight()) / 2;
				imageBottom = (getHeight() + image.getHeight()) / 2;
				touchGrid.vaildSize(imageLeft,imageTop,imageRight,imageBottom);
			}
			canvas.drawBitmap(image, imageLeft, imageTop, null);
			if(touchGrid == null){
				touchGrid = new TouchGrid();
			}
			else{
				Paint paint = new Paint();
				paint.setAlpha(0x7f);
				paint.setStyle(Style.FILL);
				
				canvas.drawRect(imageLeft, imageTop, touchGrid.getLeft(), touchGrid.getTop(), paint);
				canvas.drawRect(touchGrid.getLeft(), imageTop, touchGrid.getRight(), touchGrid.getTop(), paint);
				canvas.drawRect(touchGrid.getRight(), imageTop, imageRight, touchGrid.getTop(), paint);
				canvas.drawRect(imageLeft, touchGrid.getTop(), touchGrid.getLeft(), touchGrid.getBottom(), paint);
				canvas.drawRect(touchGrid.getRight(), touchGrid.getTop(), imageRight, touchGrid.getBottom(), paint);
				canvas.drawRect(imageLeft, touchGrid.getBottom(), touchGrid.getLeft(), imageBottom, paint);
				canvas.drawRect(touchGrid.getLeft(), touchGrid.getBottom(), touchGrid.getRight(), imageBottom, paint);
				canvas.drawRect(touchGrid.getRight(), touchGrid.getBottom(), imageRight, imageBottom, paint);
				touchGrid.drawGrid(canvas);
			}
			super.onDraw(canvas);
		}
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int what = event.getAction();
			//TODO
			NaviLogUtil.debugTouch( "CameraLayout onTouchEvent: " + what );
			NaviLogUtil.debugTouch( String.format( "Event x=%s , y=%s" , event.getX() , event.getY() ));
			switch (what){
			case MotionEvent.ACTION_DOWN:
				touchGrid.judgeTouchedPoint(event.getX(), event.getY());
				return true;
			case MotionEvent.ACTION_MOVE:
				touchGrid.moveToPosition(event.getX(), event.getY());
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				touchGrid.stopMove();
				break;
				default:
					break;
			}
			return super.onTouchEvent(event);
		}
	}
	public static final int BASIC_WIDTH = 116;
	protected class TouchGrid{
		private float left;
		private float top;
		private float right;
		private float bottom;
		
		private float limitLeft;
		private float limitTop;
		private float limitRight;
		private float limitBottom;
		
		private Bitmap touchPoint;
		private float touchPointRaidus;
		
		private Bitmap gridPic;
		private float gridPicWidth;
		private float gridPicHeight;
		private Matrix gridMatrix;
		
		private float downX;
		private float downY;
		
		private int pointMoveState;
		private static final int MOVE_NONE = 0;
		private static final int MOVE_LEFT_TOP_POINT = MOVE_NONE + 1;
		private static final int MOVE_RIGHT_BOTTOM_POINT = MOVE_LEFT_TOP_POINT + 1;
		private static final int MOVE_GRID = MOVE_RIGHT_BOTTOM_POINT + 1;
		
		public TouchGrid(){
			left = (cameraView.getWidth() - BASIC_WIDTH) / 2;
			right = left + BASIC_WIDTH;
			top = (cameraView.getHeight() - BASIC_WIDTH) / 2;
			bottom = top + BASIC_WIDTH;
			touchPoint = BitmapFactory.decodeResource(getResources(), R.drawable.navicloud_and_586a);
			touchPointRaidus = touchPoint.getWidth() / 2;
			
			gridPic = BitmapFactory.decodeResource(getResources(), R.drawable.navicloud_and_587a);
			gridPicWidth = gridPic.getWidth();
			gridPicHeight = gridPic.getHeight();
			setGridPicMatrix();
			pointMoveState = MOVE_NONE;
		}
		public void vaildSize(float imageLeft, float iamgeTop, float imageRight, float imageBottom) {
			limitLeft = imageLeft;
			limitTop = iamgeTop;
			limitRight = imageRight;
			limitBottom = imageBottom;
			left = (cameraView.getWidth() - BASIC_WIDTH) / 2;
			right = left + BASIC_WIDTH;
			top = (cameraView.getHeight() - BASIC_WIDTH) / 2;
			bottom = top + BASIC_WIDTH;
			this.left = MAX(this.left, imageLeft);
			this.top = MAX(this.top, iamgeTop);
			this.right = MIN(this.right, imageRight);
			this.bottom = MIN(this.bottom, imageBottom);
			
		}
		public void stopMove() {
			pointMoveState = MOVE_NONE;
			
		}
		public void moveToPosition(float x, float y) {
			float dX;
			float dY;
			dX = x - downX;
			dY = y - downY;
			float d = dX<dY?dX:dY;
			switch(pointMoveState){
			case MOVE_LEFT_TOP_POINT:
					downX += d;
					downY += d;
					left += d;
					top += d;
				break;
			case MOVE_RIGHT_BOTTOM_POINT:
					downX += d;
					downY += d;
					right += d;
					bottom += d;
				break;
			case MOVE_GRID:
					downX += dX;
					downY += dY;
					left += dX;
					right += dX;
					top += dY;
					bottom += dY;
				break;
			default:break;
			}
			checkRectVaild();
			
		}
		private void checkRectVaild() {
			float dX = 0;
			float dY = 0;
			float d = 0;
			switch(pointMoveState){
			case MOVE_LEFT_TOP_POINT:
				dX = left - limitLeft;
				dY = top - limitTop;
				dX = MIN(dX,0);
				dY = MIN(dY,0);
				d =MIN(dX,dY);
				downX -= dX;
				downY -= dY;
				left -= d;
				top -= d;
				
				dX = (right -left) - BASIC_WIDTH;
				dY = (bottom - top) - BASIC_WIDTH;
				dX = MIN(dX,0);
				dY = MIN(dY,0);
				d = MIN(dX,dY);
				downX += dX;
				downY += dY;
				left += d;
				top += d;
				break;
			case MOVE_RIGHT_BOTTOM_POINT:
				dX = right - limitRight ;
				dY = bottom - limitBottom;
				dX = MAX(dX,0);
				dY = MAX(dY,0);
				d =MAX(dX,dY);
				downX -= dX;
				downY -= dY;
				right -= d;
				bottom -= d;
				
				dX = (right-left) - BASIC_WIDTH ;
				dY = (bottom-top) - BASIC_WIDTH ;
				dX = MIN(dX,0);
				dY = MIN(dY,0);
				d = MIN(dX,dY);
				downX -= dX;
				downY -= dY;
				right -= d;
				bottom -= d;
				break;
			case MOVE_GRID:
				if(left<limitLeft){
					dX = limitLeft - left;
				}else if(right > limitRight){
					dX = limitRight - right;
				}
				left +=dX;
				right += dX;
				downX += dX;
				if(top<limitTop){
					dY = limitTop - top;
				}else if(bottom > limitBottom){
					dY = limitBottom - bottom;
				}
				downY += dY;
				top += dY;
				bottom += dY;
				break;
			default:break;
			}
			
		}
		private float MAX(float x, float y){
			return x>y?x:y;
		}
		private float MIN(float x, float y){
			return x<y?x:y;
		}
		public void judgeTouchedPoint(float x, float y) {
			downX = x;
			downY = y;
			if(Math.abs(left-x)<(touchPointRaidus*2) && Math.abs(top-y)<(touchPointRaidus*2)){
				pointMoveState = MOVE_LEFT_TOP_POINT;
			}else if(Math.abs(right-x)<(touchPointRaidus*2) && Math.abs(bottom-y)<(touchPointRaidus*2)){
				pointMoveState = MOVE_RIGHT_BOTTOM_POINT;
			}else if((x - left) > 0 && (right - x) > 0
					&&(y - top) > 0 && (bottom - y) > 0){			
				pointMoveState = MOVE_GRID;
			}
			
		}
		private void setGridPicMatrix() {
			if(gridMatrix == null){
				gridMatrix = new Matrix();
			}
			float sx = (float)(right - left)/gridPicWidth;
			float sy = (float)(bottom - top)/gridPicHeight;
			gridMatrix.postScale(sx, sy);
		}
		public void drawGrid (Canvas canvas) {
			canvas.drawBitmap(touchPoint, left-touchPointRaidus, top-touchPointRaidus, null);
			canvas.drawBitmap(touchPoint, right-touchPointRaidus, bottom-touchPointRaidus, null);
			
		}
		public float getLeft() {
			return left;
		}
		public float getTop() {
			return top;
		}
		public float getRight() {
			return right;
		}
		public float getBottom() {
			return bottom;
		}
	}
}


