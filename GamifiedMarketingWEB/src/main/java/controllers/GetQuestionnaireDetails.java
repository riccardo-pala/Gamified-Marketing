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

import entities.Log;
import exceptions.BadRetrievalException;
import exceptions.CredentialsException;
import services.AccessService;


@WebServlet("/GetQuestionnaireDetails")
public class GetQuestionnaireDetails extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name="services/AccessService")
	private AccessService accessService;
       
   
    public GetQuestionnaireDetails() {
        super();
    }
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
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
		
		List<Log> submitted = null;
		List<Log> canceled = null;
		
		int questionnaireId = Integer.parseInt(request.getParameter("questionnaireid"));
		
		try {
			submitted = accessService.getAllSubmittedQuestionnaire(questionnaireId);
			canceled = accessService.getAllCancelledQuestionnaire(questionnaireId);
		} catch (BadRetrievalException e) {
			ctx.setVariable("errorMsg", e.getMessage());
		}
		
		String path = "/WEB-INF/questionnairedetailspage.html";
		
		ctx.setVariable("submitted", submitted);
		ctx.setVariable("canceled", canceled);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
