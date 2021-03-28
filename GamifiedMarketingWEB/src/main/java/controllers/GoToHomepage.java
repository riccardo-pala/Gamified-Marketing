package controllers;

import java.io.IOException;


import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;



import entities.Product;
import entities.Questionnaire;
import exceptions.BadRetrievalException;
import services.ProductService;
import services.QuestionnaireService;


@WebServlet("/GoToHomepage")
public class GoToHomepage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
    
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	@EJB(name = "services/ProductService")
	private ProductService productService;
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
    public GoToHomepage() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginpath = getServletContext().getContextPath() + "/index.html";
		
		HttpSession session = request.getSession();
		
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		Questionnaire qotd = null;
		
		try {
			qotd = questionnaireService.getQuestionnaireOfTheDay();
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}
		
		if (qotd != null ) {
			Product potd = qotd.getProduct();
			String imgStr = Base64.encodeBase64String(potd.getPhoto());
			ctx.setVariable("potd", potd);
			ctx.setVariable("img", imgStr);
		}
		
		templateEngine.process("/WEB-INF/homepage.html", ctx, response.getWriter());		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}