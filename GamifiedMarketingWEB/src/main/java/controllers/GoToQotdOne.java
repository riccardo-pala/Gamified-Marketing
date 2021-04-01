package controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Product;
import entities.Question;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRetrievalException;
import services.AccessService;
import services.ProductService;
import services.QuestionService;
import services.QuestionnaireService;


@WebServlet("/GoToQotdOne")
public class GoToQotdOne extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
    
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService questionnaireService;

	@EJB(name = "services/QuestionService")
	private QuestionService questionService;
	
	@EJB(name = "services/AccessService")
	private AccessService accessService;
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
    public GoToQotdOne() {
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
		
		try{
			User u = (User) session.getAttribute("user");
			Questionnaire q = questionnaireService.getQuestionnaireOfTheDay();
			accessService.insertAccess(u.getId(), q.getId());
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}
		
		Questionnaire qotd = null;
		
		try {
			qotd = questionnaireService.getQuestionnaireOfTheDay();
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}
		
		if (qotd != null ) {
			
			List<Question> questions1 = null;
			
			try {
				questions1 = questionService.getSectionOneQuestions(qotd.getId());
			} catch (BadRetrievalException e) {
				ctx.setVariable("errorMsg", e.getMessage());
			}
			session.setAttribute("questions1", questions1);
			ctx.setVariable("questions1", questions1);
		}
		
		templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}