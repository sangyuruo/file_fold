package com.billionav.navi.naviscreen.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.uicommon.UIC_SystemCommon;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;

public class ADT_Setting_HUD_LogoMark extends PrefenrenceActivityBase{
	
	private CheckBoxPreference HUDPetrol = null;
	private CheckBoxPreference HUDCarPark = null;
	private CheckBoxPreference HUDHotel = null;
	private CheckBoxPreference HUDBank = null;
	private CheckBoxPreference HUDStation = null;
	private CheckBoxPreference HUDPostOffice = null;
	private CheckBoxPreference HUDRestaurant = null;
	private CheckBoxPreference HUDBurgers = null;
	private CheckBoxPreference HUDCoffee = null;
	private CheckBoxPreference HUDHospital = null;
	private CheckBoxPreference HUDPharmacy = null;
	private CheckBoxPreference HUDShopping = null;
	private CheckBoxPreference HUDClothing = null;
	private CheckBoxPreference HUDConsumer = null;
	private CheckBoxPreference HUDHomeSpcialty = null;
	private CheckBoxPreference HUDEntertainment = null;
	private CheckBoxPreference HUDFamilyRestaurant = null;
	
	private int hasCheckedItemNum = 0;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_setting_hud_logomark);
		setTitle("LogoMark Setting");
		findViews();
		setItemNum();
		setListener();
//		HUDPetrol.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				
//				SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK1, 0x0000);
//				return false;
//			}
//		});	
//		int i1=SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK1);
//		CheckBoxPreference c1= hudLogomark.get(i1);
//		c1.setChecked(true);
	}

	private void setListener() {
		HUDPetrol.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0000));
		HUDCarPark.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0001));
		HUDHotel.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0002));
		HUDBank.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0003));
		HUDStation.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0004));
		HUDPostOffice.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0005));
		HUDRestaurant.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0006));
		HUDBurgers.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0007));
		HUDCoffee.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0008));
		HUDHospital.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0009));
		HUDPharmacy.setOnPreferenceChangeListener(new PreferenceChangeListener(0x000A));
		HUDShopping.setOnPreferenceChangeListener(new PreferenceChangeListener(0x000B));
		HUDClothing.setOnPreferenceChangeListener(new PreferenceChangeListener(0x000C));
		HUDConsumer.setOnPreferenceChangeListener(new PreferenceChangeListener(0x000D));
		HUDHomeSpcialty.setOnPreferenceChangeListener(new PreferenceChangeListener(0x000E));
		HUDEntertainment.setOnPreferenceChangeListener(new PreferenceChangeListener(0x000F));
		HUDFamilyRestaurant.setOnPreferenceChangeListener(new PreferenceChangeListener(0x0010));
	}
	
	class PreferenceChangeListener implements OnPreferenceChangeListener{
		int id = 0x0000;
		PreferenceChangeListener(int id){
			this.id = id;
		}
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if((Boolean)newValue){
				if(hasCheckedItemNum>=5){
					showMoreNumDialog();
					return false;
				}
				hasCheckedItemNum++;
				switch (hasCheckedItemNum) {
				case 1:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK1, this.id);
					break;
				case 2:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK2, this.id);
					break;
				case 3:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK3, this.id);
					break;
				case 4:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK4, this.id);
					break;
				case 5:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK5, this.id);
					break;
				}
			}else{
				switch (hasCheckedItemNum) {
				case 1:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK1, -1);
					break;
				case 2:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK2, -1);
					break;
				case 3:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK3, -1);
					break;
				case 4:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK4, -1);
					break;
				case 5:
					SharedPreferenceData.setValue(SharedPreferenceData.HUD_LOGOMARK5, -1);
					break;
				}
				hasCheckedItemNum--;
			}
			CheckBoxPreference p = (CheckBoxPreference) preference;
			p.setChecked((Boolean)newValue);
			UIC_SystemCommon.setHUD();
			return false;
		}
		
	}
	
	private void showMoreNumDialog() {
		CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());
		dialog.setTitle(R.string.STR_MM_06_02_02_59);
		dialog.setMessage(R.string.STR_MM_06_02_02_60);

		dialog.setPositiveButton(R.string.STR_MM_09_01_02_02, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void setItemNum() {
		hasCheckedItemNum = 0;
		if(SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK1)!=-1)
			hasCheckedItemNum++;
		if(SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK2)!=-1)
			hasCheckedItemNum++;
		if(SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK3)!=-1)
			hasCheckedItemNum++;
		if(SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK4)!=-1)
			hasCheckedItemNum++;
		if(SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK5)!=-1)
			hasCheckedItemNum++;
		if (hasCheckedItemNum == 0) {
			HUDPetrol.setChecked(false);
			HUDCarPark.setChecked(false);
			HUDHotel.setChecked(false);
			HUDBank.setChecked(false);
			HUDStation.setChecked(false);
			HUDPostOffice.setChecked(false);
			HUDRestaurant.setChecked(false);
			HUDBurgers.setChecked(false);
			HUDCoffee.setChecked(false);
			HUDHospital.setChecked(false);
			HUDPharmacy.setChecked(false);
			HUDShopping.setChecked(false);
			HUDClothing.setChecked(false);
			HUDConsumer.setChecked(false);
			HUDHomeSpcialty.setChecked(false);
			HUDEntertainment.setChecked(false);
			HUDFamilyRestaurant.setChecked(false);
		}
	}

	private void findViews() {
		HUDPetrol = (CheckBoxPreference)getPreferenceManager().findPreference("HUDPetrol");
		HUDCarPark = (CheckBoxPreference)getPreferenceManager().findPreference("HUDCarPark");
		HUDHotel = (CheckBoxPreference)getPreferenceManager().findPreference("HUDHotel");
		HUDBank = (CheckBoxPreference)getPreferenceManager().findPreference("HUDBank");
		HUDStation = (CheckBoxPreference)getPreferenceManager().findPreference("HUDStation");
		HUDPostOffice = (CheckBoxPreference)getPreferenceManager().findPreference("HUDPostOffice");
		HUDRestaurant = (CheckBoxPreference)getPreferenceManager().findPreference("HUDRestaurant");
		HUDBurgers = (CheckBoxPreference)getPreferenceManager().findPreference("HUDBurgers");
		HUDCoffee = (CheckBoxPreference)getPreferenceManager().findPreference("HUDCoffee");
		HUDHospital = (CheckBoxPreference)getPreferenceManager().findPreference("HUDHospital");
		HUDPharmacy = (CheckBoxPreference)getPreferenceManager().findPreference("HUDPharmacy");
		HUDShopping = (CheckBoxPreference)getPreferenceManager().findPreference("HUDShopping");
		HUDClothing = (CheckBoxPreference)getPreferenceManager().findPreference("HUDClothing");
		HUDConsumer = (CheckBoxPreference)getPreferenceManager().findPreference("HUDConsumer");
		HUDHomeSpcialty = (CheckBoxPreference)getPreferenceManager().findPreference("HUDHomeSpcialty");
		HUDEntertainment = (CheckBoxPreference)getPreferenceManager().findPreference("HUDEntertainment");
		HUDFamilyRestaurant = (CheckBoxPreference)getPreferenceManager().findPreference("HUDFamilyRestaurant");
	}
	
	

}
