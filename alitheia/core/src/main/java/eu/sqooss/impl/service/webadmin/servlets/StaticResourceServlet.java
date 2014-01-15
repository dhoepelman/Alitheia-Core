package eu.sqooss.impl.service.webadmin.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableMap;


public class StaticResourceServlet extends HttpServlet {

	private final String default_page;
	private static final String resourceLocation = "/webadmin/statics";
	
	private final Map<String, ResourceFile> staticContentMap =
			new ImmutableMap.Builder<String, ResourceFile>()
			.put("/screen.css", new ResourceFile(resourceLocation + "/screen.css", "text/css"))
			.put("/webadmin.css", new ResourceFile(resourceLocation + "/webadmin.css", "text/css"))
			.put("/sqo-oss.png", new ResourceFile(resourceLocation + "/sqo-oss.png", "image/png"))
			.put("/queue.png", new ResourceFile(resourceLocation + "/queue.png", "image/png"))
			.put("/uptime.png", new ResourceFile(resourceLocation + "/uptime.png", "image/png"))
			.put("/greyBack.jpg", new ResourceFile(resourceLocation + "/greyBack.jpg", "image/x-jpg"))
			.put("/projects.png", new ResourceFile(resourceLocation + "/projects.png", "image/png"))
			.put("/logs.png", new ResourceFile(resourceLocation + "/logs.png", "image/png"))
			.put("/metrics.png", new ResourceFile(resourceLocation + "/metrics.png", "image/png"))
			.put("/gear.png", new ResourceFile(resourceLocation + "/gear.png", "image/png"))
			.put("/header-repeat.png", new ResourceFile(resourceLocation + "/header-repeat.png", "image/png"))
			.put("/add_user.png", new ResourceFile(resourceLocation + "/add_user.png", "image/png"))
			.put("/edit.png", new ResourceFile(resourceLocation + "/edit.png", "image/png"))
			.put("/jobs.png", new ResourceFile(resourceLocation + "/jobs.png", "image/png"))
			.put("/rules.png", new ResourceFile(resourceLocation + "/rules.png", "image/png"))
			.build();

	public StaticResourceServlet(String default_page) {
		this.default_page = default_page;
	}



	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = req.getPathInfo();
		// Check if path is / and redirect
		if(path.equals("/")) {
			resp.sendRedirect(default_page);
		} else if(staticContentMap.containsKey(path)) {
			ResourceFile res = staticContentMap.get(path);
			sendResource(resp, res.filepath, res.mimetype);
		} else {
			// not found
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
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
	public Collection<String> getResourceList() {
		return staticContentMap.keySet();
	}

	private class ResourceFile {
		public final String filepath;
		public final String mimetype;

		public ResourceFile(String filepath, String mimetype) {
			this.filepath = filepath;
			this.mimetype = mimetype;
		}
	}
}
