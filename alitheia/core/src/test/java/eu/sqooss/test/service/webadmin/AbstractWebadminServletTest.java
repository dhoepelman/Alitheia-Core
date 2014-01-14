package eu.sqooss.test.service.webadmin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.After;
import org.junit.Before;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.service.db.DBService;
import eu.sqooss.service.logging.LogManager;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.metricactivator.MetricActivator;
import eu.sqooss.service.pa.PluginAdmin;

/**
 * Sets up things needed for all Servlet test and contains tests all Servlets should pass
 */
public abstract class AbstractWebadminServletTest {
	protected AlitheiaCore mockAC;
	protected HttpServletRequest mockReq;
	protected HttpServletResponse mockResp;

	/**
	 * Output of the response
	 */
	private StringWriter responseOutput;

	// All the dependencies (that are used in servlets)
	protected Logger mockLog;
	protected DBService mockDB;
	protected PluginAdmin mockPA;
	protected MetricActivator mockMA;

	/**
	 * Real VelocityEngine
	 */
	protected VelocityEngine ve;

	@Before
	public void setUp() throws Exception {
		ve = createVelocity();
		mockReq = mock(HttpServletRequest.class);
		mockResp = mock(HttpServletResponse.class);
		mockAC = mock(AlitheiaCore.class);

		// Create a byte output stream for the response so the response can be verified
		responseOutput = new StringWriter();
		//responseOutput = new StubServletOutputStream();
		//when(mockResp.getOutputStream()).thenReturn(responseOutput);
		when(mockResp.getWriter()).thenReturn(new PrintWriter(responseOutput));

		// Mock logger
		mockLog = mock(Logger.class);
		LogManager mockLM = mock(LogManager.class);
		when(mockAC.getLogManager()).thenReturn(mockLM);
		when(mockLM.createLogger(Logger.NAME_SQOOSS_WEBADMIN)).thenReturn(mockLog);
		//Logger real = new LoggerImpl(Logger.NAME_SQOOSS_WEBADMIN);
		//when(mockLM.createLogger(Logger.NAME_SQOOSS_WEBADMIN)).thenReturn(real);
		// Mock DB
		mockDB = mock(DBService.class);
		when(mockAC.getDBService()).thenReturn(mockDB);
		// Mock PluginAdmin
		mockPA = mock(PluginAdmin.class);
		when(mockAC.getPluginAdmin()).thenReturn(mockPA);
		// Mock MetricActivator
		mockMA = mock(MetricActivator.class);
		when(mockAC.getMetricActivator()).thenReturn(mockMA);
	}

	/**
	 * Verify that every servlet created and commited a DB transaction
	 */
	@After
	public void verifyDBTransaction() {
		//verify(mockDB).startDBSession();
		//verify(mockDB).commitDBSession();
	}

	/**
	 * Get the output of the mock http response
	 */
	protected String getResponseOuput() {
		//return responseOutput.getOutput();
		return responseOutput.toString();
	}

	private VelocityEngine createVelocity() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty("runtime.log.logsystem.class",
				"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		ve.setProperty("runtime.log.logsystem.log4j.category",
				Logger.NAME_SQOOSS_WEBADMIN);
		String resourceLoader = "classpath";
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, resourceLoader);
		ve.setProperty(resourceLoader + "." + RuntimeConstants.RESOURCE_LOADER + ".class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		return ve;
	}

	/**
	 * Basic removal of HTML and whitespace suitable for testing if output contains something
	 */
	protected static String stripHTMLandWhitespace(String output) {
		return output.replaceAll("\\<[^>]+>","").replaceAll("\\s+","");
	}
}
