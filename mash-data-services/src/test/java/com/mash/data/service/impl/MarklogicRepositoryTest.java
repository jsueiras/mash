package com.mash.data.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Person;

public class MarklogicRepositoryTest {
	
	

	@Test
	public void testGetPerson() {
		
	   Repository rep = new MarklogicRepositoryImpl("http://localhost:8040/v1/resources/");
	   Person p = rep.findPersonById("SANDMAN");
	   assertNotNull(p);
	   
	}
	
	@Test
	public void testGetPeople() {
		
	   Repository rep = new MarklogicRepositoryImpl("http://localhost:8040/v1/resources/");
	   Query query = new Query();
	   query.setFirstName("SANDMAN");
	   
	   List<Person> p = rep.findPersons(query);
	   assertNotNull(p);
	   
	}
	
	
	
	
	
	

}
