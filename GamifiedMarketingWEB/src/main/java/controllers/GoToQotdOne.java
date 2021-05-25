package controllers;

import java.io.IOException;
import java.sql.Timestamp;
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
import entities.QuestionOne;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRequestException;
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
		User user = (User) session.getAttribute("user");
		if (session.isNew() || user == null) {
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
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
			return;
		}
		
		if (qotd == null) {
			ctx.setVariable("errorMsg", "There is no questionnaire of the day.");
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
			return;
		}
			
		try {
			if(accessService.checkSubmittedAccess(user.getId(), qotd.getId())) { 
				ctx.setVariable("warningMsg", "You have already filled the questionnaire today!");
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
				return;
			}
			// altrimenti procediamo iniziando a salveare l' accesso in tabella
			accessService.insertAccess(user.getId(), qotd.getId());
		} catch (BadRetrievalException | BadRequestException e) {
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
			return;
		}
		
		List<QuestionOne> questions1 = null;
		try {
			questions1 = questionService.getSectionOneQuestions(qotd.getId());
		} catch (BadRetrievalException | BadRequestException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}
		
		session.setAttribute("questions1", questions1);
		ctx.setVariable("questions1", questions1);
		
		templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}