package services;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.Log;
import entities.Product;
import entities.Questionnaire;
import entities.User;
import exceptions.BadRetrievalException;

@Stateless
public class AccessService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	
	public AccessService() {
	}
	
	public void insertAccess(int userId, int questionnaireId) throws BadRetrievalException {
		// Local.now() as accessTime
		// Submitted is false initially
		// Q of TODAY !
		
		User user = em.find(User.class, userId);
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		
		Log access = new Log(user, ts, questionnaire, false);
		
		user.addAccess(access);
		em.persist(user); // cascade
		
		// non sono molto convinto se farlo così.. ho copiato MissionService
		
	}

}