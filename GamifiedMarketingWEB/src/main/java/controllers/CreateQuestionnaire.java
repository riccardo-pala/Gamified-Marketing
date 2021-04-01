package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Product;
import entities.Question;
import entities.Questionnaire;
import exceptions.BadRetrievalException;
import services.ProductService;
import services.QuestionService;
import services.QuestionnaireService;
import utils.ImageUtils;

/**
 * Servlet implementation class CreateQuestionnaire
 */
@WebServlet("/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name="services/ProductService")
	private ProductService productService;
	
	@EJB(name="services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	@EJB(name="services/QuestionService")
	private QuestionService questionService;
    
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
       
   
    public CreateQuestionnaire() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String productname=null;
		Date questdate=null;
		ArrayList<Question> questions= new ArrayList<Question>();
		
		Part imgFile = request.getPart("productimage");
		InputStream imgContent = imgFile.getInputStream();
		byte[] imgByteArray = ImageUtils.readImage(imgContent);
		
		productname=request.getParameter("productname");
		questdate= Date.valueOf(request.getParameter("productdate"));
		
		Product p = new Product();
		
		int x=1;
		
		while(request.getParameter("quest"+x+"") != null) {
			
			Question q = new Question(request.getParameter("quest"+x+""),1);
			questions.add(q);
			x++;
		}
		
		try {
			if(productService.getProductByName(productname)==null) {
				
				 p = productService.createProduct(productname, imgByteArray);
				 questionnaireService.createQuestionnaire(p.getId(), questdate, questions);
			}
			
			else {
				
				p=productService.getProductByName(productname);
				 questionnaireService.createQuestionnaire(p.getId(), questdate, questions);
			}
		} catch (BadRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		
		
	}

}
