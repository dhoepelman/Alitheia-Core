package eu.sqooss.impl.service.webadmin.refactored.servlets;

import eu.sqooss.service.cluster.ClusterNodeService;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.updater.UpdaterService;

public class ProjectsServlet extends AbstractWebadminServlet {
	private MetricActivator compMA;
	private Scheduler sobjSched;
	private UpdaterService sobjUpdater;
	private ClusterNodeService sobjClusterNode;
}
