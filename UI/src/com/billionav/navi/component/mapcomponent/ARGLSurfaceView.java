package com.billionav.navi.component.mapcomponent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.billionav.DRIR.jni.jniDRIR_CameraViewCtrl;
import com.billionav.DRIR.jni.jniDRIR_DispControl;
import com.billionav.DRIR.HWInfo.HWinfo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;


public class ARGLSurfaceView extends GLSurfaceView {

	private IR3DRenderer m_Render;
	
	public ARGLSurfaceView(Context context) {
		super(context);
		initialize();
	}

	public ARGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	private void initialize() {
		if(isInEditMode()){
			return;
		}
		
		super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		m_Render = new IR3DRenderer();
		setRenderer(m_Render); 
		//Set render mode
		setRenderMode(RENDERMODE_WHEN_DIRTY); 
		
		//Set Z order
		setZOrderMediaOverlay(true);
		
	}
	
	public void RequestDrawing() {
		requestRender(); 
	}

}

class IR3DRenderer implements GLSurfaceView.Renderer {
	private static final float fVideoWidth = 640.0f;
	private static final float fVideoHeight = 480.0f;	
	
	
	 public void onDrawFrame(GL10 gl) {     
		 //Red color, for testing
		 gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		 gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		 //Draw GL Drawing 
		 jniDRIR_DispControl.DrawARView();
		 jniDRIR_CameraViewCtrl.setExposure();
	 }

	 //@Override
	 public void onSurfaceChanged(GL10 gl, int width, int height) {  
  	        
		 gl.glViewport(0, 0, width, height); 
		 gl.glMatrixMode(GL10.GL_PROJECTION);    
		 gl.glLoadIdentity();  
		 gl.glOrthof(0, width, 0, height, 0.1f, 500.0f);
		 gl.glMatrixMode(GL10.GL_MODELVIEW); 
		 gl.glLoadIdentity();   
		 gl.glScalef(width/fVideoWidth, height/fVideoHeight, 1.0f);
		 GLU.gluLookAt( gl,
				 0.0f, 0.0f, 100.0f, 
				 0.0f, 0.0f, 0.0f, 
				 0.0f, 1.0f, 0.0f);
		 HWinfo.SetDisplayMetrics(height, width);
	}
	 
	 public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		 // TODO Auto-generated method stub
		 // Initialize OpenGL states
         gl.glDisable(GL10.GL_DITHER);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glDisable(GL10.GL_DEPTH_TEST);

	 }
		

	
}


