package eu.sqooss.impl.service.webadmin.servlets;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.core.AlitheiaCore;

public class JobsServlet extends AbstractWebadminServlet {

	public JobsServlet(VelocityEngine ve,AlitheiaCore core) {
		super(ve, core);
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Template render(HttpServletRequest req, VelocityContext vc) {
		// TODO Auto-generated method stub
		return null;
	}

}
