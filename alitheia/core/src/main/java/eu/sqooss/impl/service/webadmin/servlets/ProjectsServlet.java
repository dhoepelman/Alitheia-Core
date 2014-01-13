package eu.sqooss.impl.service.webadmin.servlets;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.core.AlitheiaCore;
import com.google.common.collect.ImmutableMap;

import eu.sqooss.service.cluster.ClusterNodeService;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.updater.UpdaterService;

public class ProjectsServlet extends AbstractWebadminServlet {
	
    public ProjectsServlet(VelocityEngine ve,AlitheiaCore core) {
		super(ve, core);
	}
	
    private static final String ROOT_PATH = "/projects";
    
    private static final String PAGE_PROJECTSLIST = ROOT_PATH;
    
    private static final String PAGE_ADDPROJECT = ROOT_PATH + "/add";
    
    private static final String PAGE_DELETEPROJECT = ROOT_PATH + "/delete";
    
    private static final String PAGE_VIEWPROJECT = ROOT_PATH + "/view";    
    
    private static final Map<String, String> templates = new ImmutableMap.Builder<String, String>()
            .put(PAGE_PROJECTSLIST, "/projectlist.vm")
            .put(PAGE_ADDPROJECT, "/addproject.vm")
            .put(PAGE_DELETEPROJECT, "/deleteproject.vm")
            .put(PAGE_VIEWPROJECT, "/project.vm")
            .build();
    

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
	    switch(req.getRequestURI()) {
	    case PAGE_PROJECTSLIST:
	        return PageProjectsList(req, vc);
	    case PAGE_ADDPROJECT:
	        return PageAddProject(req, vc);
	    case PAGE_DELETEPROJECT:
	        return PageDeleteProject(req, vc);
	    case PAGE_VIEWPROJECT:
	        return PageViewProject(req, vc);
	    default:
	        getLogger().warn(this.getClass() + " was called with incorrect path " + req.getRequestURI());
	        return null;
	    }
	}
	
	private Template PageProjectsList(HttpServletRequest req, VelocityContext vc) {
        // Load the template
        Template t = loadTemplate(templates.get(PAGE_PROJECTSLIST));
        
        // Which project is selected
        int selProject = 0;
        vc.put("isSelProj", selProject);
        
        // The list of projects
        ArrayList<StoredProject> projectList = new ArrayList<StoredProject>();
        vc.put("projectList", projectList);
        
        
        return t;
	}
	
	private Template PageAddProject(HttpServletRequest req, VelocityContext vc) {
	    // Load the template
	    Template t = loadTemplate(templates.get(PAGE_ADDPROJECT));
	    
	    // TODO: implement this method
	    
	    return t;
	}

   private Template PageDeleteProject(HttpServletRequest req, VelocityContext vc) {
        // Load the template
        Template t = loadTemplate(templates.get(PAGE_DELETEPROJECT));
        
        // TODO: implement this method
        
        return t;
    }
   
    private Template PageViewProject(HttpServletRequest req, VelocityContext vc) {
        // Load the template
        Template t = loadTemplate(templates.get(PAGE_VIEWPROJECT));
        
        // TODO: implement this method
        
        return t;
    }
	
}
