package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.BadWord;
import exceptions.BadRetrievalException;

@Stateless
public class BadWordService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public boolean checkOffensiveWords(ArrayList<String> answers) throws BadRetrievalException {
		
		List<BadWord> words = null;
		
		try {
			words = em.createNamedQuery("BadWord.findAll", BadWord.class).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve some information.");
		}
		
		for(String a : answers)
			for(BadWord w : words)
				if(a.contains(w.getWord()))
					return false;
	
		return true;
	}
	

}
