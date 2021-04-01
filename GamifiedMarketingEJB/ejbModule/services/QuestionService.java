package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Question;
import exceptions.BadRetrievalException;

@Stateless
public class QuestionService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public QuestionService() {
	}
	
	
	public List<Question> getSectionOneQuestions(int questionnaireId) throws BadRetrievalException {
		
		List<Question> qList = null;
		
		try {
			qList = em.createNamedQuery("Question.findSectionOneByQuestionnaire", Question.class)
					.setParameter(1, questionnaireId).getResultList();
			// TODO: check if not null
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questions.");
		}
		
		return qList;
	}
	
	public List<Question> getSectionTwoQuestions() throws BadRetrievalException {
		
		List<Question> qList = null;
		
		try {
			qList = em.createNamedQuery("Question.findSectionTwo", Question.class)
					.getResultList();
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questions.");
		}
		
		return qList;		
	}
	
	
}