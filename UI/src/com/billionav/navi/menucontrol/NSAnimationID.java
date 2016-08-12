package com.billionav.navi.menucontrol;

public class NSAnimationID {
	public static final int ANIMATION_ID_NONE = -1;
	public static final int ANIMATION_ID_DEFAULT = 0;
	
	public static final int ANIMATION_ID_SLIDE_IN_LEFT  = 1;    //Slide_in_form_left & Slide_out_to_right
	public static final int ANIMATION_ID_SLIDE_IN_RIGHT = 2;    //Slide_in_from right & Slide_out_to_left
	public static final int ANIMATION_ID_SLIDE_IN_BOTTOM  = 3;  //Slide_in_from_Bottom
	public static final int ANIMATION_ID_SLIDE_IN_BOTTOM_DELAYED  = 4;  //Slide_in_from_Bottom with delayed 400ms
	public static final int ANIMATION_ID_OUT_DELAYED  = 5;  //out delayed 400ms
	public static final int ANIMATION_ID_SLIDE_OUT_BOTTOM = 6;  //Slide_out_from_Bottom
	public static final int ANIMATION_ID_ALPHA_TRANSPARANT = 7; //alpha from 255-0 & alpha from 0-255

	
	public static final int Menu2Menu_Slide_L2R = 1;   //Slide_in_form_left & Slide_out_to_right
	public static final int Menu2Menu_Slide_R2L = 2;   //Slide_in_from right & Slide_out_to_left
	public static final int Map2Menu_Slide_B2T  = 3;   //Slide_in_from_Bottom 
	public static final int Menu2Map_Slide_T2B  = 4;   //Slide_out_from_Bottom
}
