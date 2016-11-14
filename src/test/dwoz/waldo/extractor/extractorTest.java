package dwoz.waldo.extractor;

import static org.junit.Assert.*;

import java.util.List;

import dwoz.waldo.extractor.ManifestProcessor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.s3.doc._2006_03_01.ListBucketResult;
import com.dwoz.common.context.ContextHelper;
import com.dwoz.common.util.TestUtils;

public class extractorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testProcessManifest() {
		//we're going to go out to the supplied url and try to load a manifest.
		//ManifestProcessor will retrieve the manifest xml, process it
		//for jpeg contents, retrieve the named jpeg file, and parse the exif data from the image file
		//this is not a fully "ok" jUnit coverage test, we're merely using it as a test harness to launch
		//our processor, rather than have a static void main() method in ManifestProcessor.
		try{
		//configure my slf4j logger...
        TestUtils.configureLogger();  
        // initialize a JNDI helper to get a database connection from...
        ContextHelper.setupInitialContext();
		ManifestProcessor extractor = new ManifestProcessor();
		List<ListBucketResult> result = extractor.processManifest("http://s3.amazonaws.com/waldo-recruiting/");
		
		assertFalse(result == null);//we're expecting values
		} catch (Exception e){
			fail("threw an exception:" + e.getLocalizedMessage());
		}
	}


}
