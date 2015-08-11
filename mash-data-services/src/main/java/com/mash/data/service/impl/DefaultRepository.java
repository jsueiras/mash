package com.mash.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Acts;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Persons;
import com.mash.model.catalog.Referral;

import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import  org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class DefaultRepository implements Repository {
	
	private static final String PERSON_PATH = "person/";

	private static final String REFERRAL_PATH = "referral/";

	private static final String ACT_PATH = "act/";

	RestTemplate restTemplate;
	
	String baseUrl;
	
	public DefaultRepository(String baseUrl) {
		
		List<MediaType> mediaType = new ArrayList<>();
		mediaType.add(MediaType.ALL);
		
	
     	restTemplate = new RestTemplate();
//		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//		marshaller.setPackagesToScan("com.mash.model.catalog");
//		marshaller.setClassesToBeBound(Person.class,Persons.class);
//	
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//		MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
        //converter.setSupportedMediaTypes(mediaType);
//		converter.setMarshaller(marshaller);
//		converter.setUnmarshaller(marshaller);
		messageConverters.add(new Jaxb2CollectionHttpMessageConverter());
		messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		
		//messageConverters.add();

		restTemplate.setMessageConverters(messageConverters);
		
		this.baseUrl = baseUrl;
	}

	@Override
	public Person findPersonById(String id) {
		
		
	   Person person= restTemplate.getForObject(baseUrl + PERSON_PATH + id , Person.class );
	   return person;
	}

	@Override
	public List<Person> findPersonsByName(String name) {
		// TODO Auto-generated method stub
		 Persons persons = restTemplate.getForObject(baseUrl + PERSON_PATH, Persons.class,name);
		 return persons.getPersons();
	}

	@Override
	public Referral findReferralById(String id) {
		 return restTemplate.getForObject(baseUrl + REFERRAL_PATH + id , Referral.class);
	}

	@Override
	public Referral saveReferral(Referral referral) {
		return restTemplate.postForObject(baseUrl + REFERRAL_PATH,referral,Referral.class);
	}

	@Override
	public List<Act> findEvents() {
		 Acts ps = restTemplate.getForObject(baseUrl + ACT_PATH, Acts.class);
		 return extract( ps.getActs());
	}
	
	
	private List<Act> extract(List<JAXBElement<? extends Act>> elements){
		List<Act> result = new ArrayList<Act>();
		
		for (JAXBElement<? extends Act> element : elements) {
			result.add(element.getValue());
		}
		return result;
		
	}
	

}
