package eu.sqooss.impl.service.webadmin.refactored.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.impl.service.webadmin.refactored.ITranslation;
import eu.sqooss.service.db.DBService;

public abstract class AbstractWebadminServlet extends HttpServlet implements IWebadminServlet {
	private static VelocityEngine veinstance;
	private DBService sobjDB;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	protected abstract Template render(HttpServletRequest req, VelocityContext vc);

	protected DBService getDB() {
		// TODO
		return null;
	}
	
	protected ITranslation getTranslation() {
		// TODO
		return null;
	}
	
	protected VelocityEngine getVelocityEngine() {
		// TODO
		return null;
	}
}