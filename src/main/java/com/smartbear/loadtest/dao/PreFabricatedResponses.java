package com.smartbear.loadtest.dao;

import com.smartbear.loadtest.utils.ResourceLoader;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.net.URL;

/**
 * @author renato
 */
public class PreFabricatedResponses implements ResponsesProvider {

    Logger log = Logger.getLogger( PreFabricatedResponses.class );

    private final String smallJson;
    private final String mediumJson;
    private final String largeJson;
    private final String smallXml;
    private final String mediumXml;
    private final String largeXml;

    @Inject
    public PreFabricatedResponses( ResourceLoader loader ) {
        log.info( "Starting up ResponsesProvider" );

        // to make requests completely predictable, we need to keep
        // all possible responses in memory
        smallJson = loader.read( Resources.smallJsonResource() );
        mediumJson = loader.read( Resources.mediumJsonResource() );
        largeJson = loader.read( Resources.largeJsonResource() );
        smallXml = loader.read( Resources.smallXmlResource() );
        mediumXml = loader.read( Resources.mediumXmlResource() );
        largeXml = loader.read( Resources.largeXmlResource() );

    }

    @Override
    public String nextResponse( MediaType mediaType ) {
        if ( mediaType == MediaType.APPLICATION_XML_TYPE )
            return smallXml;
        else
            return smallJson;
    }

    private String nextResponse( MediaType mediaType, String size ) {
        switch ( size ) {
            case "large":
                return mediaType == MediaType.APPLICATION_XML_TYPE ?
                        largeXml : largeJson;
            case "medium":
                return mediaType == MediaType.APPLICATION_XML_TYPE ?
                        mediumXml : mediumJson;
            case "small":
            default:
                return mediaType == MediaType.APPLICATION_XML_TYPE ?
                        smallXml : smallJson;
        }
    }

    @Override
    public String responseFor( MediaType mediaType, String... parameters ) {
        if ( parameters.length == 0 ) {
            return nextResponse( mediaType );
        } else {
            String size = parameters[ 0 ];
            return nextResponse( mediaType, size );
        }
    }

    public static class Resources {

        public static URL smallJsonResource() {
            return Resources.class.getResource( "/responses/small.json" );
        }

        public static URL mediumJsonResource() {
            return Resources.class.getResource( "/responses/medium.json" );
        }

        public static URL largeJsonResource() {
            return Resources.class.getResource( "/responses/large.json" );
        }

        public static URL smallXmlResource() {
            return Resources.class.getResource( "/responses/small.xml" );
        }

        public static URL mediumXmlResource() {
            return Resources.class.getResource( "/responses/medium.xml" );
        }

        public static URL largeXmlResource() {
            return Resources.class.getResource( "/responses/large.xml" );
        }

    }

}
