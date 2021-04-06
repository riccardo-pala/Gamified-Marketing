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
import entities.Questionnaire;
import entities.User;
import exceptions.BadRetrievalException;
import services.AccessService;
import services.QuestionService;
import services.QuestionnaireService;

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
		
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		String action = null;
		User u = (User) session.getAttribute("user");
		Timestamp ts= (Timestamp) session.getAttribute("accessTime");
		Questionnaire q = null;
	
		try {
			q = questionnaireService.getQuestionnaireOfTheDay();
		} catch (BadRetrievalException e) {
			e.printStackTrace();
		}
		
		
		if (request.getParameter("button") != null) {
			action = request.getParameter("button");
		}
		else {
			String path = getServletContext().getContextPath() + "/GoToQotdOne";
			response.sendRedirect(path);
			return;
		}
	
		String[] answers2 = request.getParameterValues("answers2");
		List<String> session_answers2 = new ArrayList<String>();
		
		if (answers2 != null)
			for(int i = 0; i < answers2.length; i++)
				session_answers2.add(answers2[i]);
		
		session.setAttribute("answers2", session_answers2);
		
		if (action.equals("Previous")) {

			List<String> questions1 = null;
			List<String> answers1 = null;
			
			//System.out.println(session.getAttribute("questions1"));
			//System.out.println(session.getAttribute("answers1"));
			
			if (session.getAttribute("questions1") != null)
				questions1 = (List<String>) session.getAttribute("questions1");
			
			if (session.getAttribute("answers1") != null)
				answers1 = (List<String>) session.getAttribute("answers1");

			ctx.setVariable("questions1", questions1);
			ctx.setVariable("answers1", answers1);
			
			templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());
		}
		else if (action.equals("Submit")) {
			try {
				accessService.insertAccess(u.getId(),q.getId(),true,ts);
			} catch (BadRetrievalException e) {
				
				e.printStackTrace();
			}	
		}
		else if (action.equals("Cancel")) {
			
			try {
				accessService.insertAccess(u.getId(),q.getId(),false,ts);
			} catch (BadRetrievalException e) {
				e.printStackTrace();
			}
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
