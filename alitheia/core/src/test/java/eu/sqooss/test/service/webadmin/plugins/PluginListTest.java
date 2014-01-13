/**
 * 
 */
package eu.sqooss.test.service.webadmin.plugins;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
import eu.sqooss.service.logging.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AlitheiaCore.class)
public class PluginListTest {

	private PluginsServlet testee;
	private VelocityEngine ve;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ve = new VelocityEngine();
		testee = new PluginsServlet(ve, null);


	}

	// TODO: test if the lists of plugins contains all plugins
	@Test
	public void testPluginListNames() {
		// Mock static AlitheiaCore methods
		PowerMockito.mockStatic(AlitheiaCore.class);
		AlitheiaCore mockAC = mock(AlitheiaCore.class);
		when(AlitheiaCore.getInstance()).thenReturn(new AlitheiaCore(null));
		//LogManager mockLM = mock(LogManager.class);
		//when(mockAC.getLogManager()).thenReturn(mockLM);
		Logger mockLog = mock(Logger.class);
		when(mockAC.getLogManager().createLogger(Logger.NAME_SQOOSS_WEBADMIN)).thenReturn(mockLog);
	}

	// TODO: test if registered/installed status is correct
	@Test
	public void testPluginListInstalledStatus() {
		fail();
	}

	// TODO: Test if plugin lists contains all activators for a plugin
	@Test
	public void testPluginListActivators() {
		fail();
	}

}
