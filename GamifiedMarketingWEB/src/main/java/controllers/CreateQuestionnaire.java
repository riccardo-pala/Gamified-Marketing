package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Product;
import entities.QuestionOne;
import exceptions.BadRequestException;
import exceptions.BadRetrievalException;
import exceptions.BadUpdateException;
import exceptions.CreateProductException;
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

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		List<Product> products = null;
		Date questionnaireDate = null;
		
		/*---- Check the date ----*/
		
		try {
			questionnaireDate = Date.valueOf(request.getParameter("questionnairedate"));
			products = productService.getAllProducts();
			ctx.setVariable("products", products);
			questionnaireService.checkDateOfQuestionnaire(questionnaireDate);
		} catch (BadRetrievalException | BadRequestException | IllegalArgumentException e) {
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
			return;
		}
		
		/*---- Check the questions ----*/
		
		ArrayList<QuestionOne> questions = new ArrayList<QuestionOne>();
				
		String[] reqQuestions = request.getParameterValues("questions");
		
		if (reqQuestions != null)
			for(int i=0; i<reqQuestions.length; i++)
				if(!reqQuestions[i].isBlank()) {
					QuestionOne q = new QuestionOne(reqQuestions[i]);
					questions.add(q);
				}
		
		if(questions.size()<1) {
			ctx.setVariable("errorMsg", "Please, insert at least one question.");
			templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
			return;
		}
		
		/*---- Check the product ----*/
		
		String newProductName = request.getParameter("newproductname");

		int productid = -1;
		
		boolean isNewProduct = false;

		if(newProductName != null && !newProductName.isEmpty()) {
			
			isNewProduct = true;
			
			Product p = null;
			
			Part imgFile = request.getPart("newproductimage");
			InputStream imgContent = imgFile.getInputStream();
			byte[] imgByteArray = ImageUtils.readImage(imgContent);
			
			try {
				p = productService.createProduct(newProductName, imgByteArray);
			} catch (BadRetrievalException | CreateProductException | BadUpdateException e) {
				ctx.setVariable("errorMsg", e.getMessage());
				templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
				return;
			}
			
			if (p != null) productid = p.getId();
		}
		else if(request.getParameter("productid") != null) {
			
			boolean productExists = false;
			
			try {
				productid = Integer.parseInt(request.getParameter("productid"));
				productExists = productService.checkProduct(productid);
			} catch (NumberFormatException e) {
				ctx.setVariable("errorMsg", "Invalid data, please retry.");
				templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
				return;
			} catch (BadRetrievalException e) {
				ctx.setVariable("errorMsg", e.getMessage());
				templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
				return;
			}
			
			if (!productExists) {
				ctx.setVariable("errorMsg", "Selected product does not exist!");
				templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
				return;
			}
		}
		else {
			ctx.setVariable("errorMsg", "Invalid data, please retry.");
			templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
			return;
		}
		
		if (productid <= 0) {
			ctx.setVariable("errorMsg", "There was a problem during the creation of the questionnaire, please retry.");
			templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
			return;
		}
		
		/*---- Create the questionnaire ----*/
		
		try {
			questionnaireService.createQuestionnaire(productid, questionnaireDate, questions);
		} catch (BadRetrievalException | BadUpdateException e) {
			if(isNewProduct) deleteNewProduct(productid);
			ctx.setVariable("errorMsg", e.getMessage());
			templateEngine.process("/WEB-INF/creationpage.html", ctx, response.getWriter());
			return;
		}
		
		String path = getServletContext().getContextPath() + "/GoToAdminHomepage";
		response.sendRedirect(path);
	}
	
	private void deleteNewProduct(int productid) {
		try {
			productService.removeProduct(productid);
		} catch (BadRetrievalException e1) {
			//do nothing
		}
	}
}
