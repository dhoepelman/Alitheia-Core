package eu.sqooss.impl.service.webadmin.servlets;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.service.cluster.ClusterNodeService;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.updater.UpdaterService;

public class ProjectsServlet extends AbstractWebadminServlet {
	public ProjectsServlet(VelocityEngine ve) {
		super(ve);
		// TODO Auto-generated constructor stub
	}

	private MetricActivator compMA;
	private Scheduler sobjSched;
	private UpdaterService sobjUpdater;
	private ClusterNodeService sobjClusterNode;


	private void addProject(StoredProject project) {
		// TODO: Add project
	}

	private void removeProject(StoredProject project) {
		// TODO: Remove project
	}

	private void triggerUpdate(StoredProject project, String updater) {
		// TODO: Trigger a specific updater on a project
	}

	private void triggerAllUpdate(StoredProject project) {
		// TODO: Trigger all updates on a project
	}

	private void triggerAllUpdateNode() {
		// TODO: Trigger all updates on all projects
	}

	private void synchPlugin(StoredProject project, String plugin) {
		// TODO: Synch a plugin of a project
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
