package com.smartbear.loadtest.service;

import com.smartbear.loadtest.dao.ResponsesProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * @author renato
 */
@Path( "" )
public class PredictableResponseService
{

	private ResponsesProvider responses;

	@Inject
	public PredictableResponseService( ResponsesProvider responses )
	{
		this.responses = responses;
	}

	@GET
	@Produces( APPLICATION_JSON )
	public String getNoParamJsonResponse()
	{
		System.out.println( "Asked for Json response with no parameters" );
		return responses.nextResponse( MediaType.APPLICATION_JSON_TYPE );
	}

	@GET
	@Produces( APPLICATION_XML )
	public String getNoParamXmlResponse()
	{
		System.out.println( "Asked for XML response with no parameters" );
		return responses.nextResponse( MediaType.APPLICATION_XML_TYPE );
	}

}
