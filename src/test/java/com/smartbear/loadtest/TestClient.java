package com.smartbear.loadtest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * @author Renato
 */
public abstract class TestClient {

    protected Client client = Client.create( new DefaultClientConfig() );
    protected WebResource service = client.resource( getBaseURI() );

    protected abstract URI getBaseURI();

    protected ClientResponse makeRequest( String accept, List<String> paths, String... queries ) {
        return toResource( paths, queries )
                .accept( accept )
                .get( ClientResponse.class );
    }

    protected String asString( ClientResponse response ) {
        return response.getEntity( String.class );
    }

    protected WebResource toResource( List<String> paths, String... queries ) {
        WebResource resource = service.path( "loadtest" ).path( "predictable" );
        for ( String p : paths ) {
            System.out.println( "Adding path " + p );
            resource = resource.path( p );
        }
        for ( String query : queries ) {
            System.out.println( "Adding query " + query );
            String[] parts = query.split( "=" );
            if ( parts.length == 2 ) {
                String key = parts[ 0 ];
                String value = parts[ 1 ];
                resource = resource.queryParam( key, value );
            } else {
                throw new RuntimeException( "Queries must be of the form key=value" );
            }
        }
        return resource;
    }

    protected List<String> small() {
        return Arrays.asList( "small" );
    }

    protected List<String> medium() {
        return Arrays.asList( "medium" );
    }

    protected List<String> large() {
        return Arrays.asList( "large" );
    }


}
