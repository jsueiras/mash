package mash.util.process;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mash.util.process.GenerateResearchPdfListener.DataTransformer;

import org.junit.Test;


public class DataTransformerTest {

	@Test
	public void test() {
		Map<String,Object> testData = new HashMap<String, Object>();
		testData.put("person.name", "name");
		testData.put("person.address", "address");
		testData.put("otherp1.name", "name1");
		testData.put("otherp1.address", "address1");
		testData.put("otherp2.name", "name2");
		testData.put("otherp2.address", "address2");
		
		Map<String,Object> results = DataTransformer.transform(testData);
		assertTrue(results.get("person") instanceof Map);
		Map<String,Object> person = (Map<String, Object>) results.get("person");
		assertEquals(person.get("name"),"name");
		
		assertTrue(results.get("otherp") instanceof List);
		List<Map<String,Object>> others = (List<Map<String, Object>>) results.get("otherp");
		assertEquals(2, others.size());
		assertEquals("name1", others.get(0).get("name"));
		assertEquals("address1", others.get(0).get("address"));
		
		
	}

}
