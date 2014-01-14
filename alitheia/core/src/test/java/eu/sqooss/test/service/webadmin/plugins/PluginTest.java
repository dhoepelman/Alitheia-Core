package eu.sqooss.test.service.webadmin.plugins;

import static org.mockito.Mockito.*;

import org.junit.*;

import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
import eu.sqooss.test.service.webadmin.AbstractWebadminServletTest;

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
		when(mockReq.getRequestURI()).thenReturn("/plugins/plugin/action");
		when(mockReq.getMethod()).thenReturn("POST");
		when(mockReq.getParameter("hash")).thenReturn("PLUGINHASH");
		when(mockReq.getParameter("action")).thenReturn("install");

		testee.service(mockReq, mockResp);

		// Verify that the plugin has been installed
		verify(mockPA.installPlugin("PLUGINHASH"));
	}
}
