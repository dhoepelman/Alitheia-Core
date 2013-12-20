package eu.sqooss.impl.service.webadmin.refactored.servlets;

import eu.sqooss.service.cluster.ClusterNodeService;
import eu.sqooss.service.db.Plugin;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.updater.UpdaterService;

public class ProjectsServlet extends AbstractWebadminServlet {
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
}
