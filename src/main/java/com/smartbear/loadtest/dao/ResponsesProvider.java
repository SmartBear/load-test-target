package com.smartbear.loadtest.dao;

import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author renato
 */
public interface ResponsesProvider {

    /**
     * @param mediaType  media type to generate (JSon, XML etc)
     * @param parameters to be used to find a response
     * @param queries    query values - use the order to determine which key
     * @return whatever response I want to return for the given parameters
     */
    String responseFor( MediaType mediaType, List<String> parameters, String... queries );

}
