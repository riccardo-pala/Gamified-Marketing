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
	
	public void insertAccess(int userId, int questionnaireId) throws BadRetrievalException {
		
		User user = em.find(User.class, userId);
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		
		Log access = new Log(user, new Timestamp(System.currentTimeMillis()), questionnaire, false);
		
		user.addAccess(access);
		em.persist(user);
	}
	
	public void updateAccessAfterSubmit(int userId, int questionnaireId) throws BadRetrievalException {
		
		User user = em.find(User.class, userId);
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		
		List<Log> logs = em.createQuery("SELECT a FROM Log a WHERE a.questionnaire.id=?1 AND a.user.id=?2 ORDER BY a.accessTime DESC", Log.class)
				.setParameter(1, questionnaireId).setParameter(2, userId).getResultList();
		
		Log submittedAccess = null;
		if(logs != null) {
			user.removeAccess(logs.get(0));
			submittedAccess = logs.get(0);
			submittedAccess.setSubmitted(true);
			user.addAccess(submittedAccess);
		}
		em.persist(user);
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
	
	public List<Log> getAllSubmittedQuestionnaire(int questionnaireId) throws BadRetrievalException {
		
		List<Log> submittedAccesses= null;
		
		try {
			submittedAccesses = em.createNamedQuery("Log.findByQuestionnaireSubmitted",Log.class).setParameter(1, questionnaireId).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve accesses information.");
		}
		
		return submittedAccesses;
	}
	
	public List<Log> getAllCancelledQuestionnaire(int questionnaireId) throws BadRetrievalException {
		
		List<Log> submittedAccesses= null;
		
		try {
			submittedAccesses = em.createNamedQuery("Log.findByQuestionnaireCanceled",Log.class).setParameter(1, questionnaireId).getResultList();
			} catch (PersistenceException e) {
			throw new BadRetrievalException("Failed to retrieve accesses information.");
		}
		
		return submittedAccesses;
		
	}
	

}