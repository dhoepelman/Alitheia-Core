package eu.sqooss.impl.service.webadmin.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableMap;


public class StaticResourceServlet extends HttpServlet {

    private final Map<String, String> staticContentMap = 
            new ImmutableMap.Builder<String, String>()
                .put("/screen.css", "text/css")
                .put("/webadmin.css", "text/css")
                .put("/sqo-oss.png", "image/x-png")
                .put("/queue.png", "image/x-png")
                .put("/uptime.png", "image/x-png")
                .put("/greyBack.jpg", "image/x-jpg")
                .put("/projects.png", "image/x-png")
                .put("/logs.png", "image/x-png")
                .put("/metrics.png", "image/x-png")
                .put("/gear.png", "image/x-png")
                .put("/header-repeat.png", "image/x-png")
                .put("/add_user.png", "image/x-png")
                .put("/edit.png", "image/x-png")
                .put("/jobs.png", "image/x-png")
                .put("/rules.png", "image/x-png")
                .build();
    
    public StaticResourceServlet() {
        super();
    }
    
    /**
     * Sends a resource (stored in the jar file) as a response. The mime-type
     * is set to @p mimeType . The @p path to the resource should start
     * with a / .
     *
     * Test cases:
     *   - null mimetype, null path, bad path, relative path, path not found,
     *   - null response
     *
     * TODO: How to simulate conditions that will cause IOException
     */
    protected void sendResource(HttpServletResponse response, String resource, String contentType)
            throws ServletException, IOException {

        InputStream istream = getClass().getResourceAsStream(resource);
        if ( istream == null )
            throw new IOException("Path not found: " + resource);

        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        response.setContentType(contentType);
        ServletOutputStream ostream = response.getOutputStream();
        while ((bytesRead = istream.read(buffer)) > 0) {
            ostream.write(buffer, 0, bytesRead);
        }
    }
    
    /**
     * @return A list with all resources
     */
    public List<String> getResourceList() {
		return new ArrayList<String>(staticContentMap.keySet());
	}
}
