package dwoz.waldo.extractor.dao;

import com.dwoz.common.dao.BaseResultBean;

/*
 * this pojo fields are named specifically to match the sql query return fields.
 * BaseResultBean has processing methods to load resultset data into the pojo.
 * 
 */
public class ExifBean extends BaseResultBean<ExifBean> {
	
	
	private static final long serialVersionUID = -2964262772635130675L;
	
	private String KEY;
	private String VALUE;
	private Long CONTENTKEYID;

	public String getKEY() {
		return KEY;
	}

	public void setKEY(String kEY) {
		KEY = kEY;
	}

	public String getVALUE() {
		return VALUE;
	}

	public void setVALUE(String vALUE) {
		VALUE = vALUE;
	}

	public Long getCONTENTKEYID() {
		return CONTENTKEYID;
	}

	public void setCONTENTKEYID(Long cONTENTSKEY) {
		CONTENTKEYID = cONTENTSKEY;
	}

	public ExifBean() {
	}

}
