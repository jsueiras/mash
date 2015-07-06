package com.mash.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;

public class DefaultRepository implements Repository {
	
	private static final String PERSON_PATH = "person/";

	private static final String REFERRAL_PATH = "referral/";

	RestTemplate restTemplate;
	
	String baseUrl;
	
	public DefaultRepository(String baseUrl) {
		restTemplate = new RestTemplate();
		this.baseUrl = baseUrl;
	}

	@Override
	public Person findPersonById(String id) {
		
		
	   return restTemplate.getForObject(baseUrl + PERSON_PATH + id , Person.class);
	}

	@Override
	public List<Person> findPersonsByName(String name) {
		// TODO Auto-generated method stub
		 Person[] ps = restTemplate.getForObject(baseUrl + PERSON_PATH, Person[].class,name);
		 return Arrays.asList(ps);
	}

	@Override
	public Referral findReferralById(String id) {
		 return restTemplate.getForObject(baseUrl + REFERRAL_PATH + id , Referral.class);
	}

	@Override
	public String saveReferral(Referral referral) {
		// TODO Auto-generated method stub
		return null;
	}

}
