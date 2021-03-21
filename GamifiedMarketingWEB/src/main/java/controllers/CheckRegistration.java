package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import services.UserService;
import entities.User;
import exceptions.CreateProfileException;
import exceptions.CredentialsException;


@WebServlet("/CheckRegistration")
public class CheckRegistration extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "project.clup.services/UserService")
	private UserService userService;
	
    public CheckRegistration() {
        super();
    }
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String firstname = null;
		String lastname = null;
		String username = null;
		String email = null;
		String password = null;
		
		try {
			firstname= request.getParameter("firstname");
			lastname= request.getParameter("lastname");
			username = request.getParameter("username");
			email = request.getParameter("email");
			password = request.getParameter("password");
			if (firstname==null || lastname==null || username == null || email==null || password == null ||
					firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		
		User user = null;
		
		try {
			user = userService.insertNewUser(firstname, lastname, username, email, password);
		} catch (CredentialsException e) {
			//e.printStackTrace();
			response.sendError(HttpServletResponse.SC_CONFLICT, "Could not verify credentials");
			return;
		} catch (CreateProfileException e) {
			//e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Could not create a new profile");
			return;
		}
		
		String path;
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		if (user == null) {
			ctx.setVariable("errorMsg", "The username is already used by another account");
			path = "/registration.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		}
	}
}
