package controllers;

import java.io.IOException;
import java.util.ArrayList;
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

import entities.QuestionOne;
import entities.QuestionTwo;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRequestException;
import exceptions.BadRetrievalException;
import services.AccessService;
import services.QuestionService;
import services.QuestionnaireService;


@WebServlet("/GoToQotdTwo")
public class GoToQotdTwo extends HttpServlet {
	
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
	
    public GoToQotdTwo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		if (session.isNew() || u == null) {
			response.sendRedirect(loginpath);
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		String[] request_answers1 = request.getParameterValues("answers1");
		List<String> answers1 = new ArrayList<String>();
		
		if (request_answers1 != null)
			for(int i = 0; i < request_answers1.length; i++)
				answers1.add(request_answers1[i]);
		
		List<QuestionOne> questions1 = null;
		Questionnaire qotd = null;
		try {
			qotd = questionnaireService.getQuestionnaireOfTheDay();
			if (qotd == null) {
				session.setAttribute("answers1", null);
				ctx.setVariable("errorMsg", "There is no questionnaire of the day.");
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
				return;
			}
			questions1 = questionService.getSectionOneQuestions(qotd.getId());
			
			if(accessService.checkSubmittedAccess(u.getId(), qotd.getId())) { 

				ctx.setVariable("warningMsg", "You have already filled the questionnaire today!");
				templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
				return;
			}
		} catch (BadRetrievalException | BadRequestException e) {
			ctx.setVariable("questions1", questions1);
			ctx.setVariable("answers1", answers1);
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
			return;
		}
		
		for(String a : answers1) {
			if(a.isBlank()) {
				ctx.setVariable("questions1", questions1);
				ctx.setVariable("answers1", answers1);
				ctx.setVariable("requiredMsg", "You MUST answer all the questions to submit the questionnaire!");
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
				return;
			}
		}

		session.setAttribute("answers1", answers1);
		
		List<QuestionTwo> questions2 = null;
		try {
			questions2 = questionService.getSectionTwoQuestions();
		} catch (BadRetrievalException | BadRequestException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}
		
		ctx.setVariable("questions2", questions2);
		
		templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}