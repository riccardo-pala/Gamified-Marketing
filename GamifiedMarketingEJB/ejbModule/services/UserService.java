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
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password)
					.getResultList();
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}
	
	public User insertNewUser(String firstname, String lastname, String username, String email, String password) throws CredentialsException, CreateProfileException {
		
		List<User> uList = null;
		try {
			uList = em.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class)
					.setParameter(1, username).getResultList();
		} catch (PersistenceException e) {
			//e.printStackTrace();
			throw new CredentialsException("Could not verify credentials");
		}
		if (uList.isEmpty()) {
			
			List<User> lastUser = null;
			lastUser = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT MAX(r.id) FROM User r)", User.class)
					.getResultList();
			int lastId;
				
			if(lastUser.isEmpty()) lastId = 1;
			//else if(lastUser.size() == 1) lastId = lastUser.get(0).getId();
			else throw new NonUniqueResultException("ID values are not unique!");
			
			User user = new User(/*lastId+1, firstname, lastname, username, email, password*/);
			
			try {
				em.persist(user);
			} catch (PersistenceException e) {
				//e.printStackTrace();
				throw new CreateProfileException("Could not create a new profile");
			}
			return user;
		}
		else if (uList.size() == 1)
			throw new NonUniqueResultException("More than one user registered with same username");	
			return null;	
	}
	
	public User findByUsername (String username) throws BadRetrievalException {
		
		List<User> uList = null;
		try {
			uList = (List<User>) em.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class)
					.setParameter(1, username).getResultList();
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
	
	public User findByEmail (String email) throws BadRetrievalException {
		
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
