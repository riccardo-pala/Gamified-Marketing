package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Answer;
import entities.QuestionOne;
import entities.QuestionTwo;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRequestException;
import exceptions.BadRetrievalException;

@Stateless
public class QuestionService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public QuestionService() {
	}
	
	public List<QuestionOne> getSectionOneQuestions(int questionnaireId) throws BadRetrievalException, BadRequestException {
		
		List<QuestionOne> qList = null;
		
		try {
			qList = em.createNamedQuery("QuestionOne.findByQuestionnaire", QuestionOne.class)
					.setParameter(1, questionnaireId).getResultList();
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questions.");
		}
		
		if (qList == null || qList.isEmpty()) {
			throw new BadRequestException("It seems that questionnaire has no Marketing questions! Please contact system administrator.");
		}
		
		return qList;
	}
	
	public List<QuestionTwo> getSectionTwoQuestions() throws BadRetrievalException, BadRequestException {
		
		List<QuestionTwo> qList = null;
		
		try {
			qList = em.createNamedQuery("QuestionTwo.findAll", QuestionTwo.class)
					.getResultList();
		}
		catch(PersistenceException | NullPointerException e) {
			throw new BadRetrievalException("Unable to retrieve the questions.");
		}
		
		if (qList == null || qList.isEmpty()) {
			throw new BadRequestException("It seems that questionnaire has no Statistical questions! Please contact system administrator.");
		}
		
		return qList;		
	}
	
}