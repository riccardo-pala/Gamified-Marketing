package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Answer;
import entities.Question;
import entities.QuestionTwo;
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
	
	public void insertAnswers(int userId, int questionnaireId, List<String> answers1, List<String> answers2, List<QuestionTwo> questions2) throws BadRetrievalException {
		
		User user = null;
		Questionnaire questionnaire = null;
		
		try {
			user = em.find(User.class, userId);
			questionnaire = em.find(Questionnaire.class, questionnaireId);
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve some information.");
		}
		
		for(int i=0; i<questionnaire.getQuestions().size(); i++) { 
			Answer a = new Answer(user, questionnaire.getQuestions().get(i), questionnaire, answers1.get(i));
			questionnaire.getQuestions().get(i).addAnswer(a);
		}
		
		em.persist(questionnaire);
		
		for(int i=0; i<questions2.size(); i++) {
			if(!answers2.get(i).isBlank()) {
				Answer a = new Answer(user, questions2.get(i), questionnaire, answers2.get(i));
				em.persist(a);
			}
		}
	}
}
