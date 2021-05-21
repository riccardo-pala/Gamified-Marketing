package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
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

import entities.Questionnaire;
import exceptions.BadRetrievalException;
import services.QuestionnaireService;

/**
 * Servlet implementation class GoToDeletionPage
 */
@WebServlet("/GoToDeletionPage")
public class GoToDeletionPage extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name="services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	public void init() throws ServletException {
		
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
   
    public GoToDeletionPage() {
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
		
		List<Questionnaire> questionnaires = null;
		try {
			questionnaires = questionnaireService.getAllQuestionnaires();
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/deletionpage.html", ctx, response.getWriter());
			return;
		}
		
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		
		ArrayList<Questionnaire> questionn= new ArrayList<Questionnaire>();
		
		for(Questionnaire quest : questionnaires)
			if(quest.getDate().before(date) && !quest.getDate().toString().equals(date.toString())) {
				questionn.add(quest);
				System.out.println(quest.getDate().toString());
			}
		
		ctx.setVariable("questionnaires", questionn);
		templateEngine.process("/WEB-INF/deletionpage.html", ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
