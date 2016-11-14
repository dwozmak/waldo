package dwoz.waldo.extractor.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.s3.doc._2006_03_01.ListBucketResult;
import com.dwoz.common.dao.BaseDAO;
import com.dwoz.common.exception.DataAccessException;
import com.dwoz.common.util.DBUtil;
import com.dwoz.common.util.NextIdHelper;

/*
 * this DataAccessObject (dao) uses a bunch of pre-existing helper classes/methods in
 * package com.dwoz.common that I've written previously.  it uses basic jdbc to connect
 * to the database.  Hibernate or myBatis or some ORM framework could be implemented instead
 * of jdbc...but this programming example test is time-boxed!
 * 
 */
public class ExifDAO extends BaseDAO{
  	private Logger logger = LoggerFactory.getLogger(ExifDAO.class);

	public ExifDAO() {
	}
	
	public WaldoContentBean insertContents(ListBucketResult.Contents contents)
			throws DataAccessException {
        PreparedStatement cmd = null;
        WaldoContentBean bean = new WaldoContentBean();
        bean.setID(NextIdHelper.nextId("WALDO.SEQ_MANIFESTCONTENT"));
        try{
        	//(ID,KEY,ETAG,MODDATE,SIZE,STORAGECLASS)
        	cmd = this.getPreparedSQLStatement(DBUtil.getQueryByKey("INSERTMANIFESTCONTENT"));
	        cmd.setObject(1,bean.getID(), Types.BIGINT);
	        cmd.setObject(2,contents.getKey(), Types.VARCHAR);
	        cmd.setObject(3,contents.getETag(), Types.VARCHAR);
	        cmd.setObject(4,new Timestamp(contents.getLastModified().toGregorianCalendar().getTimeInMillis()), Types.TIMESTAMP);
	        cmd.setObject(5,contents.getSize(), Types.BIGINT);
	        cmd.setObject(6,contents.getStorageClass(), Types.VARCHAR);
		} catch (Exception e){
			logger.error("error geting insertmanifestcontent command");
		}
		
		this.runPreparedSQLInsertStatement(cmd);
		
		return this.getContentsById(bean.getID());
	}
		
	public List<WaldoContentBean> getContents() throws DataAccessException {
		
		PreparedStatement cmd = null;
		try {
			cmd = this.getPreparedSQLStatement(DBUtil.getQueryByKey("getMANIFESTCONTENT"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<WaldoContentBean> qbs = this.runPreparedSQLStatement(cmd, WaldoContentBean.class);
		return qbs.isEmpty() ? null : qbs;
		
	}
	public WaldoContentBean getContentsById(Long id) throws DataAccessException {
		
		PreparedStatement cmd = null;
		try {
			cmd = this.getPreparedSQLStatement(DBUtil.getQueryByKey("getMANIFESTCONTENTBYID"));
			cmd.setObject(1, id, Types.BIGINT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<WaldoContentBean> qbs = this.runPreparedSQLStatement(cmd, WaldoContentBean.class);
		return qbs.isEmpty() ? null : qbs.iterator().next();
		
	}
	
	
	public ExifBean insertExifTag(ExifBean exif)throws DataAccessException {
        PreparedStatement cmd = null;
        try{
        	//(ID,CONTENTKEYID,KEY,VALUE)
        	cmd = this.getPreparedSQLStatement(DBUtil.getQueryByKey("INSERTEXIFDATA"));
	        cmd.setObject(1,NextIdHelper.nextId("WALDO.SEQ_EXIFDATA"), Types.BIGINT);
	        cmd.setObject(2,exif.getCONTENTKEYID(), Types.BIGINT);
	        cmd.setObject(3,exif.getKEY(), Types.VARCHAR);
	        cmd.setObject(4,exif.getVALUE(), Types.VARCHAR);
		} catch (Exception e){
			logger.error("error geting insertEXIFDATA command");
		}
		
		this.runPreparedSQLInsertStatement(cmd);
		
		return exif;
	}

	public List<ExifBean> getExifTags(String contentKey){
		
		PreparedStatement cmd = null;
		try {
			cmd = this.getPreparedSQLStatement(DBUtil.getQueryByKey("getEXIFDATABYCONTENTKEY"));
			cmd.setObject(1, contentKey, Types.VARCHAR);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<ExifBean> qbs = this.runPreparedSQLStatement(cmd, ExifBean.class);
		return qbs.isEmpty() ? new ArrayList<ExifBean>() : qbs;
	}
}
