package services;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Log;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRequestException;
import exceptions.BadRetrievalException;

@Stateless
public class AccessService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AccessService() {
	}
	
	public void insertAccess(int userId, int questionnaireId) throws BadRetrievalException, BadRequestException {
		
		User user = null;
		Questionnaire questionnaire = null;
		
		try {
			user = em.find(User.class, userId);
			questionnaire = em.find(Questionnaire.class, questionnaireId);
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve some information.");
		}
		
		if (user == null || questionnaire == null)
			throw new BadRequestException("Malformed request or missing data.");
		
		Log access = new Log(user, new Timestamp(System.currentTimeMillis()), questionnaire, false);
		
		questionnaire.addAccess(access);
		em.persist(questionnaire);
	}
	
	public void updateAccessAfterSubmit(int userId, int questionnaireId) throws BadRetrievalException, BadRequestException {
		
		User user = null;
		Questionnaire questionnaire = null;
		List<Log> logs = null;
		
		try {
			user = em.find(User.class, userId);
			questionnaire = em.find(Questionnaire.class, questionnaireId);
			logs = em.createNamedQuery("Log.findByQuestionnaireAndUser", Log.class)
					.setParameter(1, questionnaireId).setParameter(2, userId).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve some information.");
		}
		
		if (user == null || questionnaire == null || logs == null || logs.isEmpty())
			throw new BadRequestException("Malformed request or missing data.");
		
		Log submittedAccess = null;
		
		questionnaire.removeAccess(logs.get(0));
		submittedAccess = logs.get(0);
		submittedAccess.setSubmitted(true);
		questionnaire.addAccess(submittedAccess);

		em.persist(questionnaire);
	}
	
	public boolean checkSubmittedAccess(int userId, int questionnaireId) throws BadRetrievalException, BadRequestException {
		
		User user = null;
		List<Log> submittedAccesses = null;
		
		try {
			user = em.find(User.class, userId);
			submittedAccesses = em.createNamedQuery("Log.findByQuestionnaireSubmitted",Log.class)
					.setParameter(1, questionnaireId).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve some information.");
		}

		if (user == null || submittedAccesses == null)
			throw new BadRequestException("Malformed request or missing data.");
		
		for(Log access : submittedAccesses)
			if(access.getUser().equals(user) && access.getSubmitted())
				return true;
		
		return false;
	}
	
	public List<Log> getAllSubmittedQuestionnaire(int questionnaireId) throws BadRetrievalException {
		
		List<Log> submittedAccesses= null;
		
		try {
			submittedAccesses = em.createNamedQuery("Log.findByQuestionnaireSubmitted",Log.class)
					.setParameter(1, questionnaireId).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve accesses information.");
		}
		
		return submittedAccesses;
	}
	
	public List<Log> getAllCancelledQuestionnaire(int questionnaireId) throws BadRetrievalException {
		
		List<Log> cancelledAccesses= null;
		
		try {
			cancelledAccesses = em.createNamedQuery("Log.findByQuestionnaireCancelled",Log.class).setParameter(1, questionnaireId).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve accesses information.");
		}
		
		return cancelledAccesses;
		
	}
	

}