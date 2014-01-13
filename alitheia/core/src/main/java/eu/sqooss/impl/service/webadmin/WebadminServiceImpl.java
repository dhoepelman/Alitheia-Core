/*
 * This file is part of the Alitheia system, developed by the SQO-OSS
 * consortium as part of the IST FP6 SQO-OSS project, number 033331.
 *
 * Copyright 2008 - 2010 - Organization for Free and Open Source Software,
 *                Athens, Greece.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package eu.sqooss.impl.service.webadmin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.servlets.IWebadminServlet;
import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
import eu.sqooss.impl.service.webadmin.servlets.ProjectsServlet;
import eu.sqooss.impl.service.webadmin.servlets.StaticResourceServlet;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.webadmin.WebadminService;

/**
 * This is the service which provides a web-based administration interface. This
 * user interface is used for the addition and removal of projects from Alitheia
 * Core and likewise for metric plugins. Users of the WebAdmin interface are
 * also able to receive basic system information such as uptime, number of
 * running/failed jobs, etc.
 */
public class WebadminServiceImpl implements WebadminService {

	/**
	 * The default page people are redirected to when accessing "/"
	 */
	private static final String DEFAULT_PAGE = "/plugins";

	private List<IWebadminServlet> servlets;
	private final StaticResourceServlet staticserv = new StaticResourceServlet(
			DEFAULT_PAGE);

	/**
	 * Used for logging all system messages generated by the WebAdmin
	 */
	private Logger logger = null;
	private BundleContext bc;

	private final AlitheiaCore core = AlitheiaCore.getInstance();

	public WebadminServiceImpl() {
		// Create the velocity engine
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("runtime.log.logsystem.class",
					"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			ve.setProperty("runtime.log.logsystem.log4j.category",
					Logger.NAME_SQOOSS_WEBADMIN);
			String resourceLoader = "classpath";
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, resourceLoader);
			ve.setProperty(resourceLoader + "."
					+ RuntimeConstants.RESOURCE_LOADER + ".class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		} catch (Exception e) {
			try {
				AlitheiaCore.getInstance().getLogManager()
				.createLogger(Logger.NAME_SQOOSS_WEBADMIN)
				.error("Velocity engine initialization", e);
			} catch (Exception e2) {
			}
		}

	}

	@Override
	public void setInitParams(BundleContext bc, Logger l) {
		logger = l;
		this.bc = bc;
	}

	private HttpService getHTTPService() {
		// Get a reference to the HTTPService, and then its object
		HttpService sobjHTTPService = null;
		ServiceReference srefHTTPService = bc
				.getServiceReference(HttpService.class.getName());

		if (srefHTTPService != null) {
			sobjHTTPService = (HttpService) bc.getService(srefHTTPService);
		} else {
			logger.error("Could not find a HTTP service");
			return null;
		}

		if (sobjHTTPService == null) {
			logger.error("Could retrieve the HTTP service");
			return null;
		}

		return sobjHTTPService;
	}

	@Override
	public void shutDown() {
		HttpService sobjHTTPService = getHTTPService();
		if (sobjHTTPService == null)
			return;

		if (servlets == null)
			return;
		// Unregister all paths
		for (IWebadminServlet was : servlets) {
			try {
				sobjHTTPService.unregister(was.getPath());
			} catch (IllegalArgumentException e) {
				// Was already unregistered, result is the same though
			}
		}

		// Unregister static resource servlet
		sobjHTTPService.unregister("/");
	}

	@Override
	public boolean startUp() {
		logger.debug("Starting up webadmin service");

		HttpService sobjHTTPService = getHTTPService();
		if (sobjHTTPService == null) {
			logger.error("Could not get HTTP Service");
			return false;
		}

		// Initialize Velocity
		VelocityEngine ve = createVelocity();
		logger.debug("Created Velocity engine");

		// Initialize the servlets
		servlets = new ArrayList<IWebadminServlet>();

		// Add new servlets here
		// Providing AlitheiaCore in the constructor might seem superfluous as
		// that code can be in the servlets themselves,
		// but this allows to test the servlets without mocking static methods
		// TODO: Change to use dependency injection when that is finished
		// servlets.add(new JobsServlet(ve));
		// servlets.add(new LogsServlet(ve));
		// servlets.add(new OptionsServlet(ve));
		servlets.add(new PluginsServlet(ve, core));
		servlets.add(new ProjectsServlet(ve, core));
		// servlets.add(new StatusServlet(ve));

		// Register the servlets
		for (IWebadminServlet was : servlets) {
			// Register the path the servlet is responsible for
			String path = was.getPath();
			try {
				sobjHTTPService.registerServlet(path, was, null, null);
				logger.debug("Registered " + path + " to "
						+ was.getClass().getSimpleName());
			} catch (ServletException e) {
				// Servlet init method throws exception
				logger.error("Initialization of webadmin servlet "
						+ was.getClass().getSimpleName(), e);
				continue; // Skip this servlet
			} catch (NamespaceException e) {
				logger.warn("Doubly registered webadmin path " + path, e);
			}
		}
		// Register static resources, also handles not found errors and
		// redirects "/" to the appropriate page
		try {
			sobjHTTPService.registerServlet("/", staticserv, null, null);
		} catch (NamespaceException | ServletException e) {
			logger.error("Registering of static webadmin resources", e);
		}

		logger.debug("Successfully loaded webadmin service");
		return true;
	}

	private VelocityEngine createVelocity() {
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("runtime.log.logsystem.class",
					"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			ve.setProperty("runtime.log.logsystem.log4j.category",
					Logger.NAME_SQOOSS_WEBADMIN);
			String resourceLoader = "classpath";
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, resourceLoader);
			ve.setProperty(resourceLoader + "."
					+ RuntimeConstants.RESOURCE_LOADER + ".class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			return ve;
		} catch (Exception e) {
			logger.error("Could not initialize Velocity", e);
			return null;
		}
	}
}

// vi: ai nosi sw=4 ts=4 expandtab

