package com.billionav.navi.naviscreen.mef;

import android.util.Log;
import android.view.MotionEvent;
import java.util.*;

import com.billionav.navi.naviscreen.mef.MultiTouchController.PointInfo;
import com.billionav.navi.naviscreen.mef.MultiTouchController.PositionAndScale;

public class GestureReg implements MultiTouchController.MultiTouchObjectCanvas{
	public static final int GESTURE_UNKNOWN = 0;
	public static final int GESTURE_ZOOM_ROTATE = 1;
	public static final int GESTURE_2P_MOVE = 2;
	
	private MultiTouchController m_MultiTouchControl = null;
	private final PositionAndScale mCurrXform = new PositionAndScale();
	
	private float m_LastOffsetX = 0;
	private float m_LastOffsetY = 0;
	private float m_moveOffsetX = 0;
	private float m_moveOffsetY = 0;
	
	private float m_ZoomSize = 1;
	private float m_lastZoomSize = 1;
	
	private float m_Angle = 0;
	private float m_LastAngle = 0;
	
	private boolean m_bIsMultiTouch = false;

	private float m_2pMoveOffset = 0;
	private int m_iGesReg = GESTURE_UNKNOWN;
	
	public GestureReg(int screenWidth, int screenHeight)
	{
		m_MultiTouchControl = new MultiTouchController(this, screenWidth, screenHeight);
	}
	
	public void SetScreenSize(int iScreenWidth, int iScreenHeight)
	{
		m_MultiTouchControl.SetScreenSize(iScreenWidth, iScreenHeight);
	}
	
	public void getPositionAndScale(PositionAndScale objPosAndScaleOut)
	{
		objPosAndScaleOut.set(0,
								0,
								true,
								m_ZoomSize,
								false,
								0,
								0,
								true,
								0
								);
	}
	
	public boolean setPositionAndScale(PositionAndScale newObjPosAndScale, PointInfo curTouchPoint, PointInfo prevTouchPointInfo)
	{	
		m_bIsMultiTouch = curTouchPoint.isMultiTouch();
		m_iGesReg = GESTURE_ZOOM_ROTATE;
		m_ZoomSize = newObjPosAndScale.getScale();
		float fAngle = newObjPosAndScale.getAngle(); 
		m_Angle = fAngle - m_LastAngle;
		m_LastAngle = fAngle;
		
		return true;
	}
	
	public boolean set2pMove(float rate)
	{
		m_bIsMultiTouch = true;
		m_iGesReg = GESTURE_2P_MOVE;
		m_2pMoveOffset = rate;

		return true;
	}
	
	public void finishOnceOpe()
	{
		mCurrXform.set(0,
				0,
				false,
				0,
				false,
				0,
				0,
				false,
				0
				);
		m_lastZoomSize = 1;
		m_ZoomSize = 1;
		m_moveOffsetX = 0;
		m_moveOffsetY = 0;
		m_LastOffsetX = 0;
		m_LastOffsetY = 0;
		m_2pMoveOffset = 0;
		m_iGesReg = GESTURE_UNKNOWN;
		m_Angle = 0;
		m_LastAngle = 0;
	}
	
	public void AddMotionEvent(MotionEvent event)
	{
		m_MultiTouchControl.onTouchEvent(event);
	}
	
	public float GetMoveOffSetX()
	{
		float resultOffsetX = 0;
		if (Math.abs(m_LastOffsetX - m_moveOffsetX) > 2)
		{
			resultOffsetX = m_moveOffsetX - m_LastOffsetX;
			m_LastOffsetX = m_moveOffsetX;
		}
		return resultOffsetX;
	}
	
	public float GetMoveOffSetY()
	{
		float resultOffsetY = 0;
		if (Math.abs(m_LastOffsetY - m_moveOffsetY) > 2)
		{
			resultOffsetY = m_moveOffsetY - m_LastOffsetY;
			m_LastOffsetY = m_moveOffsetY;
		}
		return resultOffsetY;
	}
	
	public float GetRotateAngle()
	{
		return m_Angle;
	}
	
	public boolean isMultiTouch()
	{
		return m_bIsMultiTouch;
	}
	
	public float GetZoomSize()
	{
		float resultZoomSize = 1;
		
		if (m_ZoomSize < m_lastZoomSize)
		{
			resultZoomSize = m_lastZoomSize - m_ZoomSize;
		}
		else if (m_ZoomSize > m_lastZoomSize)
		{
			resultZoomSize = 1 + (m_ZoomSize - m_lastZoomSize);
		}
		
		m_lastZoomSize = m_ZoomSize;
		
		if (resultZoomSize > 1.4)
		{
			resultZoomSize = (float)1.4;
		}
		
		return resultZoomSize;
	}
	
	public float Get2PMoveOffset()
	{
		return m_2pMoveOffset;
	}

	public boolean is2PMoveGes()
	{
		return (GESTURE_2P_MOVE==m_iGesReg);
	}
	
	public boolean is2PScaleRotate()
	{
		return (GESTURE_ZOOM_ROTATE==m_iGesReg);
	}
}
