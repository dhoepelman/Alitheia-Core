package eu.sqooss.test.service.webadmin.projects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.modules.junit4.PowerMockRunner;

import eu.sqooss.core.AlitheiaCore;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AlitheiaCore.class)
public class AddProjectTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void AddProjectTest() {
        //TODO: Test the adding of a project by its project.properties file
        //TODO: Test the adding of a project by manually entered properties 
        fail();
    }
}
