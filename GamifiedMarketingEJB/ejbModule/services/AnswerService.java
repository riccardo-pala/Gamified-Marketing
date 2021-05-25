package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Answer;
import entities.Question;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRetrievalException;

@Stateless
public class AnswerService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AnswerService() {
	}
	
	
	public List<Answer> getAnswersByQuestionnaireAndUser(int questionnaireId, int userId) throws BadRetrievalException{
		
		List<Answer> answers = null;
		
		try {
			answers = em.createNamedQuery("Answer.findByQuestionnaireAndUser", Answer.class)
					.setParameter(1, questionnaireId).setParameter(2, userId).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve list of answers.");
		}
		
		return answers;
	}
	
	public void insertAnswers(int userId, int questionnaireId, List<String> answersText, List<Question> questions) throws BadRetrievalException {
		
		User user = null;
		Questionnaire questionnaire = null;
		
		try {
			user = em.find(User.class, userId);
			questionnaire = em.find(Questionnaire.class, questionnaireId);
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve some information.");
		}
		
		Answer answer;
		//check empty text
		for(int i = 0; i < questions.size(); i++) 
			if(!answersText.get(i).isBlank()) {
				answer = new Answer(user, questions.get(i), questionnaire, answersText.get(i));
				user.addAnswer(answer);
			}
		
		em.persist(user);
	}
}
