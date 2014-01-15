package eu.sqooss.test.service.webadmin.misc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import eu.sqooss.impl.service.webadmin.servlets.LogsServlet;
import eu.sqooss.impl.service.webadmin.servlets.PluginsServlet;
import eu.sqooss.impl.service.webadmin.servlets.PropertiesServlet;
import eu.sqooss.impl.service.webadmin.servlets.StatusServlet;
import eu.sqooss.service.db.PluginConfiguration;
import eu.sqooss.service.logging.LogManager;
import eu.sqooss.service.pa.PluginAdmin;
import eu.sqooss.service.pa.PluginInfo;
import eu.sqooss.service.scheduler.Scheduler;
import eu.sqooss.service.scheduler.SchedulerStats;
import eu.sqooss.test.service.webadmin.AbstractWebadminServletTest;

@RunWith(PowerMockRunner.class)
public class PropertiesTest extends AbstractWebadminServletTest {

    private PropertiesServlet testee;
    @Mock private PluginAdmin mockPA;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        
        // Mock pluginadmin
        when(mockAC.getPluginAdmin()).thenReturn(mockPA);
        
        // Initialize servlet
        testee = new PropertiesServlet(ve, mockAC);
    }
    
    // Test the displaying of logs
    @Test
    public void testDisplay() throws ServletException, IOException {
        when(mockReq.getRequestURI()).thenReturn("/properties");
        when(mockReq.getMethod()).thenReturn("GET");
        
        PluginInfo plugin = new PluginInfo();
        plugin.setPluginName("Plugin1");
        Set<PluginConfiguration> conf = new HashSet<PluginConfiguration>();
        PluginConfiguration c = new PluginConfiguration();
        c.setId(1);
        c.setName("confName1");
        c.setMsg("confMsg1");
        c.setType("INTEGER");
        c.setValue("confValue1");
        conf.add(c);
        plugin.setPluginConfiguration(conf);
        when(mockReq.getParameter("propertyId")).thenReturn("1");
        when(mockReq.getParameter("hash")).thenReturn("thisishashcode");
        when(mockPA.getPluginInfo("thisishashcode")).thenReturn(plugin);

        // Do the fake request
        testee.service(mockReq, mockResp);

        // Get the output
        String output = getResponseOutput();
        
        // Check whether the entries occur in the output
        assertTrue(output.contains("Plugin1"));
        assertTrue(output.contains("confName1"));
        assertTrue(output.contains("confMsg1"));
        assertTrue(output.contains("confValue1"));
        assertTrue(output.contains("selected"));
    }
    
}
