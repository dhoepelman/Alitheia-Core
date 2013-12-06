package eu.sqooss.test.service.webadmin;

import org.junit.BeforeClass;

import eu.sqooss.impl.service.webadmin.AdminServlet;
import eu.sqooss.impl.service.webadmin.WebadminServiceImpl;

public class WebadminTestCommon {

    static WebadminServiceImpl impl;
    static AdminServlet adminServlet;
    
    @BeforeClass
    public static void setUp() {
        impl = new WebadminServiceImpl();
    }
}
