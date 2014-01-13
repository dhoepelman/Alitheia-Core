/**
 * 
 */
package eu.sqooss.test.service.webadmin.plugins;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
import eu.sqooss.service.pa.PluginInfo;
import eu.sqooss.test.service.webadmin.AbstractWebadminServletTest;

public class PluginListTest extends AbstractWebadminServletTest{

	private PluginsServlet testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		testee = new PluginsServlet(ve, mockAC);
	}

	@Test
	public void testPluginListNames() throws ServletException, IOException {
		when(mockReq.getRequestURI()).thenReturn("/plugins");
		when(mockReq.getMethod()).thenReturn("GET");

		// We expect these plugins
		PluginInfo p1 = new PluginInfo();
		p1.setPluginName("TestPlugin1");
		p1.setPluginVersion("1.0");
		PluginInfo p2 = new PluginInfo();
		p2.setPluginName("TestPlugin2");
		p2.setPluginVersion("2.0");

		when(mockPA.listPlugins()).thenReturn(Arrays.asList(new PluginInfo[] {p1,p2}));

		// Do the fake request
		testee.service(mockReq, mockResp);
		// Get the output
		String output = getResponseOuput();
		//System.out.println(output);
		// Verify that the 2 plugins are all contained correctly in the output
		assertTrue(output.contains("TestPlugin1"));
		assertTrue(output.contains("1.0"));
		assertTrue(output.contains("TestPlugin2"));
		assertTrue(output.contains("2.0"));
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
