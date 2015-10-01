package com.mash.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Acts;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Persons;
import com.mash.model.catalog.Referral;

import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import  org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class MarklogicRepositoryImpl implements Repository {
	
	private static final String PERSON_PATH = "person";

	private static final String REFERRAL_PATH = "referral/";

	private static final String ACT_PATH = "act/";

	RestTemplate restTemplate;
	
	String baseUrl;
	
	public MarklogicRepositoryImpl(String baseUrl) {
		
		List<MediaType> mediaType = new ArrayList<>();
		mediaType.add(MediaType.ALL);
		 Credentials credentials;
		  
		   //1. Set credentials
		   credentials = new UsernamePasswordCredentials("admin", "admin");
		  
		   CredentialsProvider credsProvider = new BasicCredentialsProvider();
		   credsProvider.setCredentials( AuthScope.ANY, credentials);
		   
		          //2. Bind credentialsProvider to httpClient
		   HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		   httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
		   CloseableHttpClient httpClient = httpClientBuilder.build();

		   HttpComponentsClientHttpRequestFactory factory = new  
		                       HttpComponentsClientHttpRequestFactory(httpClient);
		  
		   //3. create restTemplate
		   restTemplate = new RestTemplate();
		   restTemplate.setRequestFactory(factory);	
     	//restTemplate = new RestTemplate(rf);
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
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rs:query", id);
		
	   Person person= restTemplate.getForObject(baseUrl + PERSON_PATH , Person.class ,params);
	   return person;
	}

	
	@Override
	public Referral findReferralById(String id) {
		 return restTemplate.getForObject(baseUrl + REFERRAL_PATH + id , Referral.class);
	}

	@Override
	public Referral saveReferral(Referral referral) {
		return restTemplate.postForObject(baseUrl + REFERRAL_PATH,referral,Referral.class);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location findLocationById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findPersons(Query query) {
		// TODO Auto-generated method stub
		//HashMap<String, String> params = new HashMap<String, String>();
		//params.put("rsquery", name);
		
		 Persons persons = restTemplate.getForObject(baseUrl + PERSON_PATH + "?rs:query=" + query.getFirstName(), Persons.class);
		 return persons.getPersons();

	}

	@Override
	public List<Location> findLocations(Location sample) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
