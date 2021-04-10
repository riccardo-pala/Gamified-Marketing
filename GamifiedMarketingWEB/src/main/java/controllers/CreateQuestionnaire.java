package controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Product;
import entities.Question;
import exceptions.BadRetrievalException;
import services.ProductService;
import services.QuestionService;
import services.QuestionnaireService;

/**
 * Servlet implementation class CreateQuestionnaire
 */
@WebServlet("/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name="services/ProductService")
	private ProductService productService;
	
	@EJB(name="services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	@EJB(name="services/QuestionService")
	private QuestionService questionService;
    
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
    public CreateQuestionnaire() {
        super();
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
		int productid = Integer.parseInt(request.getParameter("productid"));
		Date questdate = Date.valueOf(request.getParameter("productdate"));
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		Product p = null;
		
		try {
			p = productService.getProductById(productid);
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/questionnairecreation.html", ctx, response.getWriter());
			return;
		}
		
		int x = 1;
		
		while(request.getParameter("quest" + x + "") != null) {
			Question q = new Question(request.getParameter("quest" + x + ""),1);
			questions.add(q);
			x++;
		}
		
		try {
			questionnaireService.createQuestionnaire(p.getId(), questdate, questions);
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/questionnairecreation.html", ctx, response.getWriter());
			return;
		}
		
		String path = getServletContext().getContextPath() + "/GoToAdminHomepage";
		response.sendRedirect(path);	
	}
}
