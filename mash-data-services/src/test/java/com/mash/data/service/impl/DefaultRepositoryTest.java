package com.mash.data.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Person;

public class DefaultRepositoryTest {
	
	

	@Test
	public void testGetPerson() {
		
	   Repository rep = new DefaultRepository();
	   Person p = rep.findPersonById("homer");
	   assertNotNull(p);
	   
	}
	
	@Test
	public void testGetPeople() {
		
	   Repository rep = new DefaultRepository();
	   Query query = new Query();
	   query.setFirstName("Simpson");
	   List<Person> p = rep.findPersons(query);
	   assertNotNull(p);
	   
	}
	
	
	
	
	

}
