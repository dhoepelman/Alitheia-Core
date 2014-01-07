package eu.sqooss.impl.service.webadmin.servlets;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class LogsServlet extends AbstractWebadminServlet {

	public LogsServlet(VelocityEngine ve) {
		super(ve);
	}

	@Override
	public List<String> getPaths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Template render(HttpServletRequest req, VelocityContext vc) {
		// TODO Auto-generated method stub
		return null;
	}

}
