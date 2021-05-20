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

import entities.Answer;
import services.AnswerService;
import services.QuestionnaireService;

/**
 * Servlet implementation class GetAnswers
 */
@WebServlet("/GetAnswers")
public class GetAnswers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	private TemplateEngine templateEngine;
	
	@EJB(name="services/AnswerService")
	private AnswerService answerService;
	
	public void init() throws ServletException {
		
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
   
    public GetAnswers() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		
		int questionnaireId = Integer.parseInt(request.getParameter("questionnaireid"));
		int userId = Integer.parseInt(request.getParameter("userid"));
		
		List<Answer> answers = answerService.getAnswersByQuestionnaireAndUser(questionnaireId, userId);
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("answers", answers);
		ctx.setVariable("questionnaireid", questionnaireId);
		templateEngine.process("/WEB-INF/answerspage.html", ctx, response.getWriter());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
