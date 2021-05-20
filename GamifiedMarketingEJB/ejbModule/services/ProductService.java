package services;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Product;
import entities.Questionnaire;
import exceptions.BadRetrievalException;
import exceptions.CreateProductException;

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
			throw new BadRetrievalException("Unable to retrieve the product.");
		}
		
		if(p.isEmpty())
			return null;
		else
			return p.get(0);		
	}
	
	public boolean checkProduct(int productId) throws BadRetrievalException {
		
		Product p = null;
		
		try {
			p = em.find(Product.class, productId);		
		} catch(PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the product.");
		}
		
		return p != null;
	}
	
	public List<Product> getAllProducts() throws BadRetrievalException {
		
		List<Product> p = null;
		
		try {
			p = em.createNamedQuery("Product.findAll", Product.class)
					.getResultList();			
		} catch(PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the product list, try again.");
		}
		
		return p;		
	}
	
	public Product createProduct(String productname, byte[] photo) throws BadRetrievalException, CreateProductException {
		
		if(getProductByName(productname) == null) {
			Product p = new Product(productname, photo);
			em.persist(p);
			return p;
		}
		else {
			throw new CreateProductException("A product with the same name already exists. Please choose another one.");
		}
	}
	
	public void removeProduct(int productId) {
		
		Product p = em.find(Product.class, productId);
		
		if(p != null) em.remove(p);
	}
	
}
