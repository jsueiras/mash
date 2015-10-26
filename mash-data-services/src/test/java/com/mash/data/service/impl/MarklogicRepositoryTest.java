package com.mash.data.service.impl;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.data.service.SecurityInfo;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MarklogicRepositoryTest {

	@BeforeClass
	public static void testFixture() throws IOException, URISyntaxException {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "pa55w0rd"));

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setDefaultCredentialsProvider(credsProvider);

		HttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(factory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);

		processScript(restTemplate, "cts:uris() ! xdmp:document-delete(.)");

		restTemplate.put("http://localhost:8042/v1/documents?uri=/config/ingestion-config.xml", getContent("/config/ingestion-config.xml"));
		restTemplate.put("http://localhost:8042/v1/documents?uri=/raw/sp/GUARDIAN/GUARDIAN.xml", getContent("/raw/GUARDIAN.xml"));

		loadOntology(httpClient, "/ontology/mash.ttl");
		loadOntology(httpClient, "/ontology/sp.ttl");

		processScript(restTemplate, getContent("/process-feed.xqy"));
		processScript(restTemplate, getContent("/process-ontology.xqy"));
	}

	private static void processScript(RestTemplate restTemplate, String script) {
		MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("xquery", script);
		restTemplate.postForLocation("http://localhost:8042/v1/eval?database=mash-content-test", bodyMap);
	}

	private static void loadOntology(HttpClient httpClient, String uri) throws IOException {
		InputStreamEntity entity = new InputStreamEntity(MarklogicRepositoryTest.class.getResourceAsStream(uri));
		entity.setContentType("text/turtle");
		HttpPut httpPut = new HttpPut("http://localhost:8042/v1/documents?uri="+uri);
		httpPut.setEntity(new BufferedHttpEntity(entity));
		HttpResponse response = httpClient.execute(httpPut);
		response.getEntity().consumeContent();
	}

	private static String getContent(String uri) {
		InputStream inputStream = MarklogicRepositoryTest.class.getResourceAsStream(uri);
		StringBuilder sb = new StringBuilder();
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Test
	public void testSearchPersonAndDetail() throws IOException {
		Repository rep = new MarklogicRepositoryImpl("http://localhost:8042/v1/resources/");
		Query query = new Query();
		query.setFirstName("Pimpernel");
		SecurityInfo securityInfo = new SecurityInfo("test-case-user", "process-id", "stage");
		List<Person> response = rep.findPersons(query, securityInfo);
		assertEquals(1, response.size());

		Person p = response.get(0);
		assertNotNull(p);
		assertEquals("Pimpernel", p.getFirstName());
		assertEquals("SANDYMAN", p.getLastName());
		assertNotNull(p.getId());

		Person personById = rep.findPersonById(p.getId(), securityInfo);
		assertEquals(p.getId(), personById.getId());
	}


	@Test
	public void testSearchLocationAndDetail() throws IOException {
		MarklogicRepositoryImpl rep = new MarklogicRepositoryImpl("http://localhost:8042/v1/resources/");
		Location sample = new Location();
		sample.setStreet("FAXFLEET");
		Location location = rep.findLocations(sample,new SecurityInfo("1", "1", "Triage")).get(0);
		assertNotNull(location);

		Location locationDeatail = rep.findLocationById(location.getId(),new SecurityInfo("1", "1", "Triage"));
		assertEquals(location.getId(), locationDeatail.getId());
	}
}
