package dwoz.waldo.logic;

import java.util.List;
import com.dwoz.common.exception.DataAccessException;

import dwoz.waldo.extractor.dao.ExifDAO;
import dwoz.waldo.extractor.dao.WaldoContentBean;

public class ContentsLogic {
	
	ExifDAO dao;
	
	public ContentsLogic() {
		try {
			dao = new ExifDAO();
			dao.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public List<WaldoContentBean> getContents(){
		
		List<WaldoContentBean> results = null;
		try {
			results = dao.getContents();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(WaldoContentBean bean: results){
			
				bean.getEXIF().addAll(
					dao.getExifTags(bean.getKEY())
				);
			}
		return results;
	}

}
