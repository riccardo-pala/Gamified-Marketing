package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.BadWord;

@Stateless
public class BadWordService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public boolean checkOffensiveWords(ArrayList<String> answers) {
		
		List<BadWord> words = null;
		words=em.createNamedQuery("BadWord.findAll",BadWord.class).getResultList();
		
		for(String answer : answers) {
			
			for(BadWord word : words) {
				if(answer.contains(word.getWord()))
				return false;
			}
		}
	
		return true;
	}
	

}
