package com.smartbear.loadtest;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.smartbear.loadtest.dao.ResponsesProvider;
import com.smartbear.loadtest.service.PredictableResponseService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Renato
 */
public class ServerTest
{

	static final URI BASE_URI = getBaseURI();
	HttpServer server;

	private static URI getBaseURI()
	{
		return UriBuilder.fromUri( "http://localhost/" ).port( 9998 ).build();
	}

	@Before
	public void startServer() throws IOException
	{
		System.out.println( "Starting grizzly..." );

		Injector injector = Guice.createInjector( new ServletModule()
		{
			@Override
			protected void configureServlets()
			{
				final ResponsesProvider mockResponses = mock( ResponsesProvider.class );
				when( mockResponses.nextResponse( MediaType.APPLICATION_JSON_TYPE ) )
						.thenReturn( "A Json response" );
				//TODO mock some more responses
				bind( ResponsesProvider.class ).toInstance( mockResponses );
			}
		} );

		ResourceConfig rc = new PackagesResourceConfig( PredictableResponseService.class.getPackage().getName() );
		IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory( rc, injector );
		server = GrizzlyServerFactory.createHttpServer( BASE_URI, rc, ioc );

		System.out.println( String.format( "Jersey app started with WADL available at "
				+ "%sservices/application.wadl\nTry out %s{app_name}\nHit enter to stop it...",
				BASE_URI, BASE_URI ) );
	}

	@After
	public void stopServer()
	{
		server.stop();
	}

	@Test
	public void jsonResponsesShouldBeCreated() {
		Client client = Client.create( new DefaultClientConfig() );
		WebResource service = client.resource( getBaseURI() );

		ClientResponse resp = service
				.accept( MediaType.APPLICATION_JSON )
				.get( ClientResponse.class );

		String response = resp.getEntity( String.class );

		assertThat( response, is( equalTo( "A Json response" ) ) );

	}

	public static void main( String[] args ) throws Exception
	{
		ServerTest test = new ServerTest();
		test.startServer();
		System.in.read(); // hit enter to stop the server
		test.server.stop();
	}

}