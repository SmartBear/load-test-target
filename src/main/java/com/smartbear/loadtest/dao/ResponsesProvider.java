package com.smartbear.loadtest.dao;

import javax.ws.rs.core.MediaType;

/**
 * @author renato
 */
public interface ResponsesProvider
{

	/**
	 * @param mediaType media type to generate (JSon, XML etc)
	 * @return whatever response would be next if I don't care about any parameters
	 */
	String nextResponse( MediaType mediaType );

	/**
	 * @param mediaType media type to generate (JSon, XML etc)
	 * @param parameters to be used to find a response
	 * @return whatever response I want to return for the given parameters
	 */
	String responseFor( MediaType mediaType, String... parameters );

}
