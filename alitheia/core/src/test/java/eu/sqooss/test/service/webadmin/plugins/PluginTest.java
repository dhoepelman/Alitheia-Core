package eu.sqooss.test.service.webadmin.plugins;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
import eu.sqooss.service.abstractmetric.AlitheiaPlugin;
import eu.sqooss.service.pa.PluginInfo;
import eu.sqooss.test.service.webadmin.AbstractWebadminServletTest;


@RunWith(PowerMockRunner.class)
public class PluginTest extends AbstractWebadminServletTest {

	private PluginsServlet testee;

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
	public void testPluginInstall() throws Exception {
		final String hash = "PLUGINHASH";

		// Make the mock request
		when(mockReq.getRequestURI()).thenReturn("/plugins/plugin/action");
		when(mockReq.getMethod()).thenReturn("POST");
		when(mockReq.getParameter("hash")).thenReturn(hash);
		when(mockReq.getParameter("action")).thenReturn("install");

		// Create the mock plugin
		PluginInfo p = mock(PluginInfo.class);
		when(p.isInstalled()).thenReturn(false);
		when(p.getHashcode()).thenReturn(hash);
		when(mockPA.getPluginInfo(hash)).thenReturn(p);

		testee.service(mockReq, mockResp);

		// Verify that the plugin has been installed
		verify(mockPA).installPlugin(hash);
	}

	@Test
	public void testPluginUninstall() throws ServletException, IOException {
		final String hash = "PLUGINHASH";

		// Make the mock request
		when(mockReq.getRequestURI()).thenReturn("/plugins/plugin/action");
		when(mockReq.getMethod()).thenReturn("POST");
		when(mockReq.getParameter("hash")).thenReturn(hash);
		when(mockReq.getParameter("action")).thenReturn("uninstall");

		// Create the mock plugin
		PluginInfo p = mock(PluginInfo.class);
		when(p.isInstalled()).thenReturn(true);
		when(p.getHashcode()).thenReturn(hash);
		when(mockPA.getPluginInfo(hash)).thenReturn(p);

		testee.service(mockReq, mockResp);

		// Verify that the plugin has been uninstalled
		verify(mockPA).uninstallPlugin(hash);
	}

	@Test
	public void testPluginSynchronize() throws Exception {
		final String hash = "PLUGINHASH";

		// Make the mock request
		when(mockReq.getRequestURI()).thenReturn("/plugins/plugin/action");
		when(mockReq.getMethod()).thenReturn("POST");
		when(mockReq.getParameter("hash")).thenReturn(hash);
		when(mockReq.getParameter("action")).thenReturn("synchronize");

		// Create the mock plugin
		PluginInfo p = mock(PluginInfo.class);
		when(p.isInstalled()).thenReturn(true);
		when(p.getHashcode()).thenReturn(hash);
		when(mockPA.getPluginInfo(hash)).thenReturn(p);
		AlitheiaPlugin p2 = mock(AlitheiaPlugin.class);
		when(mockPA.getPlugin(p)).thenReturn(p2);

		testee.service(mockReq, mockResp);

		// Verify that the plugin has been synchronized
		verify(mockMA).syncMetrics(eq(p2));
	}
}
