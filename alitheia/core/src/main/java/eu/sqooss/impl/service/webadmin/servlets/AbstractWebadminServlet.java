package eu.sqooss.impl.service.webadmin.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.ITranslation;
import eu.sqooss.impl.service.webadmin.Translation;
import eu.sqooss.service.db.DBService;
import eu.sqooss.service.logging.Logger;

public abstract class AbstractWebadminServlet extends HttpServlet implements IWebadminServlet {

	private final VelocityEngine ve;

	// AlitheiaCore god object. Will probably get replaced by dependency injection but for now use this
	private final AlitheiaCore core = AlitheiaCore.getInstance();

	// Services that most likely all subclasses will use
	protected final DBService sobjDB;
	protected final Logger sobjLogger;

	public AbstractWebadminServlet(VelocityEngine ve) {
		Logger sobjLogger = null;
		try {
			sobjLogger = core.getLogManager().createLogger(Logger.NAME_SQOOSS_WEBADMIN);
		} catch(NullPointerException e) {
			// We can't get a logger, this is going great...
			sobjLogger = null;
		} finally {
			this.sobjLogger = sobjLogger;
		}

		sobjDB = core.getDBService();
		if(sobjDB == null && sobjLogger != null) {
			sobjLogger.error("Could not get the database component's instance.");
		}

		this.ve = ve;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		preRender();
		VelocityContext vc = new VelocityContext();
		Template t;
		try {
			t = render(req, vc);
			if(t == null) {
				getLogger().warn("Servlet " + this.getClass().getName() + " failed rendering request");
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else {
				response.setContentType("text/html");
				t.merge(vc, response.getWriter());
			}
		} finally {
			postRender();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}

	/**
	 * Sets up neccesary conditions before rendering (like starting a DB serssion)
	 */
	private void preRender() {
		if (!getDB().isDBSessionActive()) {
			getDB().startDBSession();
		}
	}

	protected abstract Template render(HttpServletRequest req, VelocityContext vc);

	/**
	 * Does actions necessary after rendering (like committing the DB session)
	 * Should always be called, i.e. in a finally block
	 */
	private void postRender() {
		if (sobjDB.isDBSessionActive()) {
			sobjDB.commitDBSession();
		}
	}

	protected DBService getDB() {
		return sobjDB;
	}

	protected Logger getLogger() {
		return sobjLogger;
	}

	protected ITranslation getTranslation() {
		return Translation.EN;
	}

	protected VelocityEngine getVelocityEngine() {
		return ve;
	}
}
