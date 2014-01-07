package eu.sqooss.impl.service.webadmin.servlets;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class OptionsServlet extends AbstractWebadminServlet {

	public OptionsServlet(VelocityEngine ve) {
		super(ve);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This String is used to represent the current "message of the
	 * day" available in the Alitheia Core
	 */
	private String messageOfTheDay = null;

	@Override
	public List<String> getPaths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Template render(HttpServletRequest req, VelocityContext vc) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves the "message of the day" String
	 *
	 * @return the message as a String
	 */
	public String getMessageOfTheDay() {
		return messageOfTheDay;
	}

	/**
	 * Sets the "message of the day" String
	 *
	 * @param s The text to be used as the "message of the day"
	 */
	public void setMessageOfTheDay(String s) {
		messageOfTheDay = s;
	}

}
