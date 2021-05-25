package controllers;

import java.io.IOException;
import java.sql.Timestamp;
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

import entities.Question;
import entities.QuestionOne;
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
		
		Questionnaire qotd = null;
		try {
			qotd = questionnaireService.getQuestionnaireOfTheDay();
			if (qotd == null) {
				session.setAttribute("questions1", null);
				session.setAttribute("answers1", null);
				session.setAttribute("questions2", null);
				session.setAttribute("answers2", null);
				ctx.setVariable("errorMsg", "There is no questionnaire of the day.");
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
				return;
			}
			if(accessService.checkSubmittedAccess(user.getId(), qotd.getId())) { 
				// l'utente ha già compilato il questionario
				ctx.setVariable("warningMsg", "You have already filled the questionnaire today!");
				templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
				return;
			}
		} catch (BadRetrievalException | BadRequestException e) {
			List<String> answers2 = (List<String>) session.getAttribute("answers2");
			ctx.setVariable("answers2", answers2);
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/qotdtwo.html", ctx, response.getWriter());
			return;
		}
		
		String action = null;
		if (request.getParameter("button") != null) {
			action = request.getParameter("button");
		}
		else {
			String path = getServletContext().getContextPath() + "/GoToQotdOne";
			response.sendRedirect(path);
			return;
		}
	
		// salviamo in sessione le risposte della sezione 2 in caso si tornasse indietro

		String[] answers2 = request.getParameterValues("answers2");
		List<String> session_answers2 = new ArrayList<String>();
		
		if (answers2 != null)
			for(int i = 0; i < answers2.length; i++)
				session_answers2.add(answers2[i]);
		
		session.setAttribute("answers2", session_answers2);
		
		if (action.equals("Previous")) {

			List<QuestionOne> questions1 = null;
			List<String> answers1 = null;
			
			questions1 = (List<QuestionOne>) session.getAttribute("questions1");
			answers1 = (List<String>) session.getAttribute("answers1");
			ctx.setVariable("questions1", questions1);
			ctx.setVariable("answers1", answers1);
			
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
			
			return;
		}
		
		else if (action.equals("Submit")) {
			
			List<String> answers_text_one = null;
			List<String> answers_text_two = null;
			
			if (session.getAttribute("answers1") != null)
				answers_text_one = (List<String>) session.getAttribute("answers1"); // aggiungo prima risposte sezione 1 (mandatory)
			
			// CONTROLLO SU DOMANDE OBBLIGATORIE SEZIONE 1
			for(String mandatory_answer : answers_text_one)
				if(mandatory_answer.isBlank()) {
					// stesso comportamento di action = "Previous"
					List<String> questions1 = null;
					if (session.getAttribute("questions1") != null)
						questions1 = (List<String>) session.getAttribute("questions1");

					ctx.setVariable("questions1", questions1);
					ctx.setVariable("answers1", answers_text_one);
					ctx.setVariable("requiredMsg", "You MUST answer all the questions to submit the questionnaire!");
					
					templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
					
					return;
				}
				
			/*for(String answer_text : session_answers2)
				answers_text_two.add(answer_text); // poi aggiungo risposte sezione 2*/
			
			boolean goodAnswers;
			
			try {
				goodAnswers = badWordService.checkOffensiveWords((ArrayList<String>) answers_text_one);
			} catch (BadRetrievalException e) {
				
				List<QuestionOne> questions1 = null;
				List<String> answers1 = null;
				//WARNING:PRIMA VENGONO PASSATI OGGETTI E ORA STRINGHE??
				questions1 = (List<QuestionOne>) session.getAttribute("questions1");
				answers1 = (List<String>) session.getAttribute("answers1");

				ctx.setVariable("questions1", questions1);
				ctx.setVariable("answers1", answers1);
				ctx.setVariable("errorMsg", e.getMessage());
				
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
				
				return;
			}
			
			if(!goodAnswers) {
				
				try {
					userService.banUser(user.getId());
				} catch (BadRetrievalException | BadUpdateException e) {
					ctx.setVariable("errorMsg", e.getMessage());
				}
				
				session.setAttribute("questions1", null);
				session.setAttribute("answers1", null);
				session.setAttribute("questions2", null);
				session.setAttribute("answers2", null);
				
				ctx.setVariable("BanMsg", "Due to the insertion of offensive words in the answers you have provided, you are banned from the site");
				templateEngine.process("/WEB-INF/bannedpage.html", ctx, response.getWriter());
				
				return;
			}
			
			
			try {
				// aggiungo domande
				answerService.insertAnswersOfSectionOne(user.getId(), qotd.getId(), answers_text_one);
				questionService.associateAnswerAndQuestionOfSection2(user.getId(),qotd.getId(),session_answers2);
				// aggiorno l'accesso visto che il questionario è stato inviato
				accessService.updateAccessAfterSubmit(user.getId(), qotd.getId());
				
			} catch (BadRetrievalException | BadRequestException e) {
				ctx.setVariable("errorMsg", e.getMessage());
			}
			
			// cancello domande e risposte dalla sessione anche nel caso Submit non solo nel caso Cancel
			session.setAttribute("questions1", null);
			session.setAttribute("answers1", null);
			session.setAttribute("questions2", null);
			session.setAttribute("answers2", null);
			
			ctx.setVariable("submitted", true);
			templateEngine.process("/WEB-INF/greetings.html", ctx, response.getWriter());
			
			return;
		}
		
		else if (action.equals("Cancel")) {
			
			session.setAttribute("questions1", null);
			session.setAttribute("answers1", null);
			session.setAttribute("questions2", null);
			session.setAttribute("answers2", null);
			
			String path = getServletContext().getContextPath() + "/GoToHomepage";
			response.sendRedirect(path);
			 
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
