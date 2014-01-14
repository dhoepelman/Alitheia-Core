package eu.sqooss.test.service.webadmin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.sqooss.test.service.webadmin.plugins.PluginListTest;
import eu.sqooss.test.service.webadmin.plugins.PluginTest;

@RunWith(Suite.class)
@SuiteClasses({
	WebadminServiceTest.class,
	PluginTest.class,
	PluginListTest.class
})
public class WebadminTestSuite {

}
