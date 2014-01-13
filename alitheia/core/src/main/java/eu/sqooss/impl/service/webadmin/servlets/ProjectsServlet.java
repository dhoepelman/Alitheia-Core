package eu.sqooss.impl.service.webadmin.servlets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.prerefactoring.ProjectDeleteJob;
import eu.sqooss.impl.service.webadmin.templates.NullTool;

import com.google.common.collect.ImmutableMap;
import com.sun.org.apache.bcel.internal.generic.NEW;

import eu.sqooss.service.admin.AdminAction;
import eu.sqooss.service.admin.AdminService;
import eu.sqooss.service.admin.actions.UpdateProject;
import eu.sqooss.service.cluster.ClusterNodeService;
import eu.sqooss.service.db.Bug;
import eu.sqooss.service.db.ClusterNode;
import eu.sqooss.service.db.MailMessage;
import eu.sqooss.service.db.ProjectVersion;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.pa.PluginAdmin;
import eu.sqooss.service.pa.PluginInfo;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.scheduler.SchedulerException;
import eu.sqooss.service.updater.Updater;
import eu.sqooss.service.updater.UpdaterService;
import eu.sqooss.service.updater.UpdaterService.UpdaterStage;

public class ProjectsServlet extends AbstractWebadminServlet {
	
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
	private PluginAdmin sobjPA;
	
	private StoredProject selProject;
	
    public ProjectsServlet(VelocityEngine ve,AlitheiaCore core) {
        super(ve, core);
        compMA = core.getMetricActivator();
        sobjSched = core.getScheduler();
        sobjUpdater = core.getUpdater();
        sobjClusterNode = core.getClusterNodeService();
        sobjPA = core.getPluginAdmin();
    }
	
	private void addProject(StoredProject project) {
		// TODO: Add project
	}

    // ---------------------------------------------------------------
    // Remove project
    // ---------------------------------------------------------------	
	private void removeProject(StoredProject project) {
        // Deleting large projects in the foreground is
        // very slow
//        ProjectDeleteJob pdj = new ProjectDeleteJob(sobjCore, selProject);
//        try {
//            sobjSched.enqueue(pdj);
//        } catch (SchedulerException e1) {
//            // TODO: Give error
//        }
	}

	// ---------------------------------------------------------------
    // Trigger an update
    // ---------------------------------------------------------------
	private void triggerUpdate(StoredProject project, String updater) {
	    AdminService as = AlitheiaCore.getInstance().getAdminService();
        AdminAction aa = as.create(UpdateProject.MNEMONIC);
        //aa.addArg("project", selProject.getId());
        //aa.addArg("updater", mnem);
        as.execute(aa);

//        if (aa.hasErrors()) {
//            vc.put("RESULTS", aa.errors());
//        } else { 
//            vc.put("RESULTS", aa.results());
//        }
	}

    // ---------------------------------------------------------------
    // Trigger update on all resources for that project
    // ---------------------------------------------------------------
	private void triggerAllUpdate(StoredProject project) {
        AdminService as = AlitheiaCore.getInstance().getAdminService();
        AdminAction aa = as.create(UpdateProject.MNEMONIC);
        //aa.addArg("project", selProject.getId());
        as.execute(aa);
	}

   // ---------------------------------------------------------------
    // Trigger update on all resources on all projects of a node
    // ---------------------------------------------------------------
	private void triggerAllUpdateNode() {
	    //for (StoredProject project : projectList) {
        //    triggerAllUpdate(project);
        //}
	}

	private void synchPlugin(StoredProject project, String plugin) {
		// TODO: Synch a plugin of a project
	}

	@Override
	public String getPath() {
		return ROOT_PATH;
	}

	@Override
	protected Template render(HttpServletRequest req, VelocityContext vc) {
	    // Set selected project
        String projectId = req.getParameter("REQ_PAR_PROJECT_ID");
        if (isEmpty(projectId))
            selProject = null;
        else
            selProject = sobjDB.findObjectById(StoredProject.class, fromString(projectId));
	    
        // Switch over the URI
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
        
        // Add selected project
        vc.put("selProject", selProject);
        
        // Add installed metrics
        Collection<PluginInfo> installedMetrics = sobjPA.listPlugins();
        vc.put("installedMetrics", installedMetrics);
        
        // The list of projects
        Set<StoredProject> projectList = ClusterNode.thisNode().getProjects();
        vc.put("projectList", projectList);
        
        // Add updaters
        vc.put("updaterStages", UpdaterStage.values());
        if (selProject != null) {
            Set<Updater> updaters = sobjUpdater.getUpdaters(selProject, UpdaterStage.IMPORT);
            for (UpdaterStage stage : UpdaterStage.values()) {
                updaters = sobjUpdater.getUpdaters(selProject, stage);
            }
            
        }
        vc.put("sobjUpdater", sobjUpdater);
        
        // Add ClusterNode service
        vc.put("sobjClusterNode", sobjClusterNode);
        
        // Static classes for some information of projects
        vc.put("ProjectVersion", ProjectVersion.class);
        vc.put("MailMessage", MailMessage.class);
        vc.put("Bug", Bug.class);
        
        return t;
	}
	
	private Template PageAddProject(HttpServletRequest req, VelocityContext vc) {
	    // Load the template
	    Template t = loadTemplate(templates.get(PAGE_ADDPROJECT));
	    return t;
	}

   private Template PageDeleteProject(HttpServletRequest req, VelocityContext vc) {
        
       // Load the template
        Template t = loadTemplate(templates.get(PAGE_DELETEPROJECT));
        
        // Add selected project
        vc.put("selProject", selProject);
        
        return t;
    }
   
    private Template PageViewProject(HttpServletRequest req, VelocityContext vc) {
        
        // Load the template
        Template t = loadTemplate(templates.get(PAGE_VIEWPROJECT));
        
        // Add selected project
        vc.put("selProject", selProject);
        
        return t;
    }
    
    private boolean isEmpty(String string) {
        return string == null || string.equals("");
    }
    
    /**
     * Creates a <code>Long</code> object from the content of the given
     * <code>String</code> object, while handling internally any thrown
     * exception.
     * 
     * @param value the <code>String</code> value
     * 
     * @return The <code>Long</code> value.
     */
    protected static Long fromString (String value) {
        try {
            return (new Long(value));
        }
        catch (NumberFormatException ex){
            return null;
        }
    }
   
}
