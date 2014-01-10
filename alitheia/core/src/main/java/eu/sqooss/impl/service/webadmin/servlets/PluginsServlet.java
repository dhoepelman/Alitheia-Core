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

	private static final String PAGE_PLUGINSLIST = ROOT_PATH;

	private static final String PAGE_PLUGIN = ROOT_PATH + "/plugin";

	private static final Map<String, String> templates = new ImmutableMap.Builder<String, String>()
			.put(PAGE_PLUGINSLIST, "/pluginlist.vm")
			.put(PAGE_PLUGIN, "/plugin.vm")
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
		case PAGE_PLUGINSLIST:
			return PagePluginsList(req, vc);
		case PAGE_PLUGIN:
			return PagePlugin(req, vc);
		default:
			getLogger().warn(this.getClass() + " was called with incorrect path " + req.getRequestURI());
			return null;
		}
	}

	private Template PagePluginsList(HttpServletRequest req, VelocityContext vc) {
		// Load the template
		Template t = loadTemplate(templates.get(PAGE_PLUGINSLIST));

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

	private Template PagePlugin(HttpServletRequest req, VelocityContext vc) {
		Template t = loadTemplate(templates.get(PAGE_PLUGIN));

		if(req.getParameter("hash") == null || req.getParameter("hash").isEmpty())
			return makeErrorMsg(vc, "No plugin selected");

		String hash = req.getParameter("hash");
		PluginInfo plugin = sobjPA.getPluginInfo(hash);
		if(plugin == null)
			return makeErrorMsg(vc, "Plugin does not exist");

		vc.put("plugin", plugin);
		vc.put("metrics", sobjPA.getPlugin(plugin).getAllSupportedMetrics());
		vc.put("configPropList", plugin.getConfiguration());

		return t;
	}
}
