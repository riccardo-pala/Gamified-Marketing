package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
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

import entities.User;
import exceptions.CredentialsException;
import services.UserService;


@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private TemplateEngine templateEngine;
	
	@EJB(name = "services/UserService")
	private UserService userService;

	public CheckLogin() {
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
		
		String username = null;
		String password = null;

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	
		username = request.getParameter("username");
		password = request.getParameter("password");
		
		String path = "/index.html";
		
		if (username == null || password == null ||
				username.isEmpty() || password.isEmpty()) {
			ctx.setVariable("errorMsg", "Missing credential values!");
			templateEngine.process(path, ctx, response.getWriter());
		}
		
		User user = null;
		
		try {
			user = userService.checkCredentials(username, password);
		} catch (NonUniqueResultException | CredentialsException e) {
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		if (user == null) {
			ctx.setVariable("errorMsg", "Incorrect username or password");
			templateEngine.process(path, ctx, response.getWriter());
		} 
		else if(user.getIsBanned()) {
			ctx.setVariable("errorMsg", "You are not allowed to log in since you were banned from this site");
			templateEngine.process(path, ctx, response.getWriter());
		}
		else if (!user.getIsAdmin()) {
			request.getSession().setAttribute("user", user);
			path = getServletContext().getContextPath() + "/GoToHomepage";
			response.sendRedirect(path);
		}
		else {
			request.getSession().setAttribute("user", user);
			path = getServletContext().getContextPath() + "/GoToAdminHomepage";
			response.sendRedirect(path);	
		}
	}

}
