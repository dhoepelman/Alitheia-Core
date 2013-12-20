package eu.sqooss.impl.service.webadmin.refactored.servlets;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.pa.PluginAdmin;

public class PluginsServlet extends AbstractWebadminServlet {
	private PluginAdmin sobjPA;
	private MetricActivator compMA;
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
