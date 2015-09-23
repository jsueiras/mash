package mash.util.process;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mash.document.utils.DocumentRenderer;
import mash.util.process.GenerateDocumentListener.DataTransformer;

import org.junit.Test;


public class DataTransformerTest {

	@Test
	public void test() {
		Map<String, Object> testData = createTestContext();
		
		Map<String,Object> results = DataTransformer.transform(testData);
		assertTrue(results.get("person") instanceof Map);
		Map<String,Object> person = (Map<String, Object>) results.get("person");
		assertEquals("foreName",person.get("firstName"));
		
		assertTrue(results.get("other") instanceof List);
		List<Map<String,Object>> others = (List<Map<String, Object>>) results.get("other");
		assertEquals(2, others.size());
		assertEquals("name1", others.get(0).get("firstName"));
		assertEquals("address1", others.get(0).get("homeAddress"));
		
		
	}
	
	
	@Test
	public void testPdf() throws FileNotFoundException {
		DocumentRenderer docR = new DocumentRenderer();
		File file = new File("result.pdf");
	OutputStream fop = new FileOutputStream(file);
  
		Map<String,Object> processContext  =  createTestContext();
		
		DocumentRenderer renderer = new DocumentRenderer();
		renderer.writeAsPdf("Research template.docx", DataTransformer.transform(processContext), fop);
	}	

	private Map<String, Object> createTestContext() {
		Map<String,Object> testData = new HashMap<String, Object>();
		testData.put("person.id", "id");
		testData.put("person.firstName", "foreName");
		testData.put("person.lastName", "foreName");
		testData.put("person.homeAddress", "address");
		testData.put("other1.firstName", "name1");
		testData.put("other1.homeAddress", "address1");
		testData.put("other2.firstName", "name2");
		testData.put("other2.homeAddress", "address2");
		return testData;
	}
	
	

}
