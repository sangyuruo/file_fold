package com.billionav.jni;

import java.util.UUID;

public class UIPOIMemoryData {
	
	private int m_dataid;
	
	/**
	 * Setter method of POI Data id
	 *
	 * @param	int iPOIDataID:�n�_���ID[IN]
	 * @return	void
	 */
	public void SetDataID(int iPOIDataID) {
		m_dataid = iPOIDataID;
	}
	
	/**
	 * Getter method of POI Data id
	 *
	 * @return	data id
	 */
	public int GetDataID() {
		return m_dataid;
	}
	
	
	/**
	 * Initialize the specific POI memory data
	 * @return	void
	 */
	public native void Initialize();
	

	/**
	 * PNTDM_DataUnit���̎���
	 *
	 * @param   PNTDM_DataUnit data:�o�^�n�f�[�^[IN]
	 * @return  void
	 */
	//public native void Assign(PNTDM_DataUnit data);


	/**
	 * UIC_PT_PathPoint���̎���
	 *
	 * @param   const UIC_PT_PathPointBase* data:�T���f�[�^[IN]
	 * @return  void
	 */
	//public native void Assign(UIC_PT_PathPointBase data);

	
//@}


///@name �擾�֐�
///���ڂ̎擾�֐��Q
//@{
    /**
     * �\�����S�o�x�擾
     *
	 * @param �Ȃ�
	 * @return �\�����S�o�x
	**/
	public native long GetCenterLon();

    /**
     * �\�����S�ܓx�擾
     *
	 * @param �Ȃ�
	 * @return �\�����S�ܓx
	**/
	public native long GetCenterLat();
	
	/**
     * �\�����̎擾
	 *
	 * @param   �Ȃ�
	 * @return  String & :����
	 * @attention �ߖT���̂ł����Ă��A"�t��"�͕t���܂���B
	**/
	public native String GetDispName();



	/**
     * �Z�����̎擾
	 *
	 * @param   �Ȃ�
	 * @return  String & :����
	 */
	public native String GetAddress();

	/**
     * �d�b�ԍ��擾
	 *
	 * @param   �Ȃ�
	 * @return  String &: �d�b�ԍ�
	 */
	public native String GetTel();


	/**
     * ���F��ʃR�[�h
	 *
	 * @param	�Ȃ�
	 * @return 	DWORD	���F��ʃR�[�h
	 * @attention �������f����p
	 */
//	virtual const DWORD GetVoiceSymbol( VOID ) const ;

	/**
	 * �V���b�^�[�}�[�N�擾
	 *
	 * @return  NP_BOOL	bShutterInfo:�V���b�^�[���
	 * @note	NP_TRUE:�c�ƒ� NP_FALSE:�X��
	 * @attention �������f����p
	 */
//	virtual const NP_BOOL GetShutterInfo( VOID ) const ;


	/**
	 * �C���[�W���擾
	 *
	 * @param   AL_String *	alFilePath:�t�@�C���p�X��[OUT]
	 * @param   DWORD		*dwOffset:�I�t�Z�b�g[OUT]
	 * @param   DWORD		*dwSize:�T�C�Y[OUT]
	 * @return  �Ȃ�
	*/
	//virtual VOID GetImageInfo(AL_String *alFilePath, DWORD *pdwOffset, DWORD *pdwSize) const;

	/**
     * �n�}��ʎ�ʎ擾
	 *
	 * @param	�Ȃ�
	 * @return 	 const MAP_eDirKind: �n�}��ʎ��
	 */
	//virtual const MAP_eDirKind GetDirKind( VOID ) const;

    /**
     * �\�����S�ܓx�o�x�ݒ�
     *
     * �\�����S�ܓx�o�x��ݒ肵�܂��B
	 *
	 * @param const AL_LonLat & rcLonLat[IN]
	 * @return �Ȃ�
	**/
	public void SetCenterPos(long lng, long lat){
		SetCenterPos((int)lng,(int)lat);
	}
	public native void SetCenterPos(int lng, int lat);

	/**
     * �\�����̐ݒ�
	 *
	 * @param   const AL_String & rcDispName:����[IN]
	 * @return  �Ȃ�
	 */
	public native void SetDispName(String name);


	/**
	 * �Z�����̐ݒ�
	 *
	 * @param   String & rcAddress:�Z������[IN]
	 * @return  �Ȃ�
	 */
	public native void SetAddress(String address);

	/**
     * �d�b�ԍ��ݒ�
	 *
	 * @param   const AL_String & rcTel:�d�b�ԍ�[IN]
	 * @return  �Ȃ�
	 */
	public native void SetTel(String tel);



	/**
	 * ���F��ʃR�[�h�ݒ�
	 *
	 * @param   DWORD dwSymbol:���F��ʃR�[�h[IN]
	 * @return  �Ȃ�
	 * @attention �������f����p
	 */
//	virtual VOID SetVoiceSymbol( DWORD dwSymbol );

	/**
	 * �V���b�^�[�}�[�N�ݒ�
	 *
	 * @param   NP_BOOL bShutterInfo:�V���b�^�[���[IN]
	 * @note	NP_TRUE:�c�ƒ� NP_FALSE:�X��
	 * @return  �Ȃ�
	 * @attention �������f����p
	 */
//	virtual VOID SetShutterInfo( NP_BOOL bShutterInfo );

	/**
	 * �C���[�W���ݒ�
	 *
	 * @param   const AL_String & cFilePath:�t�@�C���p�X��[IN]
	 * @param   const DWORD dwOffset:�I�t�Z�b�g[IN]
	 * @param   const DWORD dwSize:�T�C�Y[IN]
	 * @return  �Ȃ�
	*/
	//virtual VOID SetImageInfo( const AL_String & cFilePath, const DWORD dwOffset, const DWORD dwSize );

	/**
	 * �T�C�h���ݒ�
	 *
	 * @param   const BYTE bSideInfo:�T�C�h���[IN]
	 * @return  �Ȃ�
	 * @attention �C�O���f����p
	 */
	public native void SetPathSideInfo(byte info);

	/**
	 * �����N�h�c���ݒ�
	 *
	 * @param   const LONG lLinkID:�����NID[IN]
	 * @return  �Ȃ�
	 * @attention �C�O���f����p
	 */
	public native void SetPathLinkID(long id);

	

	/**
     * �n�}��}�[�N�\���t���O�ݒ�
	 *
	 *
	 * @param	NP_TRUE:�\������ NP_FALSE:�\�����Ȃ�
	 * @return  �Ȃ�
	*/
	private native void SetUUID(String uuid);
	public void CreateUUID(){
		String uuid = UUID.randomUUID().toString(); 
		SetUUID(uuid);
	}

}
