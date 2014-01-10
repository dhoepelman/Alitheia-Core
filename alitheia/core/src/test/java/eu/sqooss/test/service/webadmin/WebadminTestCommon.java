package eu.sqooss.test.service.webadmin;

import eu.sqooss.impl.service.webadmin.WebadminServiceImpl;

public class WebadminTestCommon {

	static WebadminServiceImpl impl;

	public static void setUp() {
		impl = new WebadminServiceImpl();
	}
}
