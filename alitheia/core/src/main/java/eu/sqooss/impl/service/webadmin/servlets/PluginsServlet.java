package eu.sqooss.impl.service.webadmin.servlets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.common.collect.ImmutableMap;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.pa.PluginAdmin;
import eu.sqooss.service.pa.PluginInfo;

public class PluginsServlet extends AbstractWebadminServlet {

	private static final String ROOT_PATH = "/plugins";

	private static final String PAGE_PLUGINMANAGEMENT = ROOT_PATH;

	private static final Map<String, String> templates = new ImmutableMap.Builder<String, String>()
			.put(PAGE_PLUGINMANAGEMENT, "/webadmin/pluginlist.vm")
			.build();

	public PluginsServlet(VelocityEngine ve) {
		super(ve);
		sobjPA = AlitheiaCore.getInstance().getPluginAdmin();
		compMA = AlitheiaCore.getInstance().getMetricActivator();
	}

	private PluginAdmin sobjPA;
	private MetricActivator compMA;

	@Override
	public String getPath() {
		return ROOT_PATH;
	}

	@Override
	protected Template render(HttpServletRequest req, VelocityContext vc) {
		switch(req.getRequestURI()) {
		case PAGE_PLUGINMANAGEMENT:
			return PluginsManagmentPage(req, vc);
		default:
			getLogger().warn(this.getClass() + " was called with incorrect path " + req.getRequestURI());
			return null;
		}
		//return null;
	}

	private Template PluginsManagmentPage(HttpServletRequest req, VelocityContext vc) {
		// Load the template
		Template t = loadTemplate(templates.get(PAGE_PLUGINMANAGEMENT));

		boolean showProperties = req.getParameter("showProperties")!=null&&req.getParameter("showProperties").equals("true");
		boolean showActivators = req.getParameter("showActivators")!=null&&req.getParameter("showActivators").equals("true");

		// Whether to show properties and activators
		vc.put("showProperties", showProperties);
		vc.put("showActivators", showActivators);

		// The list of plugins
		Collection<PluginInfo> pluginList = sobjPA.listPlugins();

		if(pluginList == null) {
			pluginList = new ArrayList<PluginInfo>();
			getLogger().warn("Could not get plugin information from PluginAdmin");
		}
		vc.put("pluginList", pluginList);

		return t;
	}
}
