package dwoz.waldo.extractor.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dwoz.common.dao.BaseResultBean;

public class WaldoContentBean extends BaseResultBean<WaldoContentBean> {
	
	private static final long serialVersionUID = 5114306446923801329L;
	private String ETAG;
	private String KEY;
	private Timestamp MODDATE;
	private Long SIZE;
	private String STORAGECLASS;
	private Long ID;
	private List<ExifBean> EXIF;
	
	
	
	public String getETAG() {
		return ETAG;
	}
	public void setETAG(String eTAG) {
		ETAG = eTAG;
	}
	public String getKEY() {
		return KEY;
	}
	public void setKEY(String kEY) {
		KEY = kEY;
	}
	public Timestamp getMODDATE() {
		return MODDATE;
	}
	public void setMODDATE(Timestamp mODDATE) {
		MODDATE = mODDATE;
	}
	public Long getSIZE() {
		return SIZE;
	}
	public void setSIZE(Long sIZE) {
		SIZE = sIZE;
	}
	public String getSTORAGECLASS() {
		return STORAGECLASS;
	}
	public void setSTORAGECLASS(String sTORAGECLASS) {
		STORAGECLASS = sTORAGECLASS;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public List<ExifBean> getEXIF() {
		if(this.EXIF == null){
			this.EXIF = new ArrayList<>();
		}
		return EXIF;
	}
}
