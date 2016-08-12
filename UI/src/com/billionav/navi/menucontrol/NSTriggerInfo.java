package com.billionav.navi.menucontrol;
public class NSTriggerInfo extends Object {
	public int  	m_iTriggerID;
	public long 	m_lParam1;
	public long 	m_lParam2;
	public long 	m_lParam3;
	public long 	m_lParam4;
	public String 	m_String1  = "";
	public boolean  m_bDelayed = false;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "NSTriggerInfo["+m_iTriggerID+"]["
							   +m_lParam1+","
							   +m_lParam2+","
							   +m_lParam3+","
							   +m_lParam4+"][Delay:"+m_bDelayed+"]";
	}
	
	public void SetTriggerID(int iTriggerID){m_iTriggerID = iTriggerID;};	
	public void SetlParam1(long lParam1)	{m_lParam1 = lParam1;	};
	public void SetlParam2(long lParam2)	{m_lParam2 = lParam2;	};
	public void SetlParam3(long lParam3)	{m_lParam3 = lParam3;	};
	public void SetlParam4(long lParam4)	{m_lParam4 = lParam4;	};
	public void SetString1(String string1)	{m_String1 = string1;	};

	public int  GetTriggerID()	{ return m_iTriggerID;	};
	public long GetlParam1()	{ return m_lParam1;		};
	public long GetlParam2()	{ return m_lParam2;		};
	public long GetlParam3()	{ return m_lParam3;		};
	public long GetlParam4()	{ return m_lParam4;		};
	public String GetString1()	{ return m_String1;		};
}
