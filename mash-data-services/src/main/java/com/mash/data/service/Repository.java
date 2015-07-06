package com.mash.data.service;

import java.util.List;

import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;

public interface Repository {
	
	public Person findPersonById(String id);

	public List<Person> findPersonsByName(String name);
	
	public Referral findReferralById(String id);
	
	public String saveReferral(Referral referral);
	

}
