package eu.sqooss.test.service.webadmin.projects;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.*;

import eu.sqooss.core.AlitheiaCore;
import eu.sqooss.impl.service.webadmin.AbstractView;
import eu.sqooss.service.admin.AdminAction;
import eu.sqooss.service.admin.AdminService;
import eu.sqooss.service.admin.actions.AddProject;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AlitheiaCore.class})
public class AddProjectTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void AddProjectTestManual() {
        //TODO: Test the adding of a project by manually entered properties 
    	// Mock dependencies
    	PowerMockito.mockStatic(AlitheiaCore.class);
    	PowerMockito.mockStatic(AbstractView.class);
    	AdminService mockAS = mock(AdminService.class);
    	AdminAction mockAA = mock(AdminAction.class);
    	VelocityContext mockVC = mock(VelocityContext.class);
    	when(AlitheiaCore.getInstance().getAdminService()).thenReturn(mockAS);
    	when(mockAS.create(AddProject.MNEMONIC)).thenReturn(mockAA);
    	
    	// Test data
    	Map<String,String> data = new HashMap<String,String>();
    	data.put("scm", "svn-file:///svn");
    	data.put("name", "Test");
    	data.put("bts", "bugzilla-xml:///bugzilla-xml");
    	data.put("mail", "maildir:///mail");
    	data.put("web", "http://example.org");
    	
    	
    	// Verification
        fail();
    }
    
    @Test
    public void AddProjectTestPropertiesFile() {
    	//TODO: Test the adding of a project by its project.properties file
    	fail();
    }
}
