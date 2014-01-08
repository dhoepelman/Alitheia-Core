package eu.sqooss.test.service.webadmin.projects;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.verify;
import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.AdminServlet;
import eu.sqooss.impl.service.webadmin.ProjectsView;
import eu.sqooss.service.admin.AdminAction;
import eu.sqooss.service.admin.AdminService;
import eu.sqooss.service.admin.actions.AddProject;
import eu.sqooss.service.db.DBService;
import eu.sqooss.service.db.StoredProject;
import eu.sqooss.service.logging.Logger;
import eu.sqooss.service.webadmin.WebadminService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AlitheiaCore.class,StoredProject.class})
public class AddProjectTest {

	private AdminServlet testee;
	/**
	 * A fake empty POST request
	 */
	private HttpServletRequest mockReq;
	
	private AlitheiaCore mockCore;
	private VelocityEngine mockVE;
	
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    	// Setup up core and DB mock
    	PowerMockito.mockStatic(AlitheiaCore.class);
    	mockCore = mock(AlitheiaCore.class, Mockito.RETURNS_DEEP_STUBS);
    	when(AlitheiaCore.getInstance()).thenReturn(mockCore);
    	// Set up Velocity mock
    	mockVE = mock(VelocityEngine.class);
    	Template mockT = mock(Template.class);
    	when(mockVE.getTemplate(Mockito.anyString())).thenReturn(mockT);
    	// Set up AdminServlet and request mock
    	testee = new AdminServlet(mock(BundleContext.class), mock(WebadminService.class), mock(Logger.class), mockVE);
    	mockReq = mock(HttpServletRequest.class);
    	when(mockReq.getMethod()).thenReturn("POST");
    }
    
    @Test
    public void AddProjectTestManual() throws Exception {
        // Mock dependencies
    	PowerMockito.mockStatic(StoredProject.class);
    	AdminService mockAS = mock(AdminService.class);
    	AdminAction mockAA = mock(AdminAction.class);
    	
    	when(mockCore.getAdminService()).thenReturn(mockAS);
    	when(mockAS.create(AddProject.MNEMONIC)).thenReturn(mockAA);
    	
    	// Test data
    	Map<String,String> data = new HashMap<String,String>();
    	data.put("scm", "svn-file:///svn");
    	data.put("name", "Test");
    	data.put("bts", "bugzilla-xml:///bugzilla-xml");
    	data.put("mail", "maildir:///mail");
    	data.put("web", "http://example.org");
    	
    	when(mockReq.getPathInfo()).thenReturn("/projects");    
    	when(mockReq.getParameter("reqAction")).thenReturn("conAddProject");
    	when(mockReq.getParameter("projectSCM")).thenReturn(data.get("scm"));
    	when(mockReq.getParameter("projectName")).thenReturn(data.get("name"));
    	when(mockReq.getParameter("projectBL")).thenReturn(data.get("bts"));
    	when(mockReq.getParameter("projectML")).thenReturn(data.get("mail"));
    	when(mockReq.getParameter("projectHomepage")).thenReturn(data.get("web"));
    	
    	// Let ProjectsView process the fake request
    	//testee.service(mockReq,mock(HttpServletResponse.class));
    	ProjectsView.render(mockReq);
    	
    	// Verification
    	// Make sure an addproject admin action was created with the right arguments and executed
        verify(mockAS).create(AddProject.MNEMONIC);
        for(Map.Entry<String, String> pair : data.entrySet()) {
        	verify(mockAA).addArg(pair.getKey(), pair.getValue());
        }
    	verify(mockAS).execute(mockAA);
    }
    
    @Test
    public void AddProjectTestPropertiesFile() {
    	//TODO: Test the adding of a project by its project.properties file
    	fail();
    }
}
