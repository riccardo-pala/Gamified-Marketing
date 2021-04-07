package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Answer;
import exceptions.CredentialsException;

@Stateless
public class AnswerService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AnswerService() {
	}
	
	public List<Answer> getAnswersByUserAndQuestionnaire(int userId, int questionnaireId){
		List<Answer> answers =null;
		
		answers=em.createNamedQuery("Answer.findByUserandQuestionnaire",Answer.class)
								.setParameter(1, questionnaireId)
								.setParameter(2, userId)
								.getResultList();
		return answers;
	}
	

}
