package eu.sqooss.test.service.webadmin;

import java.util.Hashtable;

import javax.servlet.http.HttpServletResponse;

import eu.sqooss.impl.service.webadmin.AdminServlet;
import eu.sqooss.impl.service.webadmin.WebadminServiceImpl;

public class WebadminTestCommon {

    static WebadminServiceImpl impl;
    static AdminServlet adminServlet;
    
    public static void setUp() {
        impl = new WebadminServiceImpl();
    }
    
    
    protected HttpServletResponse doFakeGetRequest(String url, Hashtable<String, String> parameters) {
    	// TODO: implement fake get request
    	return null;
    }
    
    protected HttpServletResponse doFakePostRequest(String url, Hashtable<String, String> parameters) {
    	// TODO: implement fakse post request
    	return null;
    }
    
    /**
     * True iff a certain string is in the given response
     */
    protected boolean isInOutput(HttpServletResponse hsr) {
    	// TODO: Implement
    	return false;
    }
}
