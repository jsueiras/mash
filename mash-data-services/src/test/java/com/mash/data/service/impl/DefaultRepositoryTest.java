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
		
	   Repository rep = new DefaultRepository("C:\\workspace\\mash\\mash-data-services\\src\\main\\resources");
	   Person p = rep.findPersonById("1");
	   assertNotNull(p);
	   
	}
	
	@Test
	public void testGetPeople() {
		
	   Repository rep = new DefaultRepository("C:\\workspace\\mash\\mash-data-services\\src\\main\\resources");
	   Query query = new Query();
	   query.setFirstName("List");
	   List<Person> p = rep.findPersons(query);
	   assertNotNull(p);
	   
	}
	
	
	
	
	

}
