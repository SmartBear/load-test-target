package com.smartbear.loadtest.utils;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * @author Renato
 */
public class FileResourceLoader implements ResourceLoader {

    @Override
    public String read( URL url ) {
        try {
            return new String( Files
                    .readAllBytes( Paths.get( url.toURI() ) ), US_ASCII );
        } catch ( Exception e ) {
            throw new RuntimeException( "Could not read URL: " + url, e );
        }
    }

}
