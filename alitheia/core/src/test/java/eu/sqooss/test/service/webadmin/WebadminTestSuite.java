package eu.sqooss.test.service.webadmin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.sqooss.test.service.webadmin.jobs.JobsTest;
import eu.sqooss.test.service.webadmin.misc.LogsTest;
import eu.sqooss.test.service.webadmin.misc.StatusTest;
import eu.sqooss.test.service.webadmin.misc.SystemTest;
import eu.sqooss.test.service.webadmin.plugins.PluginListTest;
import eu.sqooss.test.service.webadmin.plugins.PluginTest;

@RunWith(Suite.class)
@SuiteClasses({
	WebadminServiceTest.class,
	PluginTest.class,
	PluginListTest.class,
	StatusTest.class,
	SystemTest.class,
	LogsTest.class,
	JobsTest.class
})
public class WebadminTestSuite {

}
