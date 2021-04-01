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
		
		//TODO: 3 situazioni diverse a seconda dei bottoni
		
		// PREVIOUS
		String action = request.getParameter("button");
		if(request.getParameter("button") == null) {
			// do something
		} 
	
		
		if(action.equals("Previous") && session.getAttribute("questions1") != null 
				&& session.getAttribute("answers1") != null) { // salvate in sessione dal servlet GoTOQotdTwo (ERROR)
			
			List<String> answers1 = (List<String>) session.getAttribute("answers1");
			List<String> questions1 = (List<String>) session.getAttribute("questions1");
			System.out.println(questions1.size());
			
			if(answers1.size() == questions1.size()) {
				
				ctx.setVariable("answers1", answers1);
				ctx.setVariable("questions1", questions1);
				ctx.setVariable("new", false); // perché stiamo tornando nella sezione che abbiamo già compilato
				//System.out.println("q: "+((String) questions1.get(0).toString())+" "+((String) questions1.get(1).toString()));
				
				templateEngine.process("/WEB-INF/qotdone.html", ctx, response.getWriter());	
			}
			else {
				// do something}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
}
