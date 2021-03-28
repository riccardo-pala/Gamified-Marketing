package services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Questionnaire;
import exceptions.BadRetrievalException;

@Stateless
public class QuestionnaireService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public QuestionnaireService() {
	}
	
	
	public Questionnaire getQuestionnaireOfTheDay() throws BadRetrievalException {
		
		Questionnaire q = null;
		
		Date today = new Date(); // new instance of Date object returns the current date
		
		try {
			List<Questionnaire> qList = em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class)
					.setParameter(1, today).getResultList();
			if(!qList.isEmpty())
				q = qList.get(0);
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questionnaire of the day.");
		}
		
		return q;
	}
	
}
