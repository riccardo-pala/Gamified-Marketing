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
import exceptions.BadUpdateException;
import services.AccessService;
import services.AnswerService;
import services.BadWordService;
import services.QuestionService;
import services.QuestionnaireService;
import services.UserService;

@WebServlet("/ManageQuestionnaire")
public class ManageQuestionnaire extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
    
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService questionnaireService;

	@EJB(name = "services/QuestionService")
	private QuestionService questionService;
	
	@EJB(name = "services/AccessService")
	private AccessService accessService;
	
	@EJB(name = "services/AnswerService")
	private AnswerService answerService;
	
	@EJB(name = "services/BadWordService")
	private BadWordService badWordService;
	
	@EJB(name = "services/UserService")
	private UserService userService;
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
    public ManageQuestionnaire() {
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
		
		List<QuestionTwo> questions2 = null;
		Questionnaire qotd = null;
		try {
			qotd = questionnaireService.getQuestionnaireOfTheDay();
			if (qotd == null) {
				session.setAttribute("answers1", null);
				ctx.setVariable("errorMsg", "There is no questionnaire of the day.");
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
				return;
			}
			questions2 = questionService.getSectionTwoQuestions();
			
			if(accessService.checkSubmittedAccess(user.getId(), qotd.getId())) { 

				ctx.setVariable("warningMsg", "You have already filled the questionnaire today!");
				templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
				return;
			}
		} catch (BadRetrievalException | BadRequestException e) {
			ctx.setVariable("questions2", questions2);
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
			return;
		}
		
		String action = request.getParameter("button");
		if (action == null) {
			String path = getServletContext().getContextPath() + "/GoToQotdOne";
			response.sendRedirect(path);
			return;
		}
	
		String[] request_answers2 = request.getParameterValues("answers2");
		List<String> answers2 = new ArrayList<String>();
		
		if (request_answers2 != null)
			for(int i = 0; i < request_answers2.length; i++)
				answers2.add(request_answers2[i]);
		
		List<QuestionOne> questions1;
		try {
			questions1 = questionService.getSectionOneQuestions(qotd.getId());
		} catch (BadRetrievalException | BadRequestException e) {
			ctx.setVariable("questions2", questions2);
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
			return;
		}
		List<String> answers1 = (List<String>) session.getAttribute("answers1");
		
		if (action.equals("Previous")) {

			ctx.setVariable("questions1", questions1);
			ctx.setVariable("answers1", answers1);
			
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
			
			return;
		}
		
		else if (action.equals("Submit")) {
			
			if (answers1 == null) {
				String path = getServletContext().getContextPath() + "/GoToQotdOne";
				response.sendRedirect(path);
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
			
			boolean areGoodAnswers;
			try {
				areGoodAnswers = badWordService.checkOffensiveWords((ArrayList<String>) answers1);
			} catch (BadRetrievalException e) {
				ctx.setVariable("questions2", questions2);
				ctx.setVariable("errorMsg", e.getMessage());
				
				templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
				
				return;
			}
			
			if(!areGoodAnswers) {
				
				try {
					userService.banUser(user.getId());
				} catch (BadRetrievalException | BadUpdateException e) {
					ctx.setVariable("errorMsg", e.getMessage());
				}
				
				session.setAttribute("answers1", null);
				
				ctx.setVariable("BanMsg", "Due to the insertion of offensive words in the answers which you have provided, you are banned from the site");
				templateEngine.process("/WEB-INF/bannedpage.html", ctx, response.getWriter());
				
				return;
			}
			
			try {
				answerService.insertAnswers(user.getId(), qotd.getId(), answers1, answers2, questions2);
				accessService.updateAccessAfterSubmit(user.getId(), qotd.getId());
			} catch (BadRetrievalException | BadRequestException e) {
				ctx.setVariable("errorMsg", e.getMessage());
			}
			
			session.setAttribute("answers1", null);
			
			ctx.setVariable("submitted", true);
			templateEngine.process("/WEB-INF/greetings.html", ctx, response.getWriter());
			
			return;
		}
		
		else if (action.equals("Cancel")) {
			
			session.setAttribute("answers1", null);
			
			String path = getServletContext().getContextPath() + "/GoToHomepage";
			response.sendRedirect(path);
			 
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
