package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.QuestionOne;
import entities.QuestionTwo;
import exceptions.BadRetrievalException;

@Stateless
public class QuestionService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public QuestionService() {
	}
	
	
	public List<QuestionOne> getSectionOneQuestions(int questionnaireId) throws BadRetrievalException {
		
		List<QuestionOne> qList = null;
		
		try {
			qList = em.createNamedQuery("Question.findSectionOneByQuestionnaire", QuestionOne.class)
					.setParameter(1, questionnaireId).getResultList();
			// TODO: check if not null
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questions.");
		}
		
		return qList;
	}
	
	public List<QuestionTwo> getSectionTwoQuestions() throws BadRetrievalException {
		
		List<QuestionTwo> qList = null;
		
		try {
			qList = em.createNamedQuery("Question.findSectionTwo", QuestionTwo.class)
					.getResultList();
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questions.");
		}
		
		return qList;		
	}
	
	
}