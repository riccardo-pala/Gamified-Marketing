package services;


import java.util.List;

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
	
	
	public Product getProductByName(String productname) throws BadRetrievalException {
		
		List<Product> p = null;
		
		try {
			p = em.createNamedQuery("Product.findByName", Product.class)
					.setParameter(1,productname).getResultList();			
		} catch(PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the product of the day.");
		}
		
		if(p.isEmpty())
			return null;
		else
		return p.get(0);		
	}
	
	public Product createProduct(String productname,byte[] photo) {
		Product p = new Product(productname,photo);
		em.persist(p);
		return p;
	}
	
}
