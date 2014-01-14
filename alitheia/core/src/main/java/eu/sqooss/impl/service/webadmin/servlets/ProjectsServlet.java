package eu.sqooss.impl.service.webadmin.servlets;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.common.collect.ImmutableMap;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.ProjectDeleteJob;
import eu.sqooss.impl.service.webadmin.servlets.exceptions.PageNotFoundException;
import eu.sqooss.service.abstractmetric.AlitheiaPlugin;
import eu.sqooss.service.admin.AdminAction;
import eu.sqooss.service.admin.AdminService;
import eu.sqooss.service.admin.actions.AddProject;
import eu.sqooss.service.admin.actions.UpdateProject;
import eu.sqooss.service.cluster.ClusterNodeService;
import eu.sqooss.service.db.*;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.pa.PluginAdmin;
import eu.sqooss.service.pa.PluginInfo;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.scheduler.SchedulerException;
import eu.sqooss.service.updater.UpdaterService;
import eu.sqooss.service.updater.UpdaterService.UpdaterStage;

public class ProjectsServlet extends AbstractWebadminServlet {

	private static final String ROOT_PATH = "/projects";

	private static final String PAGE_PROJECTSLIST = ROOT_PATH;

	private static final String PAGE_ADDPROJECT = ROOT_PATH + "/add";
	private static final String PAGE_DELETEPROJECT = ROOT_PATH + "/delete";
	private static final String PAGE_VIEWPROJECT = ROOT_PATH + "/view";
	private static final String ACTION_PROJECT = ROOT_PATH + "/action";

	private static final Map<String, String> templates = new ImmutableMap.Builder<String, String>()
			.put(PAGE_PROJECTSLIST, "/projectlist.vm")
			.put(PAGE_ADDPROJECT, "/addproject.vm")
			.put(PAGE_DELETEPROJECT, "/deleteproject.vm")
			.put(PAGE_VIEWPROJECT, "/project.vm")
			.build();

	/**
	 * Valid actions for a project. These are the valid values for the "action"
	 * parameter (when lowercase)
	 */
	private enum PROJECT_ACTIONS {
		INSTALLBYFORM,
		INSTALLBYPROPERTIES,
		DELETE,
		SYNCHRONIZE,
		TRIGGERUPDATE,
		TRIGGERALLUPDATE,
		TRIGGERALLUPDATENODE,
		INVALID;
	}

	private final Scheduler sobjSched;
	private final UpdaterService sobjUpdater;
	private final ClusterNodeService sobjClusterNode;
	private final PluginAdmin sobjPA;
	private final MetricActivator sobjMA;
	private AdminService sobjAdminService;

	private StoredProject selProject;

	public ProjectsServlet(VelocityEngine ve, AlitheiaCore core) {
		super(ve, core);
		sobjSched = core.getScheduler();
		sobjMA = core.getMetricActivator();
		sobjUpdater = core.getUpdater();
		sobjClusterNode = core.getClusterNodeService();
		sobjPA = core.getPluginAdmin();
		sobjAdminService = core.getAdminService();
	}

	@Override
	public String getPath() {
		return ROOT_PATH;
	}

	@Override
	protected Template render(HttpServletRequest req, VelocityContext vc) throws PageNotFoundException {
		// Set selected project
		String projectId = req.getParameter("REQ_PAR_PROJECT_ID");
		if (isEmpty(projectId)) {
			selProject = null;
		} else {
			selProject = sobjDB.findObjectById(StoredProject.class, fromString(projectId));
		}

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
		case ACTION_PROJECT:
			return renderAction(req, vc);
		default:
			throw new PageNotFoundException();
		}
	}

	private Template renderAction(HttpServletRequest req, VelocityContext vc) {
		// Convert the action argument to the enum and switch on it
		PROJECT_ACTIONS action;
		try {
			action = Enum.valueOf(PROJECT_ACTIONS.class, req.getParameter("action").toUpperCase());
		}
		catch(IllegalArgumentException | NullPointerException e) {
			action = PROJECT_ACTIONS.INVALID;
		}
		switch (action) {
		case INSTALLBYFORM:
			return installProjectByForm(req, vc);
		case INSTALLBYPROPERTIES:
			return installProjectByProperties(req, vc);
		case DELETE:
			return deleteProject(req, vc);
		case SYNCHRONIZE:
			return synchronizePlugin(req, vc);
		case TRIGGERUPDATE:
			return triggerUpdate(req, vc);
		case TRIGGERALLUPDATE:
			return triggerAllUpdate(req, vc);
		case TRIGGERALLUPDATENODE:
			return triggerAllUpdateNode(req, vc);
		default:
			return makeErrorMsg(vc, "No or invalid action ");
		}
	}

	private Template installProjectByForm(HttpServletRequest req,
			VelocityContext vc) {
		// Install a project by a filled form
		AdminAction aa = sobjAdminService.create(AddProject.MNEMONIC);
		aa.addArg("scm", req.getParameter("REQ_PAR_PRJ_CODE"));
		aa.addArg("name", req.getParameter("REQ_PAR_PRJ_NAME"));
		aa.addArg("bts", req.getParameter("REQ_PAR_PRJ_BUG"));
		aa.addArg("mail", req.getParameter("REQ_PAR_PRJ_MAIL"));
		aa.addArg("web", req.getParameter("REQ_PAR_PRJ_WEB"));
		sobjAdminService.execute(aa);

		// Print result
		if (aa.hasErrors())
			return makeErrorMsg(vc, "A problem occurred when installing the project");
		else
			return makeSuccessMsg(vc, "The project is installed succesfully");
	}

	private Template installProjectByProperties(HttpServletRequest req,
			VelocityContext vc) {
		// Install a project by a project.properties file
		AdminAction aa = sobjAdminService.create(AddProject.MNEMONIC);
		aa.addArg("dir", req.getParameter("properties"));
		sobjAdminService.execute(aa);

		// Print result
		if (aa.hasErrors())
			return makeErrorMsg(vc, "A problem is occured when installing the project");
		else
			return makeSuccessMsg(vc, "The project is installed succesfully");
	}

	private Template deleteProject(HttpServletRequest req, VelocityContext vc) {
		if (selProject != null) {
			// Deleting large projects in the foreground is
			// very slow
			ProjectDeleteJob pdj = new ProjectDeleteJob(sobjDB, sobjPA, selProject);
			try {
				sobjSched.enqueue(pdj);
			} catch (SchedulerException e1) {
				return makeErrorMsg(vc, getTranslation().error("e0034"));
			}
			return makeSuccessMsg(vc, "A delete project job is enqueued in the scheduler");
		} else
			return makeErrorMsg(vc, getTranslation().error("e0034"));
	}

	private Template synchronizePlugin(HttpServletRequest req,
			VelocityContext vc) {
		PluginInfo pInfo = sobjPA.getPluginInfo(req.getParameter("REQ_PAR_SYNC_PLUGIN"));
		if (pInfo != null) {
			AlitheiaPlugin pObj = sobjPA.getPlugin(pInfo);
			if (pObj != null) {
				sobjMA.syncMetric(pObj, selProject);
				sobjLogger.debug("Syncronise plugin (" + pObj.getName()
						+ ") on project (" + selProject.getName() + ").");
				return makeSuccessMsg(vc, "Jobs are scheduled to run the plugin over all projects");
			}
		}
		return makeErrorMsg(vc, "Could not find the plugin");
	}

	private Template triggerUpdate(HttpServletRequest req,
			VelocityContext vc) {
		// Trigger an updater on a project
		AdminAction aa = sobjAdminService.create(UpdateProject.MNEMONIC);
		aa.addArg("project", selProject.getId());
		aa.addArg("updater", req.getParameter("reqUpd"));
		sobjAdminService.execute(aa);

		// Print result
		if (aa.hasErrors())
			return makeErrorMsg(vc, "Could not trigger the update");
		else
			return makeSuccessMsg(vc, "Succesfully triggered the update");
	}

	private Template triggerAllUpdate(HttpServletRequest req,
			VelocityContext vc) {

		// Trigger all updaters on a project
		AdminAction aa = sobjAdminService.create(UpdateProject.MNEMONIC);
		aa.addArg("project", selProject.getId());
		sobjAdminService.execute(aa);

		// Print result
		if (aa.hasErrors())
			return makeErrorMsg(vc, "A problem occurred when triggering the updates");
		else
			return makeSuccessMsg(vc, "Succesfully triggered the updates");
	}

	private Template triggerAllUpdateNode(HttpServletRequest req, VelocityContext vc) {
		Set<StoredProject> projectList = sobjClusterNode.getClusterNode().getProjects();
		boolean hasErrors = false;

		if (projectList == null || projectList.isEmpty())
			return makeErrorMsg(vc, "Triggering updaters failed because there are no projects");
		else {
			// Trigger all updaters on the node
			for (StoredProject project : projectList) {

				// Execute updaters
				AdminAction aa = sobjAdminService.create(UpdateProject.MNEMONIC);
				aa.addArg("project", selProject.getId());
				sobjAdminService.execute(aa);

				// Merge the new results with the old ones
				if (aa.hasErrors()) {
					hasErrors = true;
				}
			}

			// Print result
			if (hasErrors)
				return makeErrorMsg(vc, "A problem occured when triggering the updates");
			else
				return makeSuccessMsg(vc, "Succesfully triggered the updates");
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
