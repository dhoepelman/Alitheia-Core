package eu.sqooss.impl.service.webadmin.servlets;

import java.util.List;

import javax.servlet.Servlet;

public interface IWebadminServlet extends Servlet {
	/**
	 * Gets the path(s) this servlet should handle
	 */
	public List<String> getPaths();
}
