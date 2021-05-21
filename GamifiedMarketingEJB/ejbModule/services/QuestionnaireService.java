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
import exceptions.BadUpdateException;

@Stateless
public class QuestionnaireService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public QuestionnaireService() {
	}
	
	public Questionnaire getQuestionnaireOfTheDay() throws BadRetrievalException {

		List<Questionnaire> qList = null;
		
		Date today = new Date(); // new instance of Date object returns the current date
		
		try {
			qList = em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class)
					.setParameter(1, today).getResultList();
		} catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questionnaire of the day.");
		}

		Questionnaire q = null;
		
		if(!qList.isEmpty())
			q = qList.get(0);
		
		return q;
	}
	
	public void createQuestionnaire(int productId, Date date, ArrayList<QuestionOne> questions) throws BadRetrievalException, BadUpdateException {
		
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
		}
		
		try {
			em.persist(questionnaire);
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadUpdateException("Problems during the creation of the questionnaire, retry.");
		}
		
	}
	
	public List<Questionnaire> getAllQuestionnaires() throws BadRetrievalException {
		
		List<Questionnaire> qList = null;
		try {
			qList = em.createNamedQuery("Questionnaire.findAll", Questionnaire.class).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Unable to retrieve the questionnaires.");
		}
		
		return qList;
	}
	
	public void deleteQuestionnaire(int questionnaireId) throws BadUpdateException {

		Questionnaire q = null;
		
		try {
			q = em.find(Questionnaire.class, questionnaireId);
			em.remove(q);
		} catch (PersistenceException e) {
			throw new BadUpdateException("Unable to delete the questionnaire.");
		}
	}
	
	public boolean checkDateOfQuestionnaire(Date date) throws BadRetrievalException {
		
		List<Questionnaire> qList = null;
		
		try {
			qList = em.createNamedQuery("Questionnaire.findByDate",Questionnaire.class).setParameter(1,date).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Problems during the creation of the questionnaire, retry.");
		}
		
		return qList.isEmpty();
	}
}
