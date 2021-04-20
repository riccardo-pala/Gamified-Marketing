package services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Log;
import entities.Product;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRetrievalException;
import exceptions.CredentialsException;

@Stateless
public class AccessService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AccessService() {
	}
	
	public User insertAccess(int userId, int questionnaireId, boolean sub, Timestamp ts) throws BadRetrievalException {
		
		User user = em.find(User.class, userId);
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		
		Log access = new Log(user, ts, questionnaire,sub);
		
		user.addAccess(access);
		em.persist(user);
		
		return user;
	}
	
	public boolean checkSubmittedAccess(int userId, int questionnaireId) throws BadRetrievalException {
		
		User user = em.find(User.class, userId);
		List<Log> submittedAccesses= em.createNamedQuery("Log.findByQuestionnaireSubmitted",Log.class)
				.setParameter(1, questionnaireId).getResultList();

		for(Log access : submittedAccesses) {
			if(access.getUser().equals(user) && access.getSubmitted())
				return true; // l'utente ha già compilato e inviato il questionario
		}
		
		return false;
	}
	
	public List<Log> getAllSubmittedQuestionnaire(int questionnaireId) throws CredentialsException{
		
		List<Log> submittedAccesses= null;
		
		try {
			submittedAccesses = em.createNamedQuery("Log.findByQuestionnaireSubmitted",Log.class).setParameter(1, questionnaireId).getResultList();
			} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new CredentialsException("Could not verify credentials.");
		}
		
		return submittedAccesses;
	}
	
	public List<Log> getAllCancelledQuestionnaire(int questionnaireId) throws CredentialsException{
		
		List<Log> submittedAccesses= null;
		
		try {
			submittedAccesses = em.createNamedQuery("Log.findByQuestionnaireCanceled",Log.class).setParameter(1, questionnaireId).getResultList();
			} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new CredentialsException("Could not verify credentials.");
		}
		
		return submittedAccesses;
		
	}
	

}