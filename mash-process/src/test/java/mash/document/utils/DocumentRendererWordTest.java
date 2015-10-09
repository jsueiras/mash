package mash.document.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;

import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;

public class DocumentRendererWordTest {

	@Test
	public void test() throws FileNotFoundException {
		DocumentRenderer docR = new DocumentRenderer();
			File file = new File("result.docx");
		OutputStream fop = new FileOutputStream(file);
		
		Map<String, Object> ctx = new HashMap<String, Object>();
		// Get template stream (either the default or overridden by the
		// user)
		Person person = new Person();
		person.setFirstName("Jose");
		person.setLastName("Sueiras");
		person.setId("id");
		ctx.put("person", person);

		List<Person> people = new ArrayList<Person>();
		people.add(person);
		people.add(person);
		ctx.put("other", people);
		ctx.put("date", new Date());
		
		docR.writeAsWord("Research template.docx",ctx, fop);
	}

}
