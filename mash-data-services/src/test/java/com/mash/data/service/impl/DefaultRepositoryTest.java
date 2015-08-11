package com.mash.data.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Person;

public class DefaultRepositoryTest {
	
	

	@Test
	public void testGetPerson() {
		
	   Repository rep = new DefaultRepository("http://localhost:8080/service/data/");
	   Person p = rep.findPersonById("1");
	   assertNotNull(p);
	   
	}
	
	@Test
	public void testGetPeople() {
		
	   Repository rep = new DefaultRepository("http://localhost:8080/service/data/");
	   List<Person> p = rep.findPersonsByName(null);
	   assertNotNull(p);
	   
	}
	
	@Test
	public void testEvents() {
		
	   Repository rep = new DefaultRepository("http://localhost:8080/service/data/");
	   List<Act> p = rep.findEvents();
	   assertNotNull(p);
	   
	}
	
	
	
	

}
