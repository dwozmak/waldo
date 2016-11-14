package dwoz.waldo.extractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dwoz.waldo.extractor.dao.ExifBean;
import dwoz.waldo.extractor.dao.ExifDAO;
import dwoz.waldo.extractor.dao.WaldoContentBean;

public class ExifExtractor {
	ExifDAO dao;
  	private Logger logger = LoggerFactory.getLogger(ExifExtractor.class);
	
	public ExifExtractor(ExifDAO dao) {
		//we're reusing this dao because we don't want to proliferate connection pools
		this.dao = dao;
	}
	
	/*
	 * we're using apache commons-image package to peel back the sardine can...
	 */
	public List<ExifBean> parseJPGExif(String manifestURI, WaldoContentBean contents){
		
		List<ExifBean> retList = new ArrayList<>();
		URL url = null;
		try {
			url = new URL(manifestURI + contents.getKEY());
		} catch (MalformedURLException e1) {
			logger.error("BAD URL: " + e1.getLocalizedMessage());
		}
		ImageMetadata metadata;
		try {
			metadata = Imaging.getMetadata(url.openStream(), url.toString());
			
			if (metadata instanceof JpegImageMetadata) {
	            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	            //we have exif data to extract...
				ExifTagConstants.ALL_EXIF_TAGS
				.stream()
				.filter(field -> jpegMetadata.findEXIFValue(field) != null)//don't bother with null values
				.forEach(
						(field) -> {
					        TiffField thisField = jpegMetadata.findEXIFValue(field);
					            logger.debug(thisField.getTagName() + ": "
					                    + thisField.getValueDescription());
					            ExifBean bean = new ExifBean();
					            bean.setKEY(thisField.getTagName());
					            bean.setVALUE(thisField.getValueDescription());
					            bean.setCONTENTKEYID(contents.getID());
					            try {
									retList.add(dao.insertExifTag(bean));
								} catch (Exception e) {
									logger.error(e.getLocalizedMessage());
								}							
						});
				//we also like the tiff data...
				TiffTagConstants.ALL_TIFF_TAGS
				.stream()
				.filter(field -> jpegMetadata.findEXIFValue(field) != null)
				.forEach(
						(field) -> {
					        TiffField thisField = jpegMetadata.findEXIFValue(field);
					            logger.debug(thisField.getTagName() + ": "
					                    + thisField.getValueDescription());
					            ExifBean bean = new ExifBean();
					            bean.setKEY(thisField.getTagName());
					            bean.setVALUE(thisField.getValueDescription());
					            bean.setCONTENTKEYID(contents.getID());
					            try {
									retList.add(dao.insertExifTag(bean));
								} catch (Exception e) {
									logger.error(e.getLocalizedMessage());
								}

						});
			}
			
		} catch (ImageReadException e) {
			logger.error("Image read exception: " + e.getLocalizedMessage());
		} catch (IOException e) {
			logger.error("IO exception: " + e.getLocalizedMessage());
		}
		return retList;
	}
}
