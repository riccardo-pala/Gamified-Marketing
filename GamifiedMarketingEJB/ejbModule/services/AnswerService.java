package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Answer;

@Stateless
public class AnswerService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AnswerService() {
	}
	
	public List<Answer> getAnswersByUserAndQuestionnaire(int userId, int questionnaireId){
		List<Answer> answers = em.createQuery("SELECT a FROM Answer a WHERE a.questionnaire.id=?1 AND a.user.id=?2")
								.setParameter(1, questionnaireId).setParameter(2, userId).getResultList();
		return answers;
	}
	

}
