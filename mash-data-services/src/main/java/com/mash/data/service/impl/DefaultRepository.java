package com.mash.data.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Locations;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Persons;
import com.mash.model.catalog.Referral;

public class DefaultRepository implements Repository {



	private Jaxb2Marshaller marshaller;

	private String directory;

	public DefaultRepository() {


		marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("com.mash.model.catalog");

	}

	public DefaultRepository(String directory) {
		this();
		this.directory = directory;

	}

	private StreamSource getStream(Query query)  {
		String resourceName = getFileName(query)+".xml";
		return getStream(resourceName);
	}

	private StreamSource getStream(String resourceName) {

		if (directory!=null)
		{

			File file = new File(directory+resourceName);
			try {
				return new StreamSource(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new StreamSource(this.getClass().getResourceAsStream("/" + resourceName));
	}


	@Override
	public Person findPersonById(String id) {
		return  (Person) findEntityById(id);
	}

	@Override
	public Referral findReferralById(String id) {
		JAXBElement<Referral>  referral=  (JAXBElement<Referral>) marshaller.unmarshal(getStream(id))   ;
		return referral.getValue();
	}

	@Override
	public Referral saveReferral(Referral referral) {
		return referral;
	}



	private List<Act> extract(List<JAXBElement<? extends Act>> elements){
		List<Act> result = new ArrayList<Act>();

		for (JAXBElement<? extends Act> element : elements) {
			result.add(element.getValue());
		}
		return result;

	}

	@Override
	public List<Entity> findEntitiesById(List<String> ids) {
		List<Entity> results = new ArrayList<Entity>();
		for (String id : ids) {
			results.add(findEntityById(id));
		}
		return results;
	}

	@Override
	public Location findLocationById(String id) {
		return (Location) findEntityById(id);
	}

	@Override
	public List<Person> findPersons(Query query) {
		// TODO Auto-generated method stub
		Persons persons =  (Persons) marshaller.unmarshal(getStream(query));

		return persons.getPersons();
	}

	@Override
	public List<Location> findLocations(Location sample) {
		Query query = new Query();
		query.setSampleLocation(sample);
		Locations locations =  (Locations) marshaller.unmarshal(getStream(query));

		return locations.getLocations();
	}


	public Entity findEntityById(String id) {
		JAXBElement<Entity>  location=  (JAXBElement<Entity>) marshaller.unmarshal(getStream(id.toLowerCase() + ".xml"));
		return location.getValue();
	}

	private String getFileName(Query query)
	{
		String result = "";
		if (query.getFirstName()!= null) result= result+ query.getFirstName();
		if (query.getLastName()!= null) result= result+ query.getLastName();
		if (query.getSampleLocation()!= null && query.getSampleLocation().getCity()!=null && result.length() == 0) result= query.getSampleLocation().getCity();
		return result.toLowerCase();

	}

}