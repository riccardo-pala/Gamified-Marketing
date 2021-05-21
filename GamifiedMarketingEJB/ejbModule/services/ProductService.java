package services;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Product;
import entities.Questionnaire;
import exceptions.BadRetrievalException;
import exceptions.BadUpdateException;
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
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the product list, try again.");
		}
		
		return p;		
	}
	
	public Product createProduct(String productname, byte[] photo) throws BadRetrievalException, CreateProductException, BadUpdateException {
		
		if(getProductByName(productname) == null) {
			Product p = new Product(productname, photo);
			try {
				em.persist(p);
			} catch (PersistenceException e) {
				throw new BadUpdateException("Unable to retrieve the product list, try again.");
			}
			return p;
		}
		else {
			throw new CreateProductException("A product with the same name already exists. Please choose another one.");
		}
	}
	
	public void removeProduct(int productId) throws BadRetrievalException {
		
		Product p = null;
		try {
			p = em.find(Product.class, productId);
		} catch(PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the product.");
		}
		
		if(p != null) {
			try {
				em.remove(p);
			} catch(PersistenceException e) {
				throw new BadRetrievalException("Unable to delete the product, try again.");
			}
		}
	}
	
}
