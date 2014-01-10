package eu.sqooss.impl.service.webadmin.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.common.html.HtmlEscapers;

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
		try {
			sobjLogger = core.getLogManager().createLogger(Logger.NAME_SQOOSS_WEBADMIN);
		} catch(NullPointerException e) {
			// We can't get a logger, this is going great...
			// Implementing null checks everywhere is a PIA,
			// so we just as well might die right here right now then die to a NullPointerException along the way
			throw new RuntimeException("Could not retrieve a logger for the webadmin service (template " + this.getClass() + ")");
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
		VelocityContext vc = createDefaultVC(req);
		Template t;
		try {
			t = render(req, vc);
			if(t == null) {
				String request = req.getRequestURL() + (req.getQueryString()==null?"":"?" + req.getQueryString());
				getLogger().warn("Servlet " + this.getClass().getSimpleName() + " failed rendering request " + request);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				throw new ServletException("Failed rendering the request. Sorry about that.\nThis request caused me to break:\n" + HtmlEscapers.htmlEscaper().escape(request));
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
	 * Create the default VelocityContext with variables that every template uses or might use
	 */
	private VelocityContext createDefaultVC(HttpServletRequest req) {
		VelocityContext vc = new VelocityContext();
		// Add the translation class to every page
		vc.put("tr", getTranslation());
		// Add the current path
		vc.put("path", req.getRequestURI());

		return vc;
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

	protected Template loadTemplate(String path) {
		Template t = null;
		try {
			t = ve.getTemplate( path );
		} catch (Exception e) {
			getLogger().warn("Failed to get template <" + path + ">");
		}
		return t;
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
