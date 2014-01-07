package eu.sqooss.impl.service.webadmin.servlets;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.pa.PluginAdmin;

public class PluginsServlet extends AbstractWebadminServlet {
	public PluginsServlet(VelocityEngine ve) {
		super(ve);
		// TODO Auto-generated constructor stub
	}
	private PluginAdmin sobjPA;
	private MetricActivator compMA;
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
