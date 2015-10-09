package com.mash.data.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.Result;

import com.mash.data.service.*;
import com.mash.data.service.Query;
import com.mash.model.catalog.*;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.*;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

public class MarklogicRepositoryImpl implements Repository {
    private static final String ADDRESS_PATH = "address";
	private static final String PERSON_PATH = "person";
	private static final String REFERRAL_PATH = "referral/";
	private static final String ACT_PATH = "act/";

	RestTemplate restTemplate;
	
	String baseUrl;
	private Jaxb2Marshaller marshaller;
    private CloseableHttpClient httpClient;

	public MarklogicRepositoryImpl(String baseUrl) {
		
		List<MediaType> mediaType = new ArrayList<>();
		mediaType.add(MediaType.ALL);
		 Credentials credentials;
		  
		   //1. Set credentials
		   credentials = new UsernamePasswordCredentials("admin", "pa55w0rd");
		  
		   CredentialsProvider credsProvider = new BasicCredentialsProvider();
		   credsProvider.setCredentials(AuthScope.ANY, credentials);
		   
		          //2. Bind credentialsProvider to httpClient
		   HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		   httpClientBuilder.setDefaultCredentialsProvider(credsProvider);

        httpClient = httpClientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory = new
		                       HttpComponentsClientHttpRequestFactory(httpClient);
		  
		   //3. create restTemplate
		   restTemplate = new RestTemplate();
		   restTemplate.setRequestFactory(factory);	
     	//restTemplate = new RestTemplate(rf);

//		marshaller = new Jaxb2Marshaller();
//		marshaller.setPackagesToScan("com.mash.model.catalog","com.mash.data.service");
//		marshaller.setClassesToBeBound(Person.class,Persons.class,Locations.class,Location.class,Query.class);
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
	   return restTemplate.getForObject(baseUrl + PERSON_PATH + "?rs:id=" + id, Person.class);
	}

	@Override
	public List<Person> findPersons(Query query) throws IOException {
		return restTemplate.postForObject(baseUrl + PERSON_PATH, query, Persons.class).getPersons();
	}

	@Override
	public Referral findReferralById(String id) {
		 return restTemplate.getForObject(baseUrl + REFERRAL_PATH + id, Referral.class);
	}

	@Override
	public Referral saveReferral(Referral referral) {
		return restTemplate.postForObject(baseUrl + REFERRAL_PATH, referral, Referral.class);
	}

    @Override
    public List<Location> findLocations(Location sample) throws IOException {
		Query sampleQuery = new Query();
		sampleQuery.setSampleLocation(sample);
		return restTemplate.postForObject(baseUrl + ADDRESS_PATH, sampleQuery, Locations.class).getLocations();
    }

    @Override
    public Location findLocationById(String id) {
        return restTemplate.getForObject(baseUrl + ADDRESS_PATH + "?rs:id=" + id, Location.class);
    }

	@Override
	public List<Entity> findEntitiesById(List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}
}
