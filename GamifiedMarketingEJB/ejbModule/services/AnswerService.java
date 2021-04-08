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
import exceptions.CredentialsException;

@Stateless
public class AnswerService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AnswerService() {
	}
	
	public List<Answer> getAnswersByUserAndQuestionnaire(int userId, int questionnaireId){
		List<Answer> answers = null;
		answers=em.createNamedQuery("Answer.findByUserandQuestionnaire",Answer.class)
								.setParameter(1, questionnaireId)
								.setParameter(2, userId)
								.getResultList();
		return answers;
	}
	
	public void insertAnswers(int userId, int questionnaireId, List<String> answersText, List<Question> questions) {
		
		User user = em.find(User.class, userId);
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		Answer answer;
		//check empty text
		for(int i = 0; i < questions.size(); i++) { // la dimensione dei due array dovrebbe essere la stessa
			if(!answersText.get(i).isBlank()) {
				answer = new Answer(user, questions.get(i), questionnaire, answersText.get(i));
				user.addAnswer(answer);
			}
		}
		em.persist(user); // cascade sulle answers
	}
	

}
