package eu.sqooss.test.service.webadmin.plugins;

import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
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

		when(mockReq.getRequestURI()).thenReturn("/plugins/plugin/action");
		when(mockReq.getMethod()).thenReturn("POST");
		when(mockReq.getParameter("hash")).thenReturn(hash);
		when(mockReq.getParameter("action")).thenReturn("install");

		PluginInfo p = mock(PluginInfo.class);
		when(p.isInstalled()).thenReturn(false);
		when(p.getHashcode()).thenReturn(hash);
		when(mockPA.getPluginInfo(hash)).thenReturn(p);

		testee.service(mockReq, mockResp);

		// Verify that the plugin has been installed
		verify(mockPA).installPlugin(hash);
	}
}
