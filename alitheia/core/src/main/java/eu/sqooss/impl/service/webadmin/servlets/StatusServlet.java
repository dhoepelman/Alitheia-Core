package eu.sqooss.impl.service.webadmin.servlets;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.service.scheduler.Job;
import eu.sqooss.service.scheduler.Scheduler;

public class StatusServlet extends AbstractWebadminServlet {
	public StatusServlet(VelocityEngine ve, AlitheiaCore core) {
		super(ve, core);
		// TODO Auto-generated constructor stub
	}


	private Scheduler sobjSched;

	private int getJobFailStats() {
		// TODO: Return the number of failed jobs
		return -1;
	}


	private int getJobWaitStats() {
		// TODO: Return the number of waiting jobs
		return -1;
	}

	private Job[] getFailedJobs() {
		// TODO: Return the failed jobs
		return null;
	}

	private int getJobRunStats() {
		// TODO: Return the number of running jobs
		return -1;
	}

	private int getThreads() {
		// TODO: Return the number of threads
		return -1;
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
