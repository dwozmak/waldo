package dwoz.waldo.extractor;

import dwoz.waldo.extractor.dao.ExifDAO;
import dwoz.waldo.extractor.dao.WaldoContentBean;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.amazonaws.s3.doc._2006_03_01.ListBucketResult;
import com.dwoz.common.util.DTDResolver;
import com.dwoz.common.util.XmlUtil;

public class ManifestProcessor {
  	private Logger logger = LoggerFactory.getLogger(ManifestProcessor.class);
    private Schema schema;
    private Validator validator;  
    private ExifDAO dao;

	public ManifestProcessor() {
		//initialize the dao
		if(this.dao == null){
			this.dao = new ExifDAO();
			try {
				this.dao.init();
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		//initialize the schema validation
		try {
			this.init();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
    private void init() throws Exception {
        try {
            
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            this.schema = schemaFactory.newSchema(new DOMSource[] { 
                new DOMSource(XmlUtil.loadXMLDescriptor(
                		ManifestProcessor.class.getResource("/resources/xsd/waldoManifestSchema.xsd"))) 
            });
            this.validator = this.schema.newValidator();
        } catch (SAXException e) {
        	logger.error("something extremely bad just happened setting up the validator schema");
            throw new RuntimeException(e);
        }
    }

	
	public  List<ListBucketResult> processManifest(String manifestURI){
	      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	
	      docBuilderFactory.setNamespaceAware(true);
	      
	      try{
		      URL url = new URL(manifestURI);
	    	  
		      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		      Document doc = null;
		      try {
		          docBuilder.setEntityResolver(new DTDResolver());
		          doc = docBuilder.parse(url.openStream());
		          logger.warn(manifestURI);
		          logger.warn(XmlUtil.serialize(doc));
		      } catch (SAXException e) {
		          logger.error("SAXException: '" + manifestURI + "'", e);
		      } catch (IOException io) {
		          logger.error("IOException: '" + manifestURI + "'", io);
		      }
		      if( doc == null )
		      	logger.error("no manifest found at: " + manifestURI);
		      else
		      {               
		          try {
		              return parseManifest(manifestURI, doc);
		          } catch (Exception e) {
		              logger.error(e.getLocalizedMessage());
		          }
		      }
		} catch (ParserConfigurationException | MalformedURLException e1) {
		    logger.error(e1.getLocalizedMessage());
		}
	    return null;

	  }		
	
	  public List<ListBucketResult> parseManifest( String manifestURI, Node xmlDoc) {
		  List<ListBucketResult> retList = new ArrayList<>();
		  
	        // validate the XML
	        try {
	        	//a StreamSource is used here for validation because of a bug with DomSource
	            StreamSource stream = new StreamSource(
	            		new StringReader(XmlUtil.serialize(xmlDoc)));
	            validator.validate(stream);
	            logger.warn("xml.validation.success: " + manifestURI );
	        } catch (SAXException | IOException e) {
	            logger.error("xml.validation.error:" + manifestURI + e.getLocalizedMessage());
	        }

	        try {
	            //create our unmarshaller
	            Unmarshaller unmarshaller = 
	            		JAXBContext.newInstance("com.amazonaws.s3.doc._2006_03_01")
	            		.createUnmarshaller();
	            unmarshaller.setSchema(schema);
	            
	            //populate our java object representing the xml
	            ListBucketResult manifest = (ListBucketResult) unmarshaller.unmarshal(xmlDoc);
	            
	            //initialize the extractor
	            ExifExtractor extractor = new ExifExtractor(dao);
	            
	            //java 8 lambdas are kinda useful, especially if you have a lot of server cores to play with...
	            manifest.getContents().stream().forEach(
	            		(contents) -> {
	            			logger.debug("KEY: " + contents.getKey());
	            			WaldoContentBean content = null;
	            		
							try {
								//load our content object into the database
								content = dao.insertContents(contents);
							} catch (Exception e) {
								logger.error("something bad inserting contents manifest: " + e.getLocalizedMessage());
							}
							//fetch image and extract exif
	            			extractor.parseJPGExif(manifestURI, content);
	            		});
	        } catch (Exception e) {
				logger.error("error parsing ListBucketResult: ",e.getLocalizedMessage());
	        }

		  return retList;
	  }
		

}
