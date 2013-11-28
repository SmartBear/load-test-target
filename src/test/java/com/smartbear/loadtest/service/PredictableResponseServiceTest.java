package com.smartbear.loadtest.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.smartbear.loadtest.TestClient;
import com.smartbear.loadtest.dao.ResponsesProvider;
import com.sun.jersey.api.client.ClientResponse;
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
 * @author Renato
 */
public class PredictableResponseServiceTest extends TestClient {

    final URI BASE_URI = getBaseURI();
    HttpServer server;

    @Override
    protected URI getBaseURI() {
        return UriBuilder.fromUri( "http://localhost/" ).port( 9998 ).build();
    }

    @Before
    public void startServer() throws IOException {
        System.out.println( "Starting grizzly..." );

        Injector injector = Guice.createInjector( new ServletModule() {
            @Override
            protected void configureServlets() {
                final ResponsesProvider mockResponses = mock( ResponsesProvider.class );
                when( mockResponses.nextResponse( MediaType.APPLICATION_JSON_TYPE ) )
                        .thenReturn( "A Json response" );
                when( mockResponses.nextResponse( MediaType.APPLICATION_XML_TYPE ) )
                        .thenReturn( "A Xml response" );
                when( mockResponses.responseFor( MediaType.APPLICATION_XML_TYPE, "small" ) )
                        .thenReturn( "Small Xml response" );
                when( mockResponses.responseFor( MediaType.APPLICATION_XML_TYPE, "large" ) )
                        .thenReturn( "Large Xml response" );
                when( mockResponses.responseFor( MediaType.APPLICATION_JSON_TYPE, "small" ) )
                        .thenReturn( "Small Json response" );
                when( mockResponses.responseFor( MediaType.APPLICATION_JSON_TYPE, "large" ) )
                        .thenReturn( "Large Json response" );

                bind( ResponsesProvider.class ).toInstance( mockResponses );
            }
        } );

        ResourceConfig rc = new PackagesResourceConfig( PredictableResponseService.class.getPackage().getName() );
        IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory( rc, injector );
        server = GrizzlyServerFactory.createHttpServer( BASE_URI + "loadtest/", rc, ioc );

        System.out.println( String.format( "Jersey app started with WADL available at "
                + "%sloadtest/application.wadl\nTry out %s{app_name}\nHit enter to stop it...",
                BASE_URI, BASE_URI ) );
    }

    @After
    public void stopServer() {
        server.stop();
    }

    @Test
    public void jsonResponsesShouldBeCreated() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_JSON );
        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( "A Json response" ) ) );
    }

    @Test
    public void jsonResponseDependsOnPathGiven_small() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_JSON, "small" );
        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( "Small Json response" ) ) );
    }

    @Test
    public void jsonResponseDependsOnPathGiven_large() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_JSON, "large" );
        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( "Large Json response" ) ) );
    }

    @Test
    public void xmlResponsesShouldBeCreated() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_XML );
        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( "A Xml response" ) ) );
    }

    @Test
    public void xmlResponseDependsOnPathGiven_small() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_XML, "small" );
        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( "Small Xml response" ) ) );
    }

    @Test
    public void xmlResponseDependsOnPathGiven_large() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_XML, "large" );
        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( "Large Xml response" ) ) );
    }

    @Test
    public void badPathReturnsNoContent() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_XML, "BAD" );
        assertThat( response.getStatus(), is( 204 ) );
    }

}
