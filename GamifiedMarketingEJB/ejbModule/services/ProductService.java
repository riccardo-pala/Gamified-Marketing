package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Product;
import entities.Questionnaire;
import exceptions.BadRetrievalException;

@Stateless
public class ProductService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public ProductService() {
	}
	
	
	public Product getProductById(int productId) throws BadRetrievalException {
		
		Product p = null;
		
		try {
			p = (Product) em.createNamedQuery("Product.findById", Product.class)
					.setParameter(1, productId);			
		} catch(PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the product of the day.");
		}
		
		return p;		
	}
}
