package com.smartbear.loadtest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.smartbear.loadtest.dao.PreFabricatedResponses;
import com.smartbear.loadtest.dao.ResponsesProvider;
import com.smartbear.loadtest.service.PredictableResponseService;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * @author Renato
 */
public class AppServletContextListener extends GuiceServletContextListener
{

	@Override
	protected Injector getInjector()
	{
		final ResourceConfig rc = new PackagesResourceConfig( PredictableResponseService.class.getPackage().getName() );

		return Guice.createInjector( new ServletModule()
		{
			@Override
			protected void configureServlets()
			{
				bind( ResponsesProvider.class ).to( PreFabricatedResponses.class );

				for( Class<?> resource : rc.getClasses() )
				{
					System.out.println( "Binding resource: " + resource.getName() );
					bind( resource );
				}

				serve( "loadtest/*" ).with( GuiceContainer.class );
			}
		} );
	}

}