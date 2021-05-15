package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Product;
import entities.QuestionOne;
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
		} catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questionnaire of the day.");
		}
		
		return q;
	}
	
	public void createQuestionnaire(int productId, Date date, ArrayList<QuestionOne> questions) throws BadRetrievalException {
		
		Product p = null;
		 
		try {
			p = em.find(Product.class, productId);
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Problems during the creation of the questionnaire, retry.");
		}
		
		Questionnaire questionnaire = new Questionnaire(date, p);
		
		for(int i=0; i<questions.size(); i++) {
			questionnaire.addQuestion(questions.get(i));
			System.out.println(questions.get(i).getText());
		}
		
		em.persist(questionnaire);
	}
	
	public List<Questionnaire> getAllQuestionnaire() {
		
		List<Questionnaire> p = em.createQuery("SELECT q FROM Questionnaire q ORDER BY q.date ASC", Questionnaire.class).getResultList();
		
		return p;
	}
	
	public void deleteQuestionnaire(int questionnaireId) {
		
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		em.remove(q);
	}
}
