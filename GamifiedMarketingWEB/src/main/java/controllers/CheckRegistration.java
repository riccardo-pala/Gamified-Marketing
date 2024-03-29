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
	
	@EJB(name = "services/UserService")
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

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if (firstname==null || lastname==null || username == null || email==null || password == null ||
				firstname.isBlank() || lastname.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
			ctx.setVariable("errorMsg", "Missing or empty credential value.");
			templateEngine.process("/registration.html", ctx, response.getWriter());
			return;
		}

		User user = null;
		
		try {
			user = userService.insertNewUser(firstname, lastname, username, email, password);
		} catch (CredentialsException | CreateProfileException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}

		String path;
		
		if (user == null)
			path = "/registration.html";
		else
			path = "/index.html";
		
		templateEngine.process(path, ctx, response.getWriter());
	}
}
