package com.smartbear.loadtest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.net.URI;

/**
 * @author Renato
 */
public abstract class TestClient {

    protected Client client = Client.create( new DefaultClientConfig() );
    protected WebResource service = client.resource( getBaseURI() );

    protected abstract URI getBaseURI();

    protected ClientResponse makeRequest( String accept, String... path ) {
        return toResource( path )
                .accept( accept )
                .get( ClientResponse.class );
    }

    protected String asString( ClientResponse response ) {
        return response.getEntity( String.class );
    }

    protected WebResource toResource( String... path ) {
        WebResource resource = service.path( "loadtest" ).path( "predictable" );
        for ( String p : path ) {
            resource = resource.path( p );
        }
        return resource;
    }

}
