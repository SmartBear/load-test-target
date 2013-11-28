package com.smartbear.loadtest;


import com.google.inject.Injector;
import com.smartbear.loadtest.dao.PreFabricatedResponses;
import com.smartbear.loadtest.service.PredictableResponseService;
import com.smartbear.loadtest.utils.FileResourceLoader;
import com.smartbear.loadtest.utils.ResourceLoader;
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

/**
 * User: Renato
 */
public class ServerTest extends TestClient {

    final URI BASE_URI = getBaseURI();
    HttpServer server;
    ResourceLoader loader = new FileResourceLoader();

    @Override
    protected URI getBaseURI() {
        return UriBuilder.fromUri( "http://localhost/" ).port( 9998 ).build();
    }

    @Before
    public void startServer() throws IOException {
        System.out.println( "Starting grizzly..." );

        Injector injector = new AppServletContextListener().getInjector();

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
    public void serverMapsRequestsToResourcesCorrectly() {
        ClientResponse response = makeRequest( MediaType.APPLICATION_JSON, medium() );
        String expected = loader.read( PreFabricatedResponses.Resources.mediumJsonResource() );
        System.out.println( "Lenght of response: " + expected.length() );

        assertThat( response.getStatus(), is( 200 ) );
        assertThat( asString( response ), is( equalTo( expected ) ) );
    }

    public static void main( String[] args ) throws Exception {
        ServerTest test = new ServerTest();
        test.startServer();
        System.in.read(); // hit enter to stop the server
        test.server.stop();
    }

}