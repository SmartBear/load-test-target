package com.smartbear.loadtest.service;

import com.smartbear.loadtest.dao.ResponsesProvider;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.*;

/**
 * @author renato
 */
@Path("predictable")
public class PredictableResponseService {

    private static final Logger log = Logger.getLogger( PredictableResponseService.class );

    private ResponsesProvider responses;

    @Inject
    public PredictableResponseService( ResponsesProvider responses ) {
        this.responses = responses;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public String getNoParamJsonResponse( @DefaultValue( "veryfast" )
                                          @QueryParam( "speed" ) String delay ) {
        doSleep( delay );
        return responses.responseFor( MediaType.APPLICATION_JSON_TYPE, defaultSize(), delay );
    }

    @GET
    @Produces(APPLICATION_XML)
    public String getNoParamXmlResponse( @DefaultValue( "0" )
                                         @QueryParam( "delay" ) String delay ) {
        doSleep( delay );
        return responses.responseFor( MediaType.APPLICATION_XML_TYPE, defaultSize(), delay );
    }

    @GET
    @Produces( APPLICATION_JSON )
    @Path( "{size}" )
    public String getJsonResponse( @PathParam( "size" ) String size,
                                   @DefaultValue( "0" )
                                   @QueryParam( "delay" ) String delay ) {
        doSleep( delay );
        return responses.responseFor( APPLICATION_JSON_TYPE, asList( size ), delay );
    }

    @GET
    @Produces( APPLICATION_XML )
    @Path( "{size}" )
    public String getXmlResponse( @PathParam( "size" ) String size,
                                  @DefaultValue( "0" )
                                  @QueryParam( "delay" ) String delay ) {
        doSleep( delay );
        return responses.responseFor( APPLICATION_XML_TYPE, asList( size ), delay );
    }

    private List<String> defaultSize() {
        return asList( "small" );
    }

    private void doSleep( String delay ) {
        try {
            Thread.sleep( longOrZero( delay ) );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private long longOrZero( String delay ) {
        try {
            return Long.parseLong( delay );
        } catch ( NumberFormatException nfe ) {
            log.debug( "Tried to convert " + delay + " to long, this won't work" );
            return 0L;
        }
    }

}
