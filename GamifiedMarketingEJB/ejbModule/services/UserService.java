package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.User;
import exceptions.BadRetrievalException;
import exceptions.BadUpdateException;
import exceptions.CreateProfileException;
import exceptions.CredentialsException;

@Stateless
public class UserService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	public UserService() {
	}

	public User checkCredentials(String username, String password) throws  NonUniqueResultException, CredentialsException {
		
		List<User> uList = null;
		
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class)
					.setParameter(1, username).setParameter(2, password)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials.");
		}
		if (uList.isEmpty())
			throw new CredentialsException("Wrong username or password.");
		else if (uList.size() == 1)
			return uList.get(0);
		
		throw new NonUniqueResultException("More than one user registered with same credentials, please contact system administrator.");
	}
	
	public User insertNewUser(String firstname, String lastname, String username, String email, String password) throws CredentialsException, CreateProfileException {
		
		List<User> usernameList = null;
		List<User> emailList = null;
		
		try {
			usernameList = em.createNamedQuery("User.findByUsername", User.class)
					.setParameter(1, username).getResultList();
			emailList = em.createNamedQuery("User.findByEmail", User.class)
					.setParameter(1, email).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials, retry.");
		}

		if (!usernameList.isEmpty())
			throw new CreateProfileException("The username " + username + " is not available!");
		else if (!emailList.isEmpty())
			throw new CreateProfileException("The email " + email + " is registered with another account!");	
		else {
			User user = new User(firstname, lastname, username, email, password, false, false, 0);
			try {
				em.persist(user);
			} catch (PersistenceException e) {
				throw new CreateProfileException("Could not create a new profile, retry");
			}
			return user;
		}	
	}
	
	
	public List<User> getUsersOrderedByPoints(int userId) throws BadRetrievalException {
		
		List<User> uList = null;
		try {
			uList = (List<User>) em.createQuery("SELECT u FROM User u WHERE u.isBanned =?1 AND u.isAdmin =?2 ORDER BY u.totalPoints DESC", User.class)
					.setParameter(1,false)
					.setParameter(2,false)
					.getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Could not retrieve user information.");
		}
		
		if (uList == null || uList.isEmpty())
			return null;
		else {
			for(int i=0;i<uList.size();i++)
				em.refresh(uList.get(i));
			return uList;
		}	
	}
	
	public void banUser(int userId) throws BadRetrievalException, BadUpdateException {
		
		User user = null;
		
		try {
			user = em.find(User.class,userId);
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Could not retrieve user information");
		}
		
		user.setIsBanned(true);
		
		try {
			em.persist(user);
		} catch (PersistenceException e) {
			throw new BadUpdateException("Could not update user information.");
		}
	}

}
