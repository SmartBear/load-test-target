package com.smartbear.loadtest.dao;

import javax.ws.rs.core.MediaType;

/**
 * @author renato
 */
public class PreFabricatedResponses implements ResponsesProvider
{


	@Override
	public String nextResponse( MediaType mediaType )
	{
		if( mediaType == MediaType.APPLICATION_XML_TYPE ) return xml();
		else return json();
	}

	@Override
	public String responseFor( MediaType mediaType, String... parameters )
	{
		return nextResponse( mediaType );
	}

	private String json()
	{
		return "{ \"something\": \"value\" }";
	}

	private String xml()
	{
		return "<root><something>value</something></root>";
	}

}
