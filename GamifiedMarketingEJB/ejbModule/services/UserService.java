package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.User;
import exceptions.BadRetrievalException;
import exceptions.CreateProfileException;
import exceptions.CredentialsException;
import exceptions.UpdateProfileException;

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
			usernameList = em.createQuery("SELECT u FROM User u WHERE u.username = ?1", User.class)
					.setParameter(1, username).getResultList();
			emailList = em.createQuery("SELECT u FROM User u WHERE u.email = ?1", User.class)
					.setParameter(1, email).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials, retry");
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
	
	public User findByUsername(String username) throws BadRetrievalException {
		
		List<User> uList = null;
		try {
			uList = (List<User>) em.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class)
					.setParameter(1, username).getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Could not retrieve user information");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		
		throw new NonUniqueResultException("More than one user registered with same credentials, please contact system administrator.");
	}
	
	/*
	public User findByEmail(String email) throws BadRetrievalException {
		
		List<User> uList = null;
		try {
			uList = (List<User>) em.createQuery("SELECT u FROM User u WHERE u.email=?1", User.class)
					.setParameter(1, email).getResultList();
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new BadRetrievalException("Could not retrieve user information");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}
	*/
	
	public List<User> getUsersOrderedByPoints() throws BadRetrievalException {
		
	
		em.clear();
		List<User> uList = null;
		try {
			uList = (List<User>) em.createQuery("SELECT u FROM User u WHERE u.isBanned =?1 AND u.isAdmin =?2 ORDER BY u.totalPoints DESC", User.class)
					.setParameter(1,false)
					.setParameter(2,false)
					.getResultList();
		} catch (PersistenceException e) {
			throw new BadRetrievalException("Could not retrieve user information");
		}
		if (uList.isEmpty())
			return null;
		else
			return uList;
		
	}
	
	public void banUser(int userId) {
		
		User user = em.find(User.class,userId);
		user.setIsBanned(true);
		em.persist(user);

	}
	
	
	
	/* 
	public User updateUser(String username, String newusername, String firstname, String lastname, String password, Supermarket favSupermarket) throws UpdateProfileException, BadRetrievalException{
		
		User user = findByUserName(username);
		if(firstname!=null) user.setFirstname(firstname);
		if(lastname!=null) user.setLastname(lastname);
		if(newusername!=null) user.setUsername(newusername);
		if(password!=null) user.setPassword(email);
		if(password!=null) user.setPassword(password);
		if(favSupermarket!=null) user.setFavouriteSupermarket(favSupermarket);
		try {
			em.persist(user);
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new UpdateProfileException("Could not update profile");
		}
		return user;
	}
	*/	
}
